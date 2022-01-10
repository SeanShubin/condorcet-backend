package com.seanshubin.condorcet.backend.service

import com.seanshubin.condorcet.backend.service.CaseInsensitiveStringListUtil.extra
import com.seanshubin.condorcet.backend.service.CaseInsensitiveStringListUtil.missing
import org.junit.Test
import kotlin.test.assertEquals

class CaseInsensitiveStringListUtilTest {
    @Test
    fun missing() {
        val oldList = listOf("Aaa", "Bbb", "Ccc", "Ddd")
        val newList = listOf("Aaa", "bbb", "ccc", "CCC", "eee", "EEE")
        val actual = oldList.missing(newList)
        val expected = listOf("Ddd")
        assertEquals(expected, actual)
    }

    @Test
    fun extra() {
        val oldList = listOf("Aaa", "Bbb", "Ccc", "Ddd")
        val newList = listOf("Aaa", "bbb", "ccc", "CCC", "eee", "EEE")
        val actual = oldList.extra(newList)
        val expected = listOf("eee")
        assertEquals(expected, actual)
    }
}
