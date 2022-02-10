package ru.hse.shallow

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


class AddShallowSizeMethodTest {
    @Test
    fun `shallow size exists`() {
        val x = A("Hello")
        assertEquals(10, x.shallowSize())
    }

    @Test
    fun `shallow size with class visibility modifier`() {
        val x = B(true)
        assertEquals(10, x.shallowSize())
    }

    @Test
    fun `shallow size with class inheritance`() {
        val x = Inherit(3)
        assertEquals(10, x.shallowSize())
    }

    @Test
    fun `shallow size with no back field`() {
        val x = NoBackField('c')
        assertEquals(10, x.shallowSize())
    }

    @Test
    fun `shallow size with private fields`() {
        val x = PrivateFields(3)
        assertEquals(10, x.shallowSize())
    }

    @Test
    fun `shallow size with multiple fields in constructor`() {
        val x = MutltipleFieldsInConstructor(1, 2, 3, 4)
        assertEquals(10, x.shallowSize())
    }

    @Test
    fun `shallow size with nullable primitives`() {
        val x = NullablePrimitives(1f, 1.0, 'c', true)
        assertEquals(10, x.shallowSize())
    }
}
