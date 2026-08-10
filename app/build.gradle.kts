
plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.gcwcampus"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.gcwcampus"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    //firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    //firebase-authentication
    implementation(libs.firebase.auth)
    //room library
    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler)
    //firebase-realtime-database
    implementation(libs.firebase.database)
    //glide
    implementation (libs.glide.v4151)
    annotationProcessor (libs.compiler.v4151)
    //push notification
    implementation (libs.firebase.messaging)
    implementation (libs.firebase.messaging.directboot)
    //retrofit & gson
    implementation (libs.retrofit)
    implementation (libs.converter.gson)
    //volley
    implementation (libs.volley)
    //okHttp
    implementation (libs.okhttp)
}