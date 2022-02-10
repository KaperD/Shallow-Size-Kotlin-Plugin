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

val kotlinVersion: String by project
val arrowMetaVersion: String by project
val jvmTargetVersion: String by project

plugins {
    id("org.jetbrains.kotlin.jvm")
}

dependencies {
    compileOnly("org.jetbrains.kotlin:kotlin-compiler-embeddable:$kotlinVersion")
    compileOnly("io.arrow-kt:arrow-meta:$arrowMetaVersion")
}

tasks.compileKotlin {
    kotlinOptions {
        jvmTarget = jvmTargetVersion
    }
}

tasks.jar {
    from(
        sourceSets.main.get().compileClasspath.find {
            it.absolutePath.contains(Paths.get("arrow-kt", "arrow-meta").toString())
        }?.let { zipTree(it) }
    )
}
