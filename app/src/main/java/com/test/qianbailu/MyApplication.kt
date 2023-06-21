package com.test.qianbailu

import android.app.Application
import androidx.preference.PreferenceManager
import com.tencent.bugly.crashreport.CrashReport
import com.test.qianbailu.model.BASE_URL
import com.test.qianbailu.utils.TrustAllManager
import com.test.qianbailu.utils.UnSafeHostnameVerifier
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import top.cyixlq.core.CoreManager
import top.cyixlq.core.net.bean.DnsConfig

class MyApplication : Application() {

    companion object {
        @JvmStatic
        lateinit var INSTANCE: MyApplication
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        CrashReport.initCrashReport(applicationContext, "0efb702d9f", BuildConfig.DEBUG)
        initCore()
        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(httpModule, mvvmModule)
        }
    }

    private fun initCore() {
        val keyDnsUrl = getString(R.string.key_dns_url)
        val dnsUrl = PreferenceManager.getDefaultSharedPreferences(this).getString(keyDnsUrl, "")
        val dnsConfig = if (dnsUrl.isNullOrEmpty()) null else DnsConfig(dnsUrl, null)
        CoreManager.configNetWork(
            baseUrl = BASE_URL,
            dnsConfig = dnsConfig,
            trustManager = TrustAllManager(),
            hostnameVerifier = UnSafeHostnameVerifier()
        ).configCLog(isEnableLog = BuildConfig.DEBUG).init(this)
    }
}