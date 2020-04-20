package com.test.qianbailu

import android.app.Application
import cn.leancloud.AVOSCloud
import com.tencent.bugly.crashreport.CrashReport
import com.test.qianbailu.model.BASE_URL
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import top.cyixlq.core.CoreManager


class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        CrashReport.initCrashReport(applicationContext, "6059853783", BuildConfig.DEBUG)
        CoreManager.configNetWork(baseUrl = BASE_URL)
            .configCLog(isEnableLog = BuildConfig.DEBUG)
            .init(this)
        AVOSCloud.initialize(this, "sDlEtmzGMc9vjA4Ew2ddosBE-MdYXbMMI", "miL1uWuqiFaHVrrdsWSlv07A")
        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(httpModule, mvvmModule)
        }
    }
}