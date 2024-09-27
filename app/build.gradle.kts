plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.org.jetbrains.kotlin.kapt)
//    alias(libs.plugins.kotlinAndroidKsp)
    alias(libs.plugins.hiltAndroid)
    alias(libs.plugins.compose.compiler)

    kotlin("plugin.serialization")
}

android {
    namespace = "com.example.imageapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.imageapp"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        multiDexEnabled = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
//        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
//        kotlinCompilerExtensionVersion = "1.5.1"
        kotlinCompilerExtensionVersion = "1.5.13"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

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
    implementation(libs.kotlinx.serialization.json)
    implementation (libs.androidx.lifecycle.viewmodel.compose)


    // Coil Compose
    implementation( libs.coil.compose)

// Dagger - Hilt
    implementation(libs.hilt.android)
    kapt(libs.dagger.hilt.android.compiler)
    kapt (libs.androidx.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

// Paging
    implementation(libs.androidx.paging.runtime.ktx)
    implementation(libs.androidx.paging.compose)

// Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.moshi)
    implementation(libs.logging.interceptor)

// Room
    implementation(libs.androidx.room.ktx)
    annotationProcessor(libs.androidx.room.compiler)
    implementation(libs.androidx.room.paging)

//    implementation (libs.androidx.multidex)


}