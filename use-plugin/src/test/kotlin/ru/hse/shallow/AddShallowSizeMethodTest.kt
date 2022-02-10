package ru.hse.shallow

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

const val pointerSize = 4

class AddShallowSizeMethodTest {
    @Test
    fun `shallow size exists`() {
        val x = A("Hello")
        assertEquals(pointerSize, x.shallowSize())
    }

    @Test
    fun `shallow size with class visibility modifier`() {
        val x = B(true)
        assertEquals(1, x.shallowSize())
    }

    @Test
    fun `shallow size with class inherit interfaces`() {
        val x = InheritInterface(3)
        assertEquals(Int.SIZE_BYTES, x.shallowSize())
    }

    @Test
    fun `shallow size with class inherit classes`() {
        val x = InheritClass(3)
        assertEquals(2 * Int.SIZE_BYTES, x.shallowSize())
    }

    @Test
    fun `shallow size with no back field`() {
        val x = NoBackField('c')
        assertEquals(2, x.shallowSize())
    }

    @Test
    fun `shallow size with private fields`() {
        val x = PrivateFields(3)
        assertEquals(Long.SIZE_BYTES + Int.SIZE_BYTES, x.shallowSize())
    }

    @Test
    fun `shallow size with multiple fields in constructor`() {
        val x = MutltipleFieldsInConstructor(1, 2, 3, 4)
        assertEquals(Byte.SIZE_BYTES + Short.SIZE_BYTES + Int.SIZE_BYTES + Long.SIZE_BYTES, x.shallowSize())
    }

    @Test
    fun `shallow size with nullable primitives`() {
        val x = NullablePrimitives(1f, 1.0, 'c', true)
        assertEquals(4 * pointerSize, x.shallowSize())
    }

//    @Test
//    fun `class has shallowSize function`() {
//        val x = HasShallowSize(3)
//        assertEquals(0L, x.shallowSize())
//    }

    @Test
    fun `class with java Character field`() {
        val x = JavaCharacter(Character('3'))
        assertEquals(pointerSize, x.shallowSize())
    }
}
