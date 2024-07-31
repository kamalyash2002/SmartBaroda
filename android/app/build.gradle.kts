plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("com.google.dagger.hilt.android")
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.uphar.smartbaroda"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.uphar.smartbaroda"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.androidx.cardview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.appcompat.resources)
    implementation(libs.androidx.camera.view)
    implementation(libs.firebase.firestore.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation("androidx.compose.material:material:1.6.8")
    implementation("com.patrykandpatrick.vico:core:1.15.0")
    implementation("com.patrykandpatrick.vico:compose:1.15.0")
    implementation("androidx.navigation:navigation-compose:2.5.3")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")


    implementation("androidx.compose.material3:material3:1.1.0-alpha05")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")
    implementation("androidx.compose.ui:ui:1.3.3")
    implementation("androidx.compose.material:material-icons-extended:1.3.1")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")

    implementation("androidx.datastore:datastore-core:1.0.0")
    implementation("androidx.datastore:datastore-preferences-core-jvm:1.1.0-beta02")
    implementation("com.android.volley:volley:1.2.1")

    val nav_version = "2.7.5"

    implementation("androidx.compose.foundation:foundation:1.5.4")

    implementation("androidx.compose.material:material-icons-core:1.0.5")
    implementation("androidx.compose.material:material-icons-extended:1.0.5")

    implementation("io.coil-kt:coil-compose:2.5.0")
    implementation("io.coil-kt:coil-gif:2.1.0")

    // Jetpack Compose
    implementation("androidx.navigation:navigation-compose:$nav_version")

    implementation("androidx.compose.material:material:1.0.1")

    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.0")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    implementation("androidx.compose.ui:ui:1.6.2")
    implementation("androidx.compose.material:material:1.6.2")
    implementation("androidx.compose.ui:ui-tooling:1.6.2")
    implementation("androidx.compose.foundation:foundation:1.6.2")
    implementation("androidx.compose.runtime:runtime-livedata:1.6.2")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation("androidx.compose.runtime:runtime:1.6.2")
    implementation("androidx.compose.material:material:1.6.2")
    implementation("androidx.compose.material:material-icons-core:1.6.2")
    implementation("androidx.compose.material:material-icons-extended:1.6.2")
    implementation("androidx.compose.ui:ui-android:composeVersion")
    implementation("androidx.core:core-ktx:ktxVersion")


    //webrtc
//    implementation ("org.java-websocket:Java-WebSocket:1.5.3")
    implementation("com.google.code.gson:gson:2.9.1")
    implementation("com.mesibo.api:webrtc:1.0.5")
    implementation("io.socket:socket.io-client:2.0.1")
    implementation("com.google.code.gson:gson:2.8.6")


    //coil
    implementation("io.coil-kt:coil-compose:2.1.0")

    //token
    implementation("com.auth0.android:jwtdecode:2.0.0")


    //dagger
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-compiler:2.48")
    kapt("com.google.dagger:hilt-android-compiler:2.48")


    //Lottie
    implementation("com.airbnb.android:lottie-compose:6.4.0")
    //GSOn
    implementation("com.google.code.gson:gson:2.8.9")


    // DataStore
    var dataStoreVersion = "1.0.0"
    implementation("androidx.datastore:datastore-preferences:$dataStoreVersion")

    // Retrofit
    var retrofit = "2.9.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofit")
    implementation("com.squareup.retrofit2:converter-gson:$retrofit")


    implementation ("com.google.accompanist:accompanist-permissions:0.28.0")

    //emoji
    implementation("com.github.Abhimanyu14:compose-emoji-picker:1.0.0-alpha16")

    val cameraxVersion = "1.0.0-beta07"
    val cameraxViewVersion = "1.0.0-alpha14"
    val cameraxCoreVersion = "1.0.0-alpha12"
    val mlKitFaceDetectionVersion = "16.1.5"
    implementation("androidx.camera:camera-camera2:$cameraxVersion")
    implementation("androidx.camera:camera-lifecycle:$cameraxVersion")
    implementation("androidx.camera:camera-view:$cameraxViewVersion")
    implementation("androidx.camera:camera-core:$cameraxCoreVersion")
    implementation("com.google.mlkit:face-detection:$mlKitFaceDetectionVersion")
    implementation("com.google.mlkit:text-recognition:16.0.0")
    implementation("androidx.appcompat:appcompat:1.5.1")
    implementation("com.google.guava:guava:31.0.1-android")
    implementation("org.json:json:20210307")

}