package com.test.qianbailu

import android.app.Application
import com.test.qianbailu.model.Repo
import top.cyixlq.core.CoreManager

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        CoreManager.configNetWork(baseUrl = "https://011009.www.cdn.cmavs.com")
            .configCLog(isEnableLog = BuildConfig.DEBUG)
            .init(this)
        Repo.init()
    }
}