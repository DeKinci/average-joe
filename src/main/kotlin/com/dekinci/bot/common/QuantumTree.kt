package com.dekinci.bot.common

import java.util.*
import kotlin.collections.HashSet

class QuantumTree<T>(rootElement: T) {
    private val nodes = Collections.newSetFromMap(WeakHashMap<Node, Boolean>())

    inner class Node(val data: T, parent: Node? = null) {
        private val parents = Collections.newSetFromMap(WeakHashMap<Node, Boolean>())
        private val children = HashSet<Node>()

        init {
            parent?.let { parents.add(it) }
        }

        fun next(data: T): Node {
            val next = Node(data, this)

//            val found = nodes.find { it == this }
//            if (found != null)
//                next = found
//            else
//                nodes.add(next)

            children.add(next)
            return next
        }

        override fun equals(other: Any?): Boolean {
            return other === this || (other is QuantumTree<*>.Node && other.data == data && other.parents == parents)
        }

        override fun hashCode(): Int {
            return data.hashCode()
        }

        override fun toString(): String {
            return data.toString()
        }
    }

    var root = Node(rootElement)

    init {
        nodes.add(root)
    }
}