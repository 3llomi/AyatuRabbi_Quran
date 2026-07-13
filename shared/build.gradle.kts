import java.io.File
import javax.xml.parsers.DocumentBuilderFactory
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import org.w3c.dom.Element

private data class ParsedStrings(
    val strings: Map<String, String>,
    val arrays: Map<String, List<String>>,
)

abstract class SyncSharedStringsTask : DefaultTask() {
    @get:InputFiles
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val sourceFiles: ConfigurableFileCollection

    @get:OutputFile
    abstract val commonStringsFile: RegularFileProperty

    @get:OutputFile
    abstract val androidSharedStringFile: RegularFileProperty

    @get:OutputFile
    abstract val iosSharedStringFile: RegularFileProperty

    @get:OutputDirectory
    abstract val androidResDir: DirectoryProperty

    @get:OutputDirectory
    abstract val iosLocalizableDir: DirectoryProperty

    @TaskAction
    fun sync() {
        val files = sourceFiles.files.sortedBy { it.absolutePath }
        if (files.isEmpty()) {
            throw GradleException("No strings.xml files found under app/src/main/res/values*/")
        }

        val parsedByFile = files.associateWith { parseResources(it) }

        val stringNames = linkedSetOf<String>()
        val arrayNames = linkedSetOf<String>()
        val maxArraySizes = mutableMapOf<String, Int>()
        parsedByFile.values.forEach { parsed ->
            stringNames.addAll(parsed.strings.keys)
            arrayNames.addAll(parsed.arrays.keys)
            parsed.arrays.forEach { (name, values) ->
                val current = maxArraySizes[name] ?: 0
                if (values.size > current) {
                    maxArraySizes[name] = values.size
                }
            }
        }

        val stringEntries = stringNames.sorted().map { xmlName ->
            toTypeName(xmlName) to xmlName
        }
        val arrayEntries = arrayNames.sorted().map { xmlName ->
            toTypeName(xmlName) to xmlName
        }

        assertNoTypeCollisions("Strings", stringEntries)
        assertNoTypeCollisions("StringArrays", arrayEntries)

        writeIfChanged(
            commonStringsFile.get().asFile,
            renderCommonStringsFile(stringEntries, arrayEntries),
        )
        writeIfChanged(
            androidSharedStringFile.get().asFile,
            renderAndroidSharedStringFile(stringEntries, arrayEntries),
        )
        writeIfChanged(
            iosSharedStringFile.get().asFile,
            renderIosSharedStringFile(stringEntries, arrayEntries, maxArraySizes),
        )

        syncAndroidResourceFiles(files, androidResDir.get().asFile)
        syncIosLocalizableFiles(
            files = files,
            parsedByFile = parsedByFile,
            maxArraySizes = maxArraySizes,
            destination = iosLocalizableDir.get().asFile,
        )
    }

    private fun assertNoTypeCollisions(name: String, entries: List<Pair<String, String>>) {
        val duplicateTypes = entries.groupBy { it.first }.filterValues { it.size > 1 }
        if (duplicateTypes.isNotEmpty()) {
            val message = duplicateTypes.entries.joinToString("\n") { (typeName, collisions) ->
                "$typeName <= ${collisions.joinToString { it.second }}"
            }
            throw GradleException("$name collisions after CamelCase conversion:\n$message")
        }
    }

    private fun parseResources(file: File): ParsedStrings {
        val document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file)

        val strings = linkedMapOf<String, String>()
        val stringNodes = document.getElementsByTagName("string")
        for (index in 0 until stringNodes.length) {
            val element = stringNodes.item(index) as? Element ?: continue
            val name = element.getAttribute("name").trim()
            if (name.isNotEmpty()) {
                strings[name] = element.textContent.orEmpty().trim()
            }
        }

        val arrays = linkedMapOf<String, List<String>>()
        val arrayNodes = document.getElementsByTagName("string-array")
        for (index in 0 until arrayNodes.length) {
            val arrayElement = arrayNodes.item(index) as? Element ?: continue
            val name = arrayElement.getAttribute("name").trim()
            if (name.isEmpty()) {
                continue
            }
            val values = mutableListOf<String>()
            val itemNodes = arrayElement.getElementsByTagName("item")
            for (itemIndex in 0 until itemNodes.length) {
                val item = itemNodes.item(itemIndex) as? Element ?: continue
                values += item.textContent.orEmpty().trim()
            }
            arrays[name] = values
        }

