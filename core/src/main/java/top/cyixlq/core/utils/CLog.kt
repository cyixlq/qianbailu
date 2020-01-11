package top.cyixlq.core.utils

import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.orhanobut.logger.Printer

object CLog {

    fun init(
        isEnableLog: Boolean = true,
        showThreadInfo: Boolean = true,
        methodCount: Int = 2,
        tag: String = "CY_TAG"
    ) {
        val formatStrategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(showThreadInfo)  // 是否显示当前所在线程名称
            .methodCount(methodCount)        // 一行显示多少个方法默认为2
            .tag(tag)   // 全局Log标签
            .build()
        Logger.addLogAdapter(object : AndroidLogAdapter(formatStrategy) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return isEnableLog
            }
        })
    }

    fun t(tag: String?): Printer {
        return Logger.t(tag)
    }

    fun d(any: Any?) {
        Logger.d(any)
    }

    fun d(message: String, vararg any: Any?) {
        Logger.d(message, any)
    }

    fun e(message: String, vararg any: Any?) {
        Logger.e(message, any)
    }

    fun e(throwable: Throwable, message: String, vararg any: Any?) {
        Logger.e(throwable, message, any)
    }

    fun i(message: String, vararg any: Any?) {
        Logger.i(message, any)
    }

    fun v(message: String, vararg any: Any?) {
        Logger.v(message, any)
    }

    fun w(message: String, vararg any: Any?) {
        Logger.w(message, any)
    }

    fun wtf(message: String, vararg any: Any?) {
        Logger.wtf(message, any)
    }

    fun json(json: String?) {
        Logger.json(json)
    }

    fun xml(xml: String?) {
        Logger.xml(xml)
    }

}