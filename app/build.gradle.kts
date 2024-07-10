plugins {
    id(libs.plugins.androidApplication.get().pluginId)
    id(libs.plugins.kotlinAndroid.get().pluginId)
    id("kotlin-parcelize")
    alias(libs.plugins.compose.compiler)
}


android {
    namespace = "pan.lib.baseandroidframework"
    compileSdk = libs.versions.compileSdkVersion.get().toInt()

    defaultConfig {
        applicationId = "pan.lib.android_learning"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        ndk {
            abiFilters.add("arm64-v8a")
        }
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    sourceSets {
        getByName("main") {
            java.srcDirs("src/main/java", "src/main/aidl")
        }
    }

    signingConfigs {
        create("jks") {
            storeFile = file("key.jks")
            storePassword = "yesterday you said tomorrow"
            keyAlias = "key"
            keyPassword = "yesterday you said tomorrow"
        }
    }

    buildTypes {
        release {
            initWith(buildTypes.getByName("debug"))  //复用debug配置
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        debug {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("jks")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        aidl = true
        viewBinding = true
        buildConfig = true
        compose = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(libs.baseAndroidFramework)
    debugImplementation(libs.leakcanaryAndroid)
    implementation(libs.rxjava)
    implementation(libs.rxandroid)


    //compose
    implementation(libs.lifecycleRuntimeKtx)
    implementation(libs.activityCompose)
    implementation(platform(libs.composeBom))
    implementation(libs.ui)
    implementation(libs.uiGraphics)
    implementation(libs.uiToolingPreview)
    implementation(libs.material3)
    androidTestImplementation(platform(libs.composeBom))
    androidTestImplementation(libs.uiTestJunit4)
    debugImplementation(libs.uiTooling)
    debugImplementation(libs.uiTestManifest)
    implementation(libs.lifecycleViewmodelCompose)

    //paging
    implementation(libs.pagingRuntime)
    implementation(libs.pagingCompose)

}

tasks.register<Delete>("cleanTest") {
    println("the path is = " + rootProject.file("deleteFileTest.txt"))
    delete(rootProject.file("deleteFileTest.txt"))
}
