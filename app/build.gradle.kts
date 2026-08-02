import org.gradle.kotlin.dsl.implementation

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.composeCompiler)
    id("com.google.gms.google-services")
}

//kotlin {
//    compilerOptions {
//        jvmTarget = JvmTarget.JVM_11
//    }
//}



android {
    namespace = "com.devlomi.ayaturabbi"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.devlomi.ayaturabbi"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    buildFeatures{
        viewBinding = true
        buildConfig = true
        compose = true
    }

    kotlin {
        jvmToolchain(21)
    }
}

//apply plugin: 'com.android.application'
//apply plugin: 'kotlin-android'
//apply plugin: 'dagger.hilt.android.plugin'
//apply plugin: 'kotlin-kapt'
//apply plugin: 'com.google.gms.google-services'
//apply plugin: 'com.google.firebase.crashlytics'

//android {
//    compileSdkVersion 34
//
//
//    defaultConfig {
//        applicationId "com.devlomi.ayaturabbi"
//        minSdkVersion 21
//        targetSdkVersion 34
//        versionCode 6
//        versionName "1.0.3"
//
//        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
//    }
//    buildFeatures {
//        viewBinding true
//    }
//
//    buildTypes {
//        release {
//            minifyEnabled true
//            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
//        }
//    }
//    compileOptions {
//        sourceCompatibility JavaVersion.VERSION_17
//        targetCompatibility JavaVersion.VERSION_17
//    }
//    kotlinOptions {
//        jvmTarget = JavaVersion.VERSION_17.toString()
//    }
//    namespace 'com.devlomi.ayaturabbi'
//}



dependencies {
    implementation(project(":shared"))
    implementation ("androidx.legacy:legacy-support-v4:1.0.0")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.service)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.activity.compose)

    implementation ("com.google.firebase:firebase-storage:19.1.1")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.3.1")

//
//    def room_version = "2.6.1"
    val nav_version = "2.3.1"
//
//
//    implementation ("org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version")
    implementation ("androidx.core:core-ktx:1.19.0")
//    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")
    implementation ("androidx.appcompat:appcompat:1.7.1")
    implementation ("com.google.android.material:material:1.3.0-alpha03")

    implementation ("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation ("androidx.navigation:navigation-ui-ktx:$nav_version")

    implementation ("me.zhanghai.android.systemuihelper:library:1.0.0")
//    implementation ("com.github.warkiz.tickseekbar:tickseekbar:0.1.4")
    implementation ("com.github.3llomi:TickSeekBar:0.1.4-jitpack")

    implementation ("androidx.constraintlayout:constraintlayout:2.0.2")

//    implementation ("androidx.lifecycle:lifecycle-extensions:2.10")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.10")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.10")

    implementation ("androidx.fragment:fragment-ktx:1.8.9")

    implementation ("com.afollestad.material-dialogs:core:3.3.0")
    implementation ("com.afollestad.material-dialogs:bottomsheets:3.3.0")
    implementation ("com.afollestad.material-dialogs:lifecycle:3.2.1")



//    def hilt_version = "2.48"
//    implementation ("com.google.dagger:hilt-android:$hilt_version")
//    kapt "com.google.dagger:hilt-android-compiler:$hilt_version"

//    def hilt_compiler = "1.2.0"
//
//    kapt "androidx.hilt:hilt-compiler:$hilt_compiler"


//
//    implementation ("androidx.room:room-runtime:$room_version")
//    implementation ("androidx.room:room-ktx:$room_version")
//    kapt "androidx.room:room-compiler:$room_version"





    implementation ("com.github.bumptech.glide:glide:4.11.0")
//    kapt 'com.github.bumptech.glide:compiler:4.11.0'




//    testImplementation 'junit:junit:4.12'
//    androidTestImplementation 'androidx.test.ext:junit:1.1.1'
//    androidTestImplementation 'androidx.test.espresso:espresso-core:3.3.0'

}
