package com.dekinci.bot.common

import java.util.*

fun <T> newWeakHashSet(): MutableSet<T> = Collections.newSetFromMap(WeakHashMap<T, Boolean>())

fun <T> newWeakHashSet(element: T): MutableSet<T> = Collections.newSetFromMap(WeakHashMap<T, Boolean>()).also { it.add(element) }

fun <T> newWeakHashSet(iterable: Iterable<T>): MutableSet<T> = Collections.newSetFromMap(WeakHashMap<T, Boolean>()).also { it.addAll(iterable) }