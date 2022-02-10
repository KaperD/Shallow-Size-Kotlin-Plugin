plugins {
    id("org.jetbrains.kotlin.jvm") version "1.5.0" apply false
}

allprojects {
    repositories {
        mavenCentral()
        maven {
            url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
        }
    }
}
