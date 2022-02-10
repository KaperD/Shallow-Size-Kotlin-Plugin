package ru.hse.shallow

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


class AddShallowSizeMethodTest {
    @Test
    fun `shallow size exists`() {
        val a = A("Hello")
        assertEquals(10, a.shallowSize())
    }
}
