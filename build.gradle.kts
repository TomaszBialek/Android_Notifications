// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    alias(libs.plugins.com.google.gms.google.services) apply false
    alias(libs.plugins.com.google.dagger.hilt.android) apply false
    alias(libs.plugins.com.google.devtools.ksp) apply false
}

buildscript {
    dependencies {
        // other plugins...
        classpath(libs.hilt.android.gradle.plugin)
    }
}