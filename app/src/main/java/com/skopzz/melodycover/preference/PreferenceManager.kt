package com.skopzz.melodycover.preference

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager(private val sharedPreferences: SharedPreferences) {
    /** @noinspection UnusedReturnValue
     */
    fun set(k: String?, v: Any?): PreferenceManager {
        val editor = sharedPreferences.edit()

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

    /**
     * gets value from preference map, exception may occur if type mismatch
     * @param k key
     * @return value
     * @param <T> type
    </T> */
    fun <T> get(k: String?): T? {
        @Suppress("UNCHECKED_CAST")
        return sharedPreferences.all[k] as T?
    }

    /**
     * see [PreferenceManager.get]
     * @param defaultValue default value
     */
    fun <T> get(k: String?, defaultValue: T): T {
        @Suppress("UNCHECKED_CAST")
        val v = sharedPreferences.all[k] as T? ?: return defaultValue

        return v
    }

    companion object {
        var instance: PreferenceManager? = null
            private set

        fun init(ctx: Context) {
            instance = PreferenceManager(android.preference.PreferenceManager.getDefaultSharedPreferences(ctx))
        }
    }
}
