plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}
android {
    namespace = "com.cip.conwaylife3d"
    compileSdk = 35
    defaultConfig { applicationId = "com.cip.conwaylife3d"; minSdk = 26; targetSdk = 35; versionCode = 1; versionName = "0.1.0" }
    compileOptions { sourceCompatibility = JavaVersion.VERSION_17; targetCompatibility = JavaVersion.VERSION_17 }
    kotlinOptions { jvmTarget = "17" }
    buildFeatures { compose = true }
}
dependencies {
    implementation(platform("androidx.compose:compose-bom:2025.05.01"))
    implementation("androidx.activity:activity-compose:1.10.1")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")
    testImplementation("junit:junit:4.13.2")
}


// Only the dedicated reference-test CI job supplies these paths.
tasks.withType<Test>().configureEach {
    val input = System.getProperty("rendell.input")
    val reference = System.getProperty("rendell.reference")
    if (input != null) systemProperty("rendell.input", input)
    if (reference != null) systemProperty("rendell.reference", reference)
    if (input == null || reference == null) {
        exclude("**/RendellReferenceTest.class")
    }
}
