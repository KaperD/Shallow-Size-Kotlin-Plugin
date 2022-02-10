package ru.hse.shallow

import java.io.Serializable

data class A(val name: String)

internal data class B(var isReady: Boolean)

data class InheritInterface(val x: Int) : Serializable, Runnable {
    override fun run() {
        TODO("Not yet implemented")
    }
}

open class Base(open val a: Int)

data class InheritClass(val x: Int) : Base(x)

data class NoBackField(val x: Char = 'a') {
    val y: Int
        get() = 0
}

data class PrivateFields(val x: Long) {
    private val y: Int = 0
}

data class MutltipleFieldsInConstructor(
    val b: Byte,
    val s: Short,
    val i: Int,
    val l: Long
)

data class NullablePrimitives(
    val f: Float?,
    val d: Double?,
    val c: Char?,
    val b: Boolean?
)

//data class HasShallowSize(val x: Int) {
//    fun shallowSize(): Long = 0
//}

data class JavaCharacter(val x: Character)

data class NoExplicitType(val x: Int) {
    val y = 10L
}

interface BaseInterface {
    val a: Int
}

data class OverrideFieldFromClass(override val a: Int) : Base(3)

data class OverrideFieldFromInterface(override val a: Int) : BaseInterface

