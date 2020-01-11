package top.cyixlq.network.utils

import com.google.gson.reflect.TypeToken
import top.cyixlq.core.utils.FormatUtil
import java.lang.reflect.Type

fun <T> String.jsonToObject(clazz: Class<T>): T =
    FormatUtil.getGson().fromJson<T>(this, clazz)

fun <T> String.jsonToObject(type: Type): T =
    FormatUtil.getGson().fromJson<T>(this, type)

inline fun <reified T> String.jsonToObject(): T =
    FormatUtil.getGson().fromJson<T>(this, object : TypeToken<T>() {}.type)

fun Any.toJson(): String = FormatUtil.getGson().toJson(this)

inline fun <reified T> getTypeToken(): TypeToken<T> = object : TypeToken<T>() {}
