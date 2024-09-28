plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "ru.nsu.musicplayer"
    compileSdk = 34

    defaultConfig {
        applicationId = "ru.nsu.musicplayer"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Базовые зависимости для Kotlin и Android
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")

    // Material Design компоненты
    implementation("com.google.android.material:material:1.9.0")

    // ConstraintLayout для создания адаптивных макетов
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // RecyclerView для отображения списков
    implementation("androidx.recyclerview:recyclerview:1.3.1")

    // Жизненный цикл Activity и ViewModel
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-ktx:1.7.2")

    // Поддержка векторных изображений
    implementation("androidx.vectordrawable:vectordrawable:1.1.0")

    // Для работы с макетами и их предварительного просмотра
    implementation("androidx.annotation:annotation:1.6.0")

    // Тестовые зависимости (опционально)
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
