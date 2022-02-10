package ru.hse.shallow

import arrow.meta.CliPlugin
import arrow.meta.Meta
import arrow.meta.invoke
import arrow.meta.quotes.Transform
import arrow.meta.quotes.classDeclaration

val Meta.addShallowSizeMethod: CliPlugin
    get() = "Add shallowSize method to data classes" {
        meta(
            classDeclaration(this, { element.isData() }) {
                val superTypes = if (supertypes.isEmpty()) "" else ": $supertypes"
                Transform.replace(
                    it.element,
                    """
                        $`@annotations` $visibility $kind $name $`(typeParameters)` $`(params)` $superTypes {
                        $body
                            fun shallowSize() = 10
                        }
                    """.trimIndent().`class`
                )
            }
        )
    }
