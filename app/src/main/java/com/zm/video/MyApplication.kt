package com.zm.video

import android.app.Application
import com.tencent.bugly.crashreport.CrashReport
import com.zm.video.model.Repo
import top.cyixlq.core.CoreManager

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        CrashReport.initCrashReport(applicationContext, "6059853783", false)
        CoreManager.configNetWork(baseUrl = "https://011009.www.cdn.cmavs.com")
            .configCLog(isEnableLog = true)
            .init(this)
        Repo.init()
    }
}