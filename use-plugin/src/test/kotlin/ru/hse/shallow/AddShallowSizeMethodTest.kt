package ru.hse.shallow

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.File

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

    @Test
    fun `class with java Character field`() {
        val x = JavaCharacter(Character('3'))
        assertEquals(pointerSize, x.shallowSize())
    }

    @Test
    fun `class with no explicit type field`() {
        val x = NoExplicitType(3)
        assertEquals(Int.SIZE_BYTES + Long.SIZE_BYTES, x.shallowSize())
    }

    @Test
    fun `class with override field from class`() {
        val x = OverrideFieldFromClass(4)
        assertEquals(2 * Int.SIZE_BYTES, x.shallowSize())
    }

    @Test
    fun `class with override field from interface`() {
        val x = OverrideFieldFromInterface(4)
        assertEquals(Int.SIZE_BYTES, x.shallowSize())
    }

    @Test
    fun `class has shallowSize function don't compile`() {
        val kotlinSource = SourceFile.kotlin(
            "KClass.kt",
            """
                data class HasShallowSize(val x: Int) {
                    fun shallowSize(): Long = 0
                }
            """.trimIndent()
        )
        val result = KotlinCompilation().apply {
            sources = listOf(kotlinSource)
            inheritClassPath = true
            pluginClasspaths = listOf(File("../shallow-size-plugin/build/libs/shallow-size-plugin.jar"))
            messageOutputStream = System.out
        }.compile()
        assertEquals(KotlinCompilation.ExitCode.COMPILATION_ERROR, result.exitCode)
        assertTrue(result.messages.contains("Class HasShallowSize already has method shallowSize"))
    }
}
