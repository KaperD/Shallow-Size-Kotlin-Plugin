package ru.hse.shallow

import arrow.meta.CliPlugin
import arrow.meta.Meta
import arrow.meta.invoke
import arrow.meta.quotes.Transform
import arrow.meta.quotes.classDeclaration
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.backend.wasm.ir2wasm.getSuperClass
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irInt
import org.jetbrains.kotlin.ir.builders.irReturn
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.descriptors.IrBuiltIns
import org.jetbrains.kotlin.ir.types.*
import org.jetbrains.kotlin.ir.util.functions
import org.jetbrains.kotlin.ir.util.properties
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtFunction

const val methodName = "shallowSize"

val Meta.addShallowSizeMethod: CliPlugin
    get() = "Add shallowSize method to data classes" {
        meta(
            classDeclaration(this, { element.isData() }) {
                if (it.element.hasShallowSizeMethod()) {
                    val message = "Class ${it.element.name} already has method $methodName"
                    messageCollector?.report(CompilerMessageSeverity.ERROR, message)
                    error(message)
                }
                val superTypes = if (supertypes.isEmpty()) "" else ": $supertypes"
                Transform.replace(
                    it.element,
                    """
                        $`@annotations` $visibility $modality $kind $name $`(typeParameters)` $`(params)` $superTypes {
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
                                +irReturn(irInt(clazz.shallowSize(irBuiltIns)))
                            }
                        }
                    }
                }
                clazz
            },
        )
    }

fun KtClass.hasShallowSizeMethod(): Boolean {
    declarations.forEach {
        if (it is KtFunction
            && it.name == methodName
            && it.valueParameters.isEmpty()
        ) {
            return true
        }
    }
    return false
}

fun IrClass.shallowSize(irBuiltIns: IrBuiltIns): Int {
    var result = 0
    getSuperClass(irBuiltIns)?.let {
        result += it.shallowSize(irBuiltIns)
    }
    return result + properties.sumOf {
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
