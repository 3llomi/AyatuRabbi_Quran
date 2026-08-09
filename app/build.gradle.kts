import org.gradle.kotlin.dsl.implementation

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.composeCompiler)
    id("com.google.gms.google-services")
}


android {
    namespace = "com.devlomi.ayaturabbi"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.devlomi.ayaturabbi"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 8
        versionName = "1.0.5"
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




dependencies {
    implementation(project(":shared"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.service)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.activity.compose)

    implementation ("com.google.firebase:firebase-storage:19.1.1")

    implementation ("androidx.core:core-ktx:1.19.0")
    implementation ("androidx.appcompat:appcompat:1.7.1")
    implementation ("me.zhanghai.android.systemuihelper:library:1.0.0")

}