        return ParsedStrings(strings = strings, arrays = arrays)
    }

    private fun toTypeName(xmlName: String): String {
        val raw = xmlName
            .split(Regex("[^A-Za-z0-9]+"))
            .filter { it.isNotBlank() }
            .joinToString("") { part ->
                val lower = part.lowercase()
                lower.replaceFirstChar { it.uppercase() }
            }
            .ifBlank { "GeneratedString" }

        return if (raw.first().isDigit()) "S$raw" else raw
    }

    private fun renderCommonStringsFile(
        stringEntries: List<Pair<String, String>>,
        arrayEntries: List<Pair<String, String>>,
    ): String {
        val stringObjects = stringEntries.joinToString("\n") { (typeName, _) ->
            "    object $typeName : Strings()"
        }
        val arrayObjects = arrayEntries.joinToString("\n") { (typeName, _) ->
            "    object $typeName : StringArrays()"
        }

        val arraySection = if (arrayObjects.isBlank()) {
            "sealed class StringArrays"
        } else {
            """
            |sealed class StringArrays {
            |$arrayObjects
            |}
            """.trimMargin()
        }

        return """
            |package com.devlomi.shared
            |
            |// Generated by :shared:syncSharedStrings. Do not edit manually.
            |sealed class Strings {
            |$stringObjects
            |}
            |
            |$arraySection
            |
        """.trimMargin()
    }

    private fun renderAndroidSharedStringFile(
        stringEntries: List<Pair<String, String>>,
        arrayEntries: List<Pair<String, String>>,
    ): String {
        val stringBranches = stringEntries.joinToString("\n") { (typeName, xmlName) ->
            "            is Strings.$typeName -> resolveString(R.string.$xmlName, formatArgs)"
        }

        val arrayBranches = if (arrayEntries.isEmpty()) {
            "            else -> emptyList()"
        } else {
            arrayEntries.joinToString("\n") { (typeName, xmlName) ->
                "            is StringArrays.$typeName -> context.resources.getStringArray(R.array.$xmlName).toList()"
            }
        }

        return """
            |package com.devlomi.shared
            |
            |import android.content.Context
            |
            |// Generated by :shared:syncSharedStrings. Do not edit manually.
            |actual class SharedString(private val context: Context) {
            |    actual fun getString(string: Strings, vararg formatArgs: Any): String {
            |        return when (string) {
            |$stringBranches
            |        }
            |    }
            |
            |    actual fun getStringArray(array: StringArrays): List<String> {
            |        return when (array) {
            |$arrayBranches
            |        }
            |    }
            |
            |    private fun resolveString(resId: Int, formatArgs: Array<out Any>): String {
            |        return if (formatArgs.isEmpty()) context.getString(resId) else context.getString(resId, *formatArgs)
            |    }
            |}
            |
        """.trimMargin()
    }

    private fun renderIosSharedStringFile(
        stringEntries: List<Pair<String, String>>,
        arrayEntries: List<Pair<String, String>>,
        maxArraySizes: Map<String, Int>,
    ): String {
        val stringBranches = stringEntries.joinToString("\n") { (typeName, xmlName) ->
            "            is Strings.$typeName -> \"$xmlName\""
        }

        val arrayPrefixBranches = if (arrayEntries.isEmpty()) {
            "            else -> \"\""
        } else {
            arrayEntries.joinToString("\n") { (typeName, xmlName) ->
                "            is StringArrays.$typeName -> \"$xmlName\""
            }
        }

        val arrayCountBranches = if (arrayEntries.isEmpty()) {
            "            else -> 0"
        } else {
            arrayEntries.joinToString("\n") { (typeName, xmlName) ->
                val count = maxArraySizes[xmlName] ?: 0
                "            is StringArrays.$typeName -> $count"
            }
        }

        return """
            |package com.devlomi.shared
            |
            |import platform.Foundation.NSBundle
            |
            |// Generated by :shared:syncSharedStrings. Do not edit manually.
            |actual class SharedString {
            |    actual fun getString(string: Strings, vararg formatArgs: Any): String {
            |        val key = when (string) {
            |$stringBranches
            |        }
            |        // TODO: apply formatArgs in iOS implementation once formatting flow is finalized.
            |        return localized(key)
            |    }
            |
            |    actual fun getStringArray(array: StringArrays): List<String> {
            |        val prefix = when (array) {
            |$arrayPrefixBranches
            |        }
            |        val size = when (array) {
            |$arrayCountBranches
            |        }
                        |        return (0 until size).map { index -> localized("${'$'}{prefix}__${'$'}{index}") }
            |    }
            |
            |    private fun localized(key: String): String {
            |        return NSBundle.mainBundle.localizedStringForKey(key, key, null)
            |    }
            |}
            |
        """.trimMargin()
    }

    private fun syncAndroidResourceFiles(sourceFiles: List<File>, destinationResDir: File) {
        sourceFiles.forEach { source ->
            val valuesDirName = source.parentFile.name
            val destination = destinationResDir.resolve(valuesDirName).resolve("strings.xml")
            if (!destination.parentFile.exists()) {
                destination.parentFile.mkdirs()
            }
            writeIfChanged(destination, source.readText())
        }

        val expectedOutputs = sourceFiles.map {
            destinationResDir.resolve(it.parentFile.name).resolve("strings.xml").canonicalFile
        }.toSet()

        destinationResDir
            .walkTopDown()
            .filter { it.isFile && it.name == "strings.xml" && it.parentFile.name.startsWith("values") }
            .forEach { existing ->
                if (existing.canonicalFile !in expectedOutputs) {
                    existing.delete()
                }
            }
    }

    private fun syncIosLocalizableFiles(
        files: List<File>,
        parsedByFile: Map<File, ParsedStrings>,
        maxArraySizes: Map<String, Int>,
        destination: File,
    ) {
        val expected = mutableSetOf<File>()

        files.forEach { file ->
            val locale = toLocaleCode(file.parentFile.name)
            val lprojDir = destination.resolve("$locale.lproj")
            val outFile = lprojDir.resolve("Localizable.strings")
            expected += outFile.canonicalFile

            val parsed = parsedByFile[file] ?: ParsedStrings(emptyMap(), emptyMap())
            val content = renderLocalizable(parsed, maxArraySizes)
            writeIfChanged(outFile, content)
        }

        if (!destination.exists()) {
            return
        }

        destination
            .walkTopDown()
            .filter { it.isFile && it.name == "Localizable.strings" }
            .forEach { existing ->
                if (existing.canonicalFile !in expected) {
                    existing.delete()
                }
            }
    }

    private fun toLocaleCode(valuesDirName: String): String {
        if (valuesDirName == "values") {
            return "Base"
        }
        val qualifier = valuesDirName.removePrefix("values-")
        return qualifier.replace("-r", "-")
    }

    private fun renderLocalizable(parsed: ParsedStrings, maxArraySizes: Map<String, Int>): String {
        val lines = mutableListOf<String>()
        lines += "/* Generated by :shared:syncSharedStrings. Dummy localizable values. */"

        parsed.strings.keys.sorted().forEach { key ->
            lines += "\"$key\" = \"TODO_$key\";"
        }

        maxArraySizes.keys.sorted().forEach { arrayName ->
            val size = maxArraySizes[arrayName] ?: 0
            for (index in 0 until size) {
                val key = "${arrayName}__${index}"
                lines += "\"$key\" = \"TODO_$key\";"
            }
        }

        return lines.joinToString("\n", postfix = "\n")
    }

    private fun writeIfChanged(file: File, content: String) {
        if (!file.parentFile.exists()) {
            file.parentFile.mkdirs()
        }
        if (!file.exists() || file.readText() != content) {
            file.writeText(content)
        }
    }
}

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("com.android.kotlin.multiplatform.library")
    id("com.android.lint")
    alias(libs.plugins.ksp)
    alias(libs.plugins.androidx.room) apply false
    id("com.rickclephas.kmp.nativecoroutines") version "1.0.0-ALPHA-38"
}


