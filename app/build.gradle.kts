import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "io.github.sekademi.pipedata"
    compileSdk = 35
    defaultConfig {
        applicationId = "io.github.sekademi.pipedata"
        minSdk = 24
        targetSdk = 35
        versionCode = 201
        versionName = "2.0.1"
    }
    val localProperties = Properties()
    val localPropsFile = rootProject.file("local.properties")
    if (localPropsFile.exists()) {
        localPropsFile.inputStream().use { stream ->
            localProperties.load(stream)
        }
    }

    val releaseStoreFilePath = (project.findProperty("MYAPP_RELEASE_STORE_FILE") as? String)
        ?: localProperties.getProperty("MYAPP_RELEASE_STORE_FILE")
    val releaseStorePassword = (project.findProperty("MYAPP_RELEASE_STORE_PASSWORD") as? String)
        ?: localProperties.getProperty("MYAPP_RELEASE_STORE_PASSWORD")
    val releaseKeyAlias = (project.findProperty("MYAPP_RELEASE_KEY_ALIAS") as? String)
        ?: localProperties.getProperty("MYAPP_RELEASE_KEY_ALIAS")
    val releaseKeyPassword = (project.findProperty("MYAPP_RELEASE_KEY_PASSWORD") as? String)
        ?: localProperties.getProperty("MYAPP_RELEASE_KEY_PASSWORD")

    signingConfigs {
        create("release") {
            val customStoreFile = releaseStoreFilePath?.let { path ->
                val f = file(path)
                if (f.exists()) f else rootProject.file(path)
            }
            if (customStoreFile != null && customStoreFile.exists() && !releaseKeyAlias.isNullOrBlank()) {
                storeFile = customStoreFile
                storePassword = releaseStorePassword
                keyAlias = releaseKeyAlias
                keyPassword = releaseKeyPassword
            } else {
                storeFile = file("antigravity.keystore")
                storePassword = "antigravity_android_key"
                keyAlias = "antigravity"
                keyPassword = "antigravity_android_key"
            }
        }
    }
    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        debug {
            signingConfig = signingConfigs.getByName("release")
            isDebuggable = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
    buildFeatures { compose = true }
}

dependencies {
    testImplementation("junit:junit:4.13.2")
    implementation(platform("androidx.compose:compose-bom:2024.09.00"))
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.activity:activity-compose:1.9.2")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    debugImplementation("androidx.compose.ui:ui-tooling")
}
