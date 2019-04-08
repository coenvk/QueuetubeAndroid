package com.arman.queuetube.util

import android.content.Intent

inline fun <reified T : Enum<T>> Intent.putExtra(extra: T): Intent = putExtra(T::class.qualifiedName, extra.ordinal)

inline fun <reified T : Enum<T>> Intent.getEnumExtra(): T? = getIntExtra(T::class.qualifiedName, -1).takeUnless { it == -1 }?.let { T::class.java.enumConstants[it] }