kotlin {
    jvmToolchain(21)
    // Target declarations - add or remove as needed below. These define
    // which platforms this KMP module supports.
    // See: https://kotlinlang.org/docs/multiplatform-discover-project.html#targets

    androidLibrary {
        namespace = "com.devlomi.shared"
        compileSdk = 36
        minSdk = 24

        androidResources {
            enable = true
        }


//        withHostTestBuilder {
//        }

//        withDeviceTestBuilder {
//            sourceSetTreeName = "test"
//        }.configure {
//            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//        }
    }

    // For iOS targets, this is also where you should
    // configure native binary output. For more information, see:
    // https://kotlinlang.org/docs/multiplatform-build-native-binaries.html#build-xcframeworks

    // A step-by-step guide on how to include this library in an XCode
    // project can be found here:
    // https://developer.android.com/kotlin/multiplatform/migrate
    val xcfName = "sharedKit"

    iosX64 {
        binaries.framework {
            baseName = xcfName
            isStatic = true
            linkerOpts("-framework", "FirebaseCore")
            linkerOpts("-framework", "FirebaseAuth")
            linkerOpts("-framework", "FirebaseFirestore")
            // Suppress the duplicate libraries warning
            linkerOpts("-Xlinker", "-no_warn_duplicate_libraries")

        }
    }

    iosArm64 {
        binaries.framework {
            baseName = xcfName
            isStatic = true
            linkerOpts("-framework", "FirebaseCore")
            linkerOpts("-framework", "FirebaseAuth")
            linkerOpts("-framework", "FirebaseFirestore")
            linkerOpts("-Xlinker", "-no_warn_duplicate_libraries")
        }
    }

    iosSimulatorArm64 {
        binaries.framework {
            baseName = xcfName
            isStatic = true
            linkerOpts("-framework", "FirebaseCore")
            linkerOpts("-framework", "FirebaseAuth")
            linkerOpts("-framework", "FirebaseFirestore")
            // Suppress the duplicate libraries warning
            linkerOpts("-Xlinker", "-no_warn_duplicate_libraries")

        }
    }

    // Source set declarations.
    // Declaring a target automatically creates a source set with the same name. By default, the
    // Kotlin Gradle Plugin creates additional source sets that depend on each other, since it is
    // common to share sources between related targets.
    // See: https://kotlinlang.org/docs/multiplatform-hierarchy.html

    sourceSets {
        all {
            languageSettings.optIn("kotlinx.cinterop.ExperimentalForeignApi")
            languageSettings.optIn("kotlin.experimental.ExperimentalObjCName")
        }
        commonMain {
            kotlin.srcDir("build/generated/sharedStrings/commonMain/kotlin")
            dependencies {

                implementation(libs.kotlinx.coroutines)
                implementation("dev.gitlive:firebase-storage:2.4.0")
//                implementation("dev.gitlive:firebase-analytics:2.4.0")
//                implementation("dev.gitlive:firebase-crashlytics:2.4.0")

                api(libs.androidx.lifecycle.viewmodel)

                api(libs.koin.core)

                implementation(libs.androidx.room.runtime)
//                implementation(libs.androidx.sqlite.bundled)

//                api("org.jetbrains.compose.runtime:runtime:1.6.11")
//                implementation(compose.components.resources)
                implementation(libs.kotlinx.datetime)
                implementation("io.github.vinceglb:filekit-core:0.12.0")
                api("com.rickclephas.kmp:kmp-observableviewmodel-core:1.0.0-BETA-8")

            }
        }

        commonTest {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-test:2.2.0")
            }
        }

        androidMain {
            kotlin.srcDir("build/generated/sharedStrings/androidMain/kotlin")
            dependencies {
                implementation("dev.gitlive:firebase-storage:2.4.0")
//                implementation("dev.gitlive:firebase-analytics:2.4.0")
                api(libs.koin.android)
                implementation(libs.androidx.room.sqlite.wrapper)
                implementation("com.google.firebase:firebase-storage:19.1.1")
//                implementation ("com.google.firebase:firebase-crashlytics:17.2.2")//TODO
                implementation("com.google.firebase:firebase-analytics:17.6.0")
//                implementation("org.jetbrains.compose.runtime:runtime:1.6.11")
            }
        }

//        getByName("androidDeviceTest") {
//            dependencies {
//                implementation("androidx.test.ext:junit:1.3.0")
//                implementation("androidx.test:core:1.5.0")
//                implementation("androidx.test:runner:1.5.2")
//            }
//        }

        iosMain {
            kotlin.srcDir("build/generated/sharedStrings/iosMain/kotlin")
            dependencies {
                // Add iOS-specific dependencies here. This a source set created by Kotlin Gradle
                // Plugin (KGP) that each specific iOS target (e.g., iosX64) depends on as
                // part of KMP’s default source set hierarchy. Note that this source set depends
                // on common by default and will correctly pull the iOS artifacts of any
                // KMP dependencies declared in commonMain.
            }
        }
    }

}
dependencies {
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspIosSimulatorArm64", libs.androidx.room.compiler)
    add("kspIosX64", libs.androidx.room.compiler)
    add("kspIosArm64", libs.androidx.room.compiler)
}

