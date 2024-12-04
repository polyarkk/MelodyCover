package com.skopzz.melodycover.util

@Suppress("UNCHECKED_CAST")
inline fun <reified T : Number> Float.toSpecificNumber(): T =
  when (T::class) {
    Short::class -> toInt().toShort()
    Integer::class -> toInt()
    Long::class -> toLong()
    Float::class -> this
    Double::class -> toDouble()
    else -> throw UnsupportedOperationException()
  } as T

fun Float.roundTo(n: Int): Float = "%.${n}f".format(this).toFloat()

