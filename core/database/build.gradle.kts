import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.androidx.room)
}

android {
    namespace = "com.ainsln.core.database"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "com.ainsln.core.testing.CustomTestRunner"
        consumerProguardFiles("consumer-rules.pro")

        room {
            schemaDirectory("$projectDir/schemas")
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
    kotlin {
        jvmToolchain(17)
    }
    kotlin {
        explicitApi = ExplicitApiMode.Strict
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)

    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    implementation(libs.hilt.android)
    compileOnly(libs.hilt.android.testing)
    ksp(libs.hilt.android.compiler)

    androidTestImplementation(projects.core.testing)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.hilt.android.testing)
    androidTestImplementation (libs.androidx.core.testing)
    androidTestImplementation (libs.truth)
}
