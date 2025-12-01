package org.example.app

import org.junit.Test
import org.junit.Assert.assertEquals

class MessageUtilsTest {
    @Test
    fun testGetMessage() {
        // Expect exactly what MessageUtils.message() returns
        assertEquals("Hello     World!", MessageUtils.message())
    }
}
