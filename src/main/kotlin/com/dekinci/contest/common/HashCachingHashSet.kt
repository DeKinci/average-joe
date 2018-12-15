package com.dekinci.contest.common

import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

class HashCachingHashSet<T> : HashSet<T> {
    constructor() : super()
    constructor(c: Collection<T>) : super() {
        addAll(c)
    }
    constructor(initialCapacity: Int, loadFactor: Float) : super(initialCapacity, loadFactor)
    constructor(initialCapacity: Int) : super(initialCapacity)

    private val hash = AtomicInteger(0)
    private val isValid = AtomicBoolean(false)

    override fun hashCode(): Int {
        if (!isValid.get()) {
            hash.set(super.hashCode())
            isValid.set(true)
        }

        return hash.get()
    }

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun clear() {
        isValid.set(false)
        super.clear()
    }

    override fun remove(element: T): Boolean {
        isValid.set(false)
        return super.remove(element)
    }

    override fun add(element: T): Boolean {
        isValid.set(false)
        return super.add(element)
    }
}