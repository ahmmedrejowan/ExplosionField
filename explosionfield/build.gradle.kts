plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("maven-publish")
}

android {
    namespace = "com.rejowan.explosionfield"
    compileSdk = 36

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

// Maven publishing configuration for JitPack
afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])

                groupId = "com.github.ahmmedrejowan"
                artifactId = "explosionfield"
                version = "1.0.0"

                pom {
                    name.set("ExplosionField")
                    description.set("A powerful and customizable Android explosion animation library with multiple explosion styles")
                    url.set("https://github.com/ahmmedrejowan/ExplosionField")

                    licenses {
                        license {
                            name.set("The Apache License, Version 2.0")
                            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        }
                    }

                    developers {
                        developer {
                            id.set("ahmmedrejowan")
                            name.set("K M Rejowan Ahmmed")
                            email.set("kmrejowan@gmail.com")
                        }
                    }

                    scm {
                        connection.set("scm:git:git://github.com/ahmmedrejowan/ExplosionField.git")
                        developerConnection.set("scm:git:ssh://github.com/ahmmedrejowan/ExplosionField.git")
                        url.set("https://github.com/ahmmedrejowan/ExplosionField")
                    }
                }
            }
        }
    }
}