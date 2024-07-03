@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id(libs.plugins.androidApplication.get().pluginId) apply false
    id(libs.plugins.kotlinAndroid.get().pluginId) apply false
    id(libs.plugins.androidLibrary.get().pluginId) apply false
    alias(libs.plugins.kotlinKsp) apply false
}
true // Needed to make the Suppress annotation work for the plugins block
