import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    explicitApi = ExplicitApiMode.Strict
}


dependencies {
    compileOnly(libs.androidx.annotation)
}
