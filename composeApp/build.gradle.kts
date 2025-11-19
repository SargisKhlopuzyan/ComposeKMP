@file:OptIn(ExperimentalComposeLibrary::class, ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }

        instrumentedTestVariant {

            sourceSetTree.set(KotlinSourceSetTree.test)

//            dependencies {
//                implementation(libs.core.ktx)
//                implementation(libs.compose.ui.test.junit4.android)
//                debugImplementation(libs.compose.ui.test.manifest)
//            }
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            // koin
            api(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.lifecycle.viewmodel)
            implementation(libs.navigation.compose)

            // ktor
            implementation(libs.bundles.ktor)

            // datastore
            // api allow a dependence to be used in modules which extended from this module (ivm, android, native)
            api(libs.datastore)
            api(libs.datastore.preferences)

            // already exist
//            implementation(libs.lifecycle.viewmodel)
//            implementation(libs.androidx.lifecycle.viewmodelCompose)
            // moko-permission
            api(libs.moko.permissions)
            api(libs.moko.permissions.compose)

            implementation(libs.kotlinx.datetime)
        }
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)

            // koin
            implementation(libs.koin.android)
            implementation(libs.koin.androidx.compose)
//            debugImplementation(libs.compose.ui.test.manifest)

            // ktor
            implementation(libs.ktor.client.okhttp)

            // splashscreen
            implementation(libs.core.splashscreen)

        }
        nativeMain.dependencies {
            // ktor
            implementation(libs.ktor.client.darwin)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
            implementation(libs.oshi.core)

            // ktor
            implementation(libs.ktor.client.okhttp)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(kotlin("test-annotations-common"))

            implementation(libs.assertK)
            implementation(compose.uiTest)

        }

        androidInstrumentedTest.dependencies {
            implementation(libs.core.ktx)
            implementation(libs.compose.ui.test.junit4.android)
//            debugImplementation(libs.compose.ui.test.manifest)
        }
    }
}

android {
    namespace = "com.sargis.composekmp"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.sargis.composekmp"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.junit.jupiter)
    debugImplementation(compose.uiTooling)

    implementation(libs.core.ktx)
    implementation(libs.compose.ui.test.junit4.android)
    implementation(libs.compose.ui.test.manifest)
    debugImplementation(libs.compose.ui.test.manifest)
}

compose.desktop {
    application {
        mainClass = "com.sargis.composekmp.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.sargis.composekmp"
            packageVersion = "1.0.0"
        }
    }
}
