plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "ru.mactiva.castumritma"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "ru.mactiva.castumritma"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        androidResources {
            localeFilters += listOf("en", "ru")
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            isDebuggable = true
            isMinifyEnabled = false
            // Добавляем логирование для отладки сети
            buildConfigField("boolean", "LOG_ENABLED", "true")
        }

        release {
            isMinifyEnabled = true // Включаем R8 для оптимизации и защиты
            isShrinkResources = true // Удаляем неиспользуемые ресурсы
            isDebuggable = false
            buildConfigField("boolean", "LOG_ENABLED", "false")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        // Включаем поддержку новых Java библиотек на старых Android
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        compose = true
        buildConfig = true // Позволяет использовать buildConfigField
    }
}

dependencies {
    // ОБЯЗАТЕЛЬНО для поддержки дат на старых Android (Desugaring)
    coreLibraryDesugaring(libs.desugar.jdk.libs)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    // --- ТЕСТЫ ---
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    // Сеть (iTunes API)
    implementation(libs.retrofit.main)
    implementation(libs.retrofit.gson)
    implementation(libs.okhttp.logging)

    // БД (Offline mode)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    // Важно: для KSP/KAPT (нужен плагин, но пока добавим так)
    annotationProcessor(libs.room.compiler)

    // Плеер (Media3)
    implementation(libs.media3.exoplayer)
    implementation(libs.media3.ui)
    implementation(libs.media3.session)

    // Изображения (Coil)
    implementation(libs.coil.compose)

    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.coil.compose)

    implementation(libs.androidx.navigation.compose)

    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.androidx.navigation)

//    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material.icons.extended)
}