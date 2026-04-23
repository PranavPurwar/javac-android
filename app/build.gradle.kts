plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "dev.pranav.javacompiler"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "dev.pranav.javacompiler"
        minSdk = 26
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    packaging {
        resources.excludes += "README.md"
        resources.excludes += "SECURITY.md"
    }
}

dependencies {
    val javaCompilerVersion: String by project

    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)

    implementation("com.github.Cosmic-IDE.kotlinc-android:jaxp:fce2462f00")
    implementation(files("../build/patchedJavaCompilerJar/nb-javac-$javaCompilerVersion-patched.jar"))
}
