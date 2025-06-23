plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("plugin.serialization") version "1.9.0"
}

android {
    namespace = "com.example.booking"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.booking"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
    implementation ("io.ktor:ktor-client-core:2.3.5")
    implementation ("io.ktor:ktor-client-android:2.3.5")
    implementation ("io.ktor:ktor-client-content-negotiation:2.3.5")
    implementation ("io.ktor:ktor-serialization-kotlinx-json:2.3.5")
    implementation ("io.ktor:ktor-client-logging:2.3.5")
    implementation("androidx.compose.ui:ui:1.5.0") // Обновлено
    implementation("androidx.compose.material3:material3:1.1.0") // Обновлено
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1") // Обновлено
    implementation("androidx.activity:activity-compose:1.7.0") // Обновлено
    implementation("androidx.navigation:navigation-compose:2.7.0") // Обновлено
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}