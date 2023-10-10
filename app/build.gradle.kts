plugins {
    id("com.android.application")
}

android {
    namespace = "com.gcs.appantiroboandroid"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.gcs.appantiroboandroid"
        minSdk = 31
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        packaging {
            resources {
                excludes += "/META-INF/DEPENDENCIES"
                excludes += "/META-INF/NOTICE.md"
                excludes += "/META-INF/LICENSE.md"
            }
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
}

dependencies {

    val fragment_version = "1.5.5"

    // Java language implementation
    implementation("androidx.fragment:fragment:$fragment_version")
    implementation("androidx.fragment:fragment-ktx:$fragment_version")
    debugImplementation("androidx.fragment:fragment-testing:$fragment_version")

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    implementation ("com.google.android.gms:play-services-auth:20.7.0")
    implementation ("com.google.api-client:google-api-client:2.2.0")
    implementation ("com.google.oauth-client:google-oauth-client-jetty:1.23.0")
    implementation ("com.google.apis:google-api-services-gmail:v1-rev83-1.23.0")

    implementation ("com.google.auth:google-auth-library-oauth2-http:1.11.0")
    implementation ("javax.mail:mail:1.4.7")

    implementation ("com.j256.ormlite:ormlite-android:5.1")
    implementation ("com.j256.ormlite:ormlite-core:5.1")
    implementation("androidx.preference:preference:1.2.1")
    implementation("org.projectlombok:lombok:1.18.26")


    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}