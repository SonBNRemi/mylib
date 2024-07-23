plugins {
    id("com.android.library")
    id("maven-publish")
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.sonbn.remi.mylib"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

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
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

}

publishing {
    publications {
        register<MavenPublication>("release") {
            afterEvaluate {
                from(components["release"])

            }

            groupId = "com.sonbn.remi.mylib"
            artifactId = "mylib"
            version = "1.0.0"

            pom {
                licenses {
                    license {
                        name = "The Apache License, Version 2.0"
                        url = "http://www.apache.org/licenses/LICENSE-2.0.txt"
                    }
                }
            }
        }
    }
}
dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.process)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    api("com.google.android.gms:play-services-ads:23.0.0")
    api("com.facebook.shimmer:shimmer:0.5.0@aar")
    api("com.google.android.ump:user-messaging-platform:2.2.0")
    api("androidx.preference:preference-ktx:1.1.1")

    val billing_version = "7.0.0"
    api("com.android.billingclient:billing-ktx:$billing_version")
}