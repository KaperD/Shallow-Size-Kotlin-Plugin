package ru.hse.shallow

import arrow.meta.CliPlugin
import arrow.meta.Meta
import arrow.meta.invoke
import arrow.meta.quotes.Transform
import arrow.meta.quotes.classDeclaration
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irInt
import org.jetbrains.kotlin.ir.builders.irReturn
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.types.*
import org.jetbrains.kotlin.ir.util.functions
import org.jetbrains.kotlin.ir.util.properties

const val methodName = "shallowSize"

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
                            fun $methodName() = 10
                        }
                    """.trimIndent().`class`
                )
            },
            irClass { clazz ->
                if (clazz.isData) {
                    clazz.functions.forEach {
                        if (it.name.toString() == methodName && it.valueParameters.isEmpty()) {
                            it.body = DeclarationIrBuilder(pluginContext, it.symbol).irBlockBody {
                                +irReturn(irInt(clazz.shallowSize()))
                            }
                        }
                    }
                }
                clazz
            },
        )
    }

fun IrClass.shallowSize(): Int {
    return properties.sumOf {
        val type = it.backingField?.type ?: return@sumOf 0
        return@sumOf when {
            type.isByte() -> Byte.SIZE_BYTES
            type.isUByte() -> UByte.SIZE_BYTES
            type.isShort() -> Short.SIZE_BYTES
            type.isUShort() -> UShort.SIZE_BYTES
            type.isInt() -> Int.SIZE_BYTES
            type.isUInt() -> UInt.SIZE_BYTES
            type.isLong() -> Long.SIZE_BYTES
            type.isULong() -> ULong.SIZE_BYTES
            type.isFloat() -> Float.SIZE_BYTES
            type.isDouble() -> Double.SIZE_BYTES
            type.isChar() -> Char.SIZE_BYTES
            type.isBoolean() -> 1
            else -> 4 // consider that pointer takes 4 bytes
        }
    }
}
