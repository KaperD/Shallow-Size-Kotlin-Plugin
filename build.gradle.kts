plugins {
    id("org.jetbrains.kotlin.jvm") version "1.5.0" apply false
}

group = "ru.hse"
version = "1.0-SNAPSHOT"

allprojects {
    repositories {
        mavenCentral()
        maven {
            url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
        }
    }
}
