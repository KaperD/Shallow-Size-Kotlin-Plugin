package ru.hse.shallow

import arrow.meta.CliPlugin
import arrow.meta.Meta
import arrow.meta.invoke
import arrow.meta.quotes.Transform
import arrow.meta.quotes.classDeclaration

val Meta.addShallowSizeMethod: CliPlugin
    get() = "Transform New Multiple Source" {
        meta(
            classDeclaration(this, { element.name == "NewMultipleSource" }) {
                Transform.newSources(
                    """
          |package arrow
          |         
          |class ${name}_Generated
          """.trimMargin().file("${name}_Generated"), // default path: generated/source/kapt/main
                    """
          |package arrow
          |
          |class ${name}_Generated_2
          """.trimMargin().file("${name}_Generated_2", "generated/custom/directory")
                )
            }
        )
    }
