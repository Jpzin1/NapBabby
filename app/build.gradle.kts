plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.napbabby"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.napbabby"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    buildToolsVersion = "35.0.0"
    ndkVersion = "28.0.13004108"
    // buildFeatures {
    //     viewBinding = true
    // }
    lint {
        abortOnError = false
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.auth)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    // Google Sign-In
    implementation("com.google.android.gms:play-services-auth:20.7.0")

    //GOOGLE AUTH
    // Import the BoM for the Firebase platform
    implementation(platform("com.google.firebase:firebase-bom:33.14.0"))

    // Add the dependency for the Firebase Authentication library
    // When using the BoM, you don\"t specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-auth")


}
