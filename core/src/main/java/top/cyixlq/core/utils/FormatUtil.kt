package top.cyixlq.core.utils

import com.google.gson.Gson

object FormatUtil {

    private val gson = Gson()

    fun getGson(): Gson {
        return gson
    }
}