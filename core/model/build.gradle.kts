
plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
}

kotlin {
    jvmToolchain(17)
}
dependencies {
    compileOnly(libs.androidx.annotation)
}
