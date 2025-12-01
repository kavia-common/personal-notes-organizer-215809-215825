androidApplication {
    namespace = "org.example.app"

    dependencies {
        // Core AndroidX + Material
        implementation("androidx.core:core-ktx:1.13.1")
        implementation("androidx.appcompat:appcompat:1.7.0")
        implementation("com.google.android.material:material:1.12.0")
        implementation("androidx.recyclerview:recyclerview:1.3.2")
        implementation("androidx.constraintlayout:constraintlayout:2.2.0")

        // Lifecycle + ViewModel
        implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.6")
        implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.6")
        implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")

        // Activity KTX for viewModels() delegate
        implementation("androidx.activity:activity-ktx:1.9.3")

        // Coroutines
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")
    }
}
