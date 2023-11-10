package com.test.qianbailu

import android.app.Application
import android.os.Build
import com.tencent.bugly.crashreport.CrashReport
import com.test.qianbailu.model.BASE_URL
import com.test.qianbailu.utils.PreferenceManager
import com.test.qianbailu.utils.TrustAllManager
import com.test.qianbailu.utils.UnSafeHostnameVerifier
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import top.cyixlq.core.CoreManager
import top.cyixlq.core.net.bean.DnsConfig
import top.cyixlq.core.utils.CLog

class MyApplication : Application() {

    companion object {
        @JvmStatic
        lateinit var INSTANCE: MyApplication
        private const val TAG = "MyApplication"
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        initCore()
        initBugly()
        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(httpModule, mvvmModule)
        }
    }

    private fun initBugly() {
        val strategy = CrashReport.UserStrategy(this)
        strategy.appVersion = BuildConfig.VERSION_NAME
        strategy.appPackageName = BuildConfig.APPLICATION_ID
        strategy.deviceModel = Build.MODEL
        strategy.deviceID = Build.ID
        CLog.t(TAG).i("device model: ${Build.MODEL}, id: ${Build.ID}")
        CrashReport.initCrashReport(applicationContext, "0efb702d9f", BuildConfig.DEBUG, strategy)
    }

    private fun initCore() {
        val dnsUrl = PreferenceManager.getDnsUrl()
        val dnsConfig = if (dnsUrl.isNullOrEmpty()) null else DnsConfig(dnsUrl, null)
        CoreManager.configNetWork(
            baseUrl = BASE_URL,
            dnsConfig = dnsConfig,
            trustManager = TrustAllManager(),
            hostnameVerifier = UnSafeHostnameVerifier()
        )
        .configCLog(isEnableLog = BuildConfig.DEBUG)
        .init(this)
    }
}