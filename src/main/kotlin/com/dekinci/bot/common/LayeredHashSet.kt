package com.dekinci.bot.common

class LayeredHashSet<T, K>(initial: Set<T> = emptySet()) : AbstractMutableSet<T>() {
    private val base = HashSet(initial)

    private val positiveLayers = HashMap<K?, HashSet<T>>()
    private val negativeLayers = HashMap<K?, HashSet<T>>()
    private var defaultKey: K? = null

    init {
        positiveLayers[null] = hashSetOf()
        negativeLayers[null] = hashSetOf()
    }

    fun setDefault(key: K? = null) {
        defaultKey = key
    }

    fun addLayer(key: K, additions: Set<T> = emptySet(), deletions: Set<T> = emptySet()) {
        positiveLayers[key] = HashSet(additions)
        negativeLayers[key] = HashSet(deletions)
    }

    fun removeLayer(key: K) {
        positiveLayers.remove(key)
        negativeLayers.remove(key)
    }

    fun mergeLayer(key: K?) {
        base.addAll(positiveLayers[key]!!)
        base.removeAll(negativeLayers[key]!!)
        positiveLayers[key]!!.clear()
        negativeLayers[key]!!.clear()
    }

    fun mergeRemoveLayer(key: K) {
        base.addAll(positiveLayers[key]!!)
        base.removeAll(negativeLayers[key]!!)
        positiveLayers.remove(key)
        negativeLayers.remove(key)
    }

    fun rotateToLayer(key: K?) {
        negativeLayers.filter { it.key != key }.forEach { it.value.addAll(positiveLayers[key]!!) }
        positiveLayers.filter { it.key != key }.forEach { it.value.addAll(negativeLayers[key]!!) }
        mergeLayer(key)
    }

    fun layersEqual(first: K, second: K) = positiveLayers[first] == positiveLayers[second] &&
            negativeLayers[first] == negativeLayers[second]

    fun distinct(): Set<K> {
        val distinctMap = positiveLayers
                .map { it.key to Pair(it.value, negativeLayers[it.key]!!) }
                .distinctBy { it.second }.toMap()

        positiveLayers.clear()
        negativeLayers.clear()

        positiveLayers.putAll(distinctMap.map { it.key to it.value.first })
        negativeLayers.putAll(distinctMap.map { it.key to it.value.second })

        return distinctMap.keys.filter { it != null }.map { it!! }.toSet()
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

    override fun remove(element: T) = remove(element, defaultKey)

    fun remove(element: T, key: K?): Boolean {
        if (!positiveLayers[key]!!.remove(element))
            return if (base.contains(element)) negativeLayers[key]!!.add(element) else false

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

    fun clearLayer(key: K?) {
        positiveLayers[key]!!.clear()
        negativeLayers[key]!!.clear()
    }

    fun clearBase(key: K?) {
        base.clear()
    }

    override fun isEmpty() = size == 0

    fun isEmpty(key: K?) = size(key) == 0

    fun baseIsEmpty() = base.isEmpty()

    override fun iterator(): MutableIterator<T> = iterator(defaultKey)

    fun iterator(key: K?): MutableIterator<T> =
            base.union(positiveLayers[key]!!).subtract(negativeLayers[key]!!).toMutableSet().iterator()

    fun baseIterator() = base.toSet().iterator()

    override fun equals(other: Any?): Boolean {
        return super<AbstractMutableSet>.equals(other)
    }

    override fun hashCode(): Int {
        return super<AbstractMutableSet>.hashCode()
    }
}