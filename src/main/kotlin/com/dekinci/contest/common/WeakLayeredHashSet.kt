package com.dekinci.contest.common

import java.util.*

class WeakLayeredHashSet<T, K>(initial: Set<T> = emptySet()) : AbstractMutableSet<T>() {
    private data class ImmaterialKeyToPair<T, K>(val key: K, val pair: Pair<HashSet<T>, HashSet<T>>) {
        override fun equals(other: Any?): Boolean {
            return other is ImmaterialKeyToPair<*, *> && other.pair == pair
        }

        override fun hashCode(): Int {
            return pair.hashCode()
        }
    }

    private val base = HashSet(initial)

    private val positiveLayers = WeakHashMap<K?, HashSet<T>>()
    private val negativeLayers = WeakHashMap<K?, HashSet<T>>()
    private var defaultKey: K? = null

    init {
        positiveLayers[null] = HashCachingHashSet()
        negativeLayers[null] = HashCachingHashSet()
    }

    fun clearLayers() {
        positiveLayers.clear()
        negativeLayers.clear()

        positiveLayers[null] = HashCachingHashSet()
        negativeLayers[null] = HashCachingHashSet()
    }

    fun setDefault(key: K? = null) {
        defaultKey = key
    }

    fun addLayer(key: K, additions: Set<T> = emptySet(), deletions: Set<T> = emptySet()): MutableSet<T> {
        positiveLayers[key] = HashCachingHashSet(additions)
        negativeLayers[key] = HashCachingHashSet(deletions)
        return PartialSet(key)
    }

    fun get(key: K?): MutableSet<T> = PartialSet(key)

    fun removeLayer(key: K) {
        positiveLayers.remove(key)
        negativeLayers.remove(key)
    }

    fun mergeLayer(key: K?) {
        base.removeAll(negativeLayers[key]!!)
        base.addAll(positiveLayers[key]!!)
        positiveLayers[key]!!.clear()
        negativeLayers[key]!!.clear()
    }

    fun mergeRemoveLayer(key: K) {
        base.addAll(positiveLayers[key]!!)
        base.removeAll(negativeLayers[key]!!)
        positiveLayers.remove(key)
        negativeLayers.remove(key)
    }

    fun extendLayer(parent: K?, key: K): MutableSet<T> {
        addLayer(key, positiveLayers[parent]!!, negativeLayers[parent]!!)
        return PartialSet(key)
    }

    fun rotateToLayer(key: K?) {
        val positives = positiveLayers[key]!!
        val negatives = negativeLayers[key]!!

        negativeLayers.forEach { it.value.addAll(positives.subtract(positiveLayers[it.key]!!)) }
        positiveLayers.forEach { it.value.addAll(negatives.subtract(negativeLayers[it.key]!!)) }
        mergeLayer(key)
    }

    fun layersEqual(first: K, second: K) = positiveLayers[first] == positiveLayers[second] &&
            negativeLayers[first] == negativeLayers[second]

    fun distinct(): Set<K> {
        val set = HashSet<ImmaterialKeyToPair<T, K?>>()
        for (key in positiveLayers.keys)
            set.add(ImmaterialKeyToPair(key, positiveLayers[key]!! to negativeLayers[key]!!))

        set.add(ImmaterialKeyToPair(null, positiveLayers[null]!! to negativeLayers[null]!!))

        positiveLayers.clear()
        negativeLayers.clear()

        val keys = HashSet<K>()

        for (ktp in set) {
            ktp.key?.let { keys.add(it) }
            positiveLayers[ktp.key] = ktp.pair.first
            negativeLayers[ktp.key] = ktp.pair.second
        }

        return keys
    }

    override val size: Int
        get() = size(defaultKey)

    fun size(key: K?) = base.union(positiveLayers[key]!!).subtract(negativeLayers[key]!!).size

    val baseSize: Int
        get() = base.size

    override fun add(element: T) = add(element, defaultKey)

    fun add(element: T, key: K?): Boolean {
        if (!negativeLayers[key]!!.remove(element))
            return if (!base.contains(element)) positiveLayers[key]!!.add(element) else false

        return true
    }

    fun baseAdd(element: T): Boolean {
        positiveLayers.values.forEach { it.remove(element) }
        return base.add(element)
    }

    fun baseGet(): MutableSet<T> = base

    override fun remove(element: T) = remove(element, defaultKey)

    fun remove(element: T, key: K?): Boolean {
        if (!positiveLayers[key]!!.remove(element))
            return if (base.contains(element))
                negativeLayers[key]!!.add(element)
            else false

        return true
    }

    fun baseRemove(element: T): Boolean {
        negativeLayers.values.forEach { it.remove(element) }
        return base.remove(element)
    }

    override fun contains(element: T) = contains(element, defaultKey)

    fun contains(element: T, key: K?): Boolean {
        return element in positiveLayers[key]!! || (element !in negativeLayers[key]!! && element in base)
    }

    fun baseContains(element: T) = base.contains(element)

    override fun clear() {
        clearLayer(defaultKey)
    }

    fun clearLayer(key: K?) {
        positiveLayers[key]!!.clear()
        negativeLayers[key]!!.clear()
        negativeLayers[key]!!.addAll(base)
    }

    fun clearBase() {
        base.clear()
    }

    override fun isEmpty() = size == 0

    fun isEmpty(key: K?) = size(key) == 0

    fun baseIsEmpty() = base.isEmpty()

    override fun iterator(): MutableIterator<T> = iterator(defaultKey)

    fun iterator(key: K?): MutableIterator<T> =
            base.union(positiveLayers[key]!!).subtract(negativeLayers[key]!!).toMutableSet().iterator()

    fun baseIterator() = base.toSet().iterator()

    fun keysIterator() = positiveLayers.keys.filter { it != null }.iterator()

    private inner class PartialSet(private val key: K?) : AbstractMutableSet<T>() {

        override val size: Int
            get() = size(key)

        override fun add(element: T) = add(element, key)

        override fun iterator() = iterator(key)

        override fun clear() {
            clearLayer(key)
        }

        override fun contains(element: T) = contains(element, key)

        override fun remove(element: T) = remove(element, key)
    }
}