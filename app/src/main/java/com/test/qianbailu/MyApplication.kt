package com.test.qianbailu

import android.app.Application
import com.tencent.bugly.crashreport.CrashReport
import com.test.qianbailu.model.Repo
import top.cyixlq.core.CoreManager

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        CrashReport.initCrashReport(applicationContext, "6059853783", BuildConfig.DEBUG)
        CoreManager.configNetWork(baseUrl = "http://www.600avs.com/")
            .configCLog(isEnableLog = BuildConfig.DEBUG)
            .init(this)
        Repo.init()
    }
}