package com.dekinci.bot.common

class QuantumTree<T>(rootElement: T) {
    private val nodes = newWeakHashSet<Node>()

    inner class Node(val data: T, parent: Node? = null) {
        private val parents = newWeakHashSet<Node>()
        private val children = HashSet<Node>()

        init {
            parent?.let { parents.add(it) }
        }

        fun next(data: T): Node {
            var next = Node(data, this)

            val found = nodes.find { it == next }
            if (found != null) {
                next = found
                next.parents.add(this)
            }
            else
                nodes.add(next)

            children.add(next)
            return next
        }

        fun anyLastMatchingOrRoot(predicate: (T) -> Boolean): Node {
            var parents = newWeakHashSet<Node>(parents)

            while (parents.isNotEmpty()) {
                val nextParents = newWeakHashSet<Node>()
                for (parent in parents) {
                    if (predicate.invoke(parent.data))
                        return parent
                    nextParents.addAll(parent.children)
                }
                parents = nextParents
            }

            return this
        }

        override fun equals(other: Any?): Boolean {
            return other === this || (other is QuantumTree<*>.Node && other.data == data )
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