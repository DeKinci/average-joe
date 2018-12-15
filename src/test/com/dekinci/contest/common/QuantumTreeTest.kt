package com.dekinci.contest.common

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.lang.ref.PhantomReference
import java.lang.ref.Reference
import java.lang.ref.ReferenceQueue
import java.util.*


internal class QuantumTreeTest {

    /**
     *    2
     *  /
     * 1
     *  \   4
     *   3
     *      5
     */
    @Test
    fun test() {
        val qt = QuantumTree(1.toString())
        val n22 = qt.root.next(3.toString())

        n22.next(4.toString())
        n22.next(5.toString())

        val rq = ReferenceQueue<QuantumTree<String>.Node>()
        val r1 = PhantomReference<QuantumTree<String>.Node>(qt.root, rq)
        val r2 = PhantomReference<QuantumTree<String>.Node>(qt.root.next(2.toString()), rq)

        qt.root = n22
        System.gc()

        val expectedSet = setOf<Reference<out QuantumTree<String>.Node>?>(r1, r2)
        val actualSet = HashSet<Reference<out QuantumTree<String>.Node>>()

        var ref: Reference<out QuantumTree<String>.Node>? = rq.remove(100)
        while (ref != null) {
            actualSet.add(ref)
            ref = rq.remove(100)
        }

        assertEquals(expectedSet, actualSet)
    }

    /**
     *       4
     *    2
     *   /   5
     * 1
     *   \   5
     *    3
     *       6
     */
    @Test
    fun quantumTest() {
        val qt = QuantumTree(1.toString())
        val n21: QuantumTree<String>.Node = qt.root.next(2.toString())
        n21.next(4.toString())
        val first = n21.next(5.toString())

        val n22: QuantumTree<String>.Node = qt.root.next(3.toString())
        val second = n22.next(5.toString())
        n22.next(6.toString())

        assertTrue(first === second)
    }
}

fun main(args: Array<String>) {

}