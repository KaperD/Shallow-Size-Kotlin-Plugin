package ru.hse.shallow

import java.io.Serializable

data class A(val name: String)

internal data class B(var isReady: Boolean)

data class Inherit(val x: Int) : Serializable, Runnable {
    override fun run() {
        TODO("Not yet implemented")
    }

    val y: Int = 0
}

data class NoBackField(val x: Char) {
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
