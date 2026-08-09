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

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("com.android.kotlin.multiplatform.library")
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    id("com.android.lint")
    alias(libs.plugins.ksp)
    alias(libs.plugins.androidx.room) apply false
//    id("com.rickclephas.kmp.nativecoroutines") version "1.0.0-ALPHA-38"
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

//    iosX64 {
//        binaries.framework {
//            baseName = xcfName
//            isStatic = true
//            linkerOpts("-framework", "FirebaseCore")
//            linkerOpts("-framework", "FirebaseStorage")
//            // Suppress the duplicate libraries warning
//            linkerOpts("-Xlinker", "-no_warn_duplicate_libraries")
//
//        }
//    }



    iosArm64 {
        binaries.framework {
            baseName = xcfName
            isStatic = true
            linkerOpts("-framework", "FirebaseCore")
            linkerOpts("-framework", "FirebaseStorage")
            linkerOpts("-Xlinker", "-no_warn_duplicate_libraries")
            linkerOpts("-lsqlite3")
        }
    }

    iosSimulatorArm64 {
        binaries.framework {
            baseName = xcfName
            isStatic = true
            linkerOpts("-framework", "FirebaseCore")
            linkerOpts("-framework", "FirebaseStorage")
            // Suppress the duplicate libraries warning
            linkerOpts("-Xlinker", "-no_warn_duplicate_libraries")
            linkerOpts("-lsqlite3")

        }
    }

    swiftPMDependencies {
        // Import FirebaseAnalytics into your Kotlin code
        swiftPackage(
            url = url("https://github.com/firebase/firebase-ios-sdk.git"),
            version = from("11.3.0"),
            products = listOf(product("FirebaseStorage")),
        )
        // swift-protobuf is a transitive Firebase dependency,
        // so you only need to include it
        // if you want to use a specific version
        swiftPackage(
            url = url("https://github.com/apple/swift-protobuf.git"),
            version = exact("1.37.0"),
            products = listOf(),
        )
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
                api(libs.androidx.lifecycle.viewmodel)
                api(libs.koin.core)
                implementation(libs.compose.runtime)
                implementation(libs.compose.foundation)
                implementation(libs.compose.material3)
                implementation(compose.materialIconsExtended)
                implementation(libs.compose.ui)
                implementation(libs.compose.components.resources)
                implementation(libs.compose.uiToolingPreview)
                implementation(libs.androidx.lifecycle.viewmodelCompose)
                implementation(libs.androidx.lifecycle.runtimeCompose)
                api(libs.kermit)
                api(libs.koin.core)
                implementation(libs.koin.compose)
                implementation(libs.koin.compose.viewmodel)
                implementation(libs.navigation.compose)

                implementation(libs.androidx.room.runtime)
                implementation(libs.kotlinx.datetime)
                implementation("io.github.vinceglb:filekit-core:0.12.0")
                implementation("com.squareup.okio:okio:3.18.1")

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
//                implementation("dev.gitlive:firebase-storage:2.4.0")
//                implementation("dev.gitlive:firebase-analytics:2.4.0")
                api(libs.koin.android)
                implementation(libs.androidx.lifecycle.service)
                implementation(libs.androidx.room.sqlite.wrapper)
                implementation("com.google.firebase:firebase-storage:19.1.1")
//                implementation ("com.google.firebase:firebase-crashlytics:17.2.2")//TODO
                implementation("com.google.firebase:firebase-analytics:17.6.0")
                implementation(libs.compose.uiToolingPreview)
                implementation(libs.compose.uiTooling)

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
                /*
                RESOLVES Undefined symbols for architecture arm64:
              "_sqlite3_load_extension", referenced from:
                _sqlite3_sqlite3_load_extension_wrapper203 in sharedKit[16](libandroidx.sqlite:sqlite-framework-cinterop-sqlite3-cache.a.o)
                ld: symbol(s) not found for architecture arm64
                clang: error: linker command failed with exit code 1 (use -v to see invocation)
                 */
                implementation("androidx.sqlite:sqlite-framework:${libs.versions.sqlite.get()}")
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
//    add("kspIosX64", libs.androidx.room.compiler)
    add("kspIosArm64", libs.androidx.room.compiler)
}

