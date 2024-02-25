package top.cyixlq.core.utils

import com.google.gson.Gson

object FormatUtil {

    private val gson = Gson()

    fun getGson(): Gson {
        return gson
    }

    inline fun <reified T> String.fromJson(): T? {
        return try {
            getGson().fromJson(this, T::class.java)
        } catch (e: Exception) {
            null
        }
    }

    fun Any.toJson(): String =
        getGson().toJson(this)
}