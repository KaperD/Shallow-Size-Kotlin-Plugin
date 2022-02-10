import java.nio.file.Paths

/*
 * Copyright (C) 2020 The Arrow Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

plugins {
    id("org.jetbrains.kotlin.jvm")
}

dependencies {
    compileOnly("org.jetbrains.kotlin:kotlin-compiler-embeddable:1.5.0")
    compileOnly("io.arrow-kt:arrow-meta:1.5.0-SNAPSHOT")
}

tasks.compileKotlin {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

tasks.jar {
    from(
        sourceSets.main.get().compileClasspath.find {
            it.absolutePath.contains(Paths.get("arrow-kt", "arrow-meta").toString())
        }?.let { zipTree(it) }
    )
}
