package com.skopzz.melodycover.util

import android.content.SharedPreferences

@Suppress("UNCHECKED_CAST")
fun <T> SharedPreferences.get(k: String?): T? = all[k] as T?

@Suppress("UNCHECKED_CAST")
fun <T> SharedPreferences.get(k: String?, defaultValue: T): T = all[k] as T? ?: defaultValue
fun SharedPreferences.set(k: String?, v: Any?): SharedPreferences {
  val editor = edit()

  if (v is Int) {
    editor.putInt(k, v)
  } else if (v is String) {
    editor.putString(k, v)
  } else if (v is Boolean) {
    editor.putBoolean(k, v)
  } else if (v is Long) {
    editor.putLong(k, v)
  } else if (v is Float) {
    editor.putFloat(k, v)
  } else {
    try {
      @Suppress("UNCHECKED_CAST")
      editor.putStringSet(k, v as Set<String>)
    } catch (e: ClassCastException) {
      throw RuntimeException("unsupported", e)
    }
  }

  editor.apply()

  return this
}