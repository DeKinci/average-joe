package com.dekinci.bot.common

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach

internal class LayeredHashSetTest {
    var set = LayeredHashSet<Int, Int>()

    @BeforeEach
    fun setup() {
        set = LayeredHashSet()
    }

    @Test
    fun test() {

    }

    @Test
    fun setDefault() {
    }

    @Test
    fun addLayer() {
    }

    @Test
    fun removeLayer() {
    }

    @Test
    fun mergeLayer() {
    }

    @Test
    fun mergeRemoveLayer() {
    }

    @Test
    fun rotateToLayer() {
        set.baseAdd(1)
        set.baseAdd(2)
        set.baseAdd(5)

        set.addLayer(0)
        val first = set.get(0)
        set.addLayer(1)
        val second = set.get(1)

        first.add(3)
        first.remove(1)

        second.add(4)
        second.add(3)
        second.remove(5)

        set.rotateToLayer(0)

        assertEquals(setOf(2, 3, 5), first)
        assertEquals(setOf(1, 2, 3, 4), second)
        assertEquals(setOf(2, 3, 5), set.baseGet())
    }

    @Test
    fun getSize() {
    }

    @Test
    fun size() {
    }

    @Test
    fun getBaseSize() {
    }

    @Test
    fun add() {
        set.add(1)

        assertEquals(1, set.size)
        assertTrue(set.contains(1))
    }

    @Test
    fun add1() {
        set.addLayer(0)
        set.add(1, 0)

        assertEquals(1, set.size(0))
        assertEquals(0, set.size)
        assertTrue(set.contains(1, 0))
    }

    @Test
    fun baseAdd() {
        set.baseAdd(1)
        assertEquals(1, set.size)
        assertEquals(1, set.baseSize)
        assertTrue(set.contains(1))
        assertTrue(set.baseContains(1))
    }

    @Test
    fun remove() {
        set.add(1)
        set.remove(1)

        assertEquals(0, set.size)
        assertFalse(set.contains(1))
        assertTrue(set.isEmpty())
    }

    @Test
    fun remove1() {
        set.addLayer(0)
        set.add(1, 0)
        set.remove(1, 0)

        assertEquals(0, set.size)
        assertFalse(set.contains(1, 0))
        assertTrue(set.isEmpty(0))
    }

    @Test
    fun baseRemove() {
        set.baseAdd(1)
        set.baseRemove(1)

        assertEquals(0, set.size)
        assertEquals(0, set.baseSize)
        assertFalse(set.contains(1))
        assertFalse(set.baseContains(1))
    }

    @Test
    fun contains() {
    }

    @Test
    fun contains1() {
    }

    @Test
    fun baseContains() {
    }

    @Test
    fun clearLayer() {
    }

    @Test
    fun clearBase() {
    }
}