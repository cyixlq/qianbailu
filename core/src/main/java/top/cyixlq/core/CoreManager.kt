package top.cyixlq.core

import android.app.Application
import top.cyixlq.core.net.RetrofitManager
import top.cyixlq.core.net.bean.NetWorkConfig
import top.cyixlq.core.utils.CLog
import java.lang.RuntimeException

object CoreManager {

    private var application: Application? = null

    fun init(application: Application): CoreManager {
        CoreManager.application = application
        return this
    }

    fun configNetWork(
        readTimeOut: Long = 10,
        connectTimeOut: Long = 10,
        writeTimeOut: Long = 10,
        baseUrl: String
    ): CoreManager {
        RetrofitManager.config(
            NetWorkConfig(
                readTimeOut,
                connectTimeOut,
                writeTimeOut,
                baseUrl
            )
        )
        return this
    }

    fun configCLog(
        isEnableLog: Boolean = true,
        showThreadInfo: Boolean = true,
        methodCount: Int = 2,
        tag: String = "CY_TAG"
    ): CoreManager {
        CLog.init(isEnableLog, showThreadInfo, methodCount, tag)
        return this
    }

    fun getApplication(): Application {
        application?.let {
            return it
        }
        throw RuntimeException("You must init CommonManager")
    }
}