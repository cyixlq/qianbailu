package com.test.qianbailu

import android.app.Application
import com.tencent.bugly.crashreport.CrashReport
import com.test.qianbailu.model.Repo
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import top.cyixlq.core.CoreManager

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        CrashReport.initCrashReport(applicationContext, "6059853783", BuildConfig.DEBUG)
        CoreManager.configNetWork(baseUrl = "http://www.600avs.com/")
            .configCLog(isEnableLog = BuildConfig.DEBUG)
            .init(this)
        Repo.init()
        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(httpModule, mvvmModule)
        }
    }
}