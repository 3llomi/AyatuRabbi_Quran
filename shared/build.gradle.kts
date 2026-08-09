plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("com.android.kotlin.multiplatform.library")
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    id("com.android.lint")
    alias(libs.plugins.ksp)
    alias(libs.plugins.androidx.room) apply false
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


    }

    val xcfName = "Shared"


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
                api(libs.koin.android)
                implementation(libs.androidx.lifecycle.service)
                implementation(libs.androidx.room.sqlite.wrapper)
                implementation("com.google.firebase:firebase-storage:19.1.1")
                implementation(libs.compose.uiToolingPreview)
                implementation(libs.compose.uiTooling)
            }
        }


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
//                implementation("androidx.sqlite:sqlite-framework:${libs.versions.sqlite.get()}")
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

