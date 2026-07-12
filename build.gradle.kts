import org.gradle.api.internal.initialization.ClassLoaderIds.buildScript

//// Top-level build file where you can add configuration options common to all sub-projects/modules.
//buildscript {
//    ext.kotlin_version = "2.2.0"
//    repositories {
//        google()
////        jcenter()
//        mavenCentral()
//    }
//    dependencies {
////        classpath 'com.google.gms:google-services:4.3.15'
////        classpath 'com.google.firebase:firebase-crashlytics-gradle:2.9.8'
//        def hilt_version = '2.48'
//
//        classpath 'com.android.tools.build:gradle:8.8.0'
//        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
////        classpath "com.google.dagger:hilt-android-gradle-plugin:$hilt_version"
//
//
//        // NOTE: Do not place your application dependencies here; they belong
//        // in the individual module build.gradle files
//    }
//}
//
//allprojects {
//    repositories {
//        google()
////        jcenter()
//        maven{ url 'https://jitpack.io' }
//        mavenCentral()
//    }
//}
//
//task clean(type: Delete) {
//    delete rootProject.buildDir
//}

plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidMultiplatformLibrary) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.kotlin.android) apply false
}
buildscript{
    dependencies{
        classpath ("com.google.gms:google-services:4.3.15")
    }
}


//room {
//    schemaDirectory("$projectDir/schemas")
//}