val generatedSharedStringsRoot = layout.buildDirectory.dir("generated/sharedStrings")

val syncSharedStrings by tasks.registering(SyncSharedStringsTask::class) {
    sourceFiles.from(
        rootProject.fileTree(rootProject.layout.projectDirectory.dir("app/src/main/res")) {
            include("values*/strings.xml")
        }
    )
    commonStringsFile.set(generatedSharedStringsRoot.map { it.file("commonMain/kotlin/com/devlomi/shared/Strings.generated.kt") })
    androidSharedStringFile.set(generatedSharedStringsRoot.map { it.file("androidMain/kotlin/com/devlomi/shared/SharedString.android.generated.kt") })
    iosSharedStringFile.set(generatedSharedStringsRoot.map { it.file("iosMain/kotlin/com/devlomi/shared/SharedString.ios.generated.kt") })
    iosLocalizableDir.set(generatedSharedStringsRoot.map { it.dir("iosLocalizable") })
    androidResDir.set(layout.projectDirectory.dir("src/androidMain/res"))
}

tasks.matching {
    val name = it.name
    name.contains("Kotlin", ignoreCase = true) &&
            (name.startsWith("compile", ignoreCase = true) || name.startsWith(
                "ksp",
                ignoreCase = true
            ))
}.configureEach {
    dependsOn(syncSharedStrings)
}

tasks.matching { it.name == "preBuild" }.configureEach {
    dependsOn(syncSharedStrings)
}

