# Shallow Size Kotlin Compiler Plugin

Plugin adds shallowSize(): Int method to all data classes at compile time

This method returns sum of all data class fields sizes in bytes

Pointer size is considered as 4 bytes, Boolean size as 1 byte, other primitive types have SIZE_BYTES field in their companion object

## Using with Gradle

### Requirements
- JDK 8
- Kotlin 1.5.0

### Steps
1. Get plugin shallow-size-plugin.jar file
   - Download it from GitHub Releases
   - Or clone this repo, run `./gradlew new-plugin:jar` and get jar from new-plugin/build/libs/
2. Add this to your build.gradle.kts:
```kotlin
tasks.withType<KotlinCompile> {
    kotlinOptions.freeCompilerArgs += "-Xplugin=${rootDir}/path/to/shallow-size-plugin.jar"
}
```
Or if you use build.gradle:
```groovy
compileKotlin {
    kotlinOptions {
        freeCompilerArgs += ["-Xplugin=${rootDir}/path/to/shallow-size-plugin.jar"]
    }
}
```

## Example of plugin usage with gradle
You can see it in use-plugin folder. It also contains some plugin tests

## Example of plugin work
```kotlin
open class Base(var a: Short)

data class A(val b: Int) : Base(42) {
    val c: Int // don't have field
        get() = 1
}

fun main() {
    val a = A(3)
    println(a.shallowSize()) // 6
}
```



