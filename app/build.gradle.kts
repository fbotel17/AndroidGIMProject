plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.gim"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.gim"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation("com.squareup.retrofit2:retrofit:2.9.0") // Mise à jour avec la syntaxe correcte
    implementation("com.squareup.retrofit2:converter-gson:2.9.0") // idem
    implementation("com.squareup.okhttp3:okhttp:4.9.0") // idem
    implementation ("com.journeyapps:zxing-android-embedded:4.3.0")
    implementation ("com.google.zxing:core:3.4.0")
    implementation ("com.squareup.okhttp3:okhttp:4.9.0")
    implementation ("com.squareup.okhttp3:okhttp:4.9.3")
    implementation ("androidx.recyclerview:recyclerview:1.2.1")
    implementation ("androidx.cardview:cardview:1.0.0")



    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
