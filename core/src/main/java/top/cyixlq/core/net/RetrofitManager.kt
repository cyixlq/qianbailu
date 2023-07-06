package top.cyixlq.core.net

import okhttp3.Cache
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.dnsoverhttps.DnsOverHttps
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import top.cyixlq.core.CoreManager
import top.cyixlq.core.net.bean.NetWorkConfig
import top.cyixlq.core.net.bean.SSLSocketFactoryCompat
import top.cyixlq.core.utils.FormatUtil
import java.io.File
import java.net.InetAddress
import java.util.concurrent.TimeUnit

class RetrofitManager private constructor() {

    private object SingleHolder {
        val INSTANCE = RetrofitManager()
    }

    companion object {
        private var config: NetWorkConfig =
            NetWorkConfig()

        @JvmStatic
        fun config(configs: NetWorkConfig) {
            config = configs

        }

        @JvmStatic
        fun getInstance(): RetrofitManager {
            return SingleHolder.INSTANCE
        }
    }


    private val retrofit: Retrofit = Retrofit.Builder()
        .client(getOkHttpClient())
        .baseUrl(config.baseUrl)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(FormatUtil.getGson()))
        .build()

    fun <T> create(clazz: Class<T>): T {
        return retrofit.create(clazz)
    }

    private fun getOkHttpClient(): OkHttpClient {
        val config = config
        val cacheDir = File(CoreManager.getApplication().externalCacheDir, "okhttpCache")
        val appCache = Cache(cacheDir, 10 * 1024 * 1024)
        val okhttpBuilder = OkHttpClient.Builder()
            .addNetworkInterceptor(
                HttpLoggingInterceptor(InterceptorLogger()).apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .cache(appCache)
            .readTimeout(config.readTimeOut, TimeUnit.SECONDS)
            .connectTimeout(config.connectTimeOut, TimeUnit.SECONDS)
            .writeTimeout(config.writeTimeOut, TimeUnit.SECONDS)
        if (config.trustManager != null) {
            val sslSocket = SSLSocketFactoryCompat(config.trustManager)
            okhttpBuilder.sslSocketFactory(sslSocket, config.trustManager)
        }
        if (config.hostnameVerifier != null) {
            okhttpBuilder.hostnameVerifier(config.hostnameVerifier)
        }
        val okHttpClient = okhttpBuilder.build()
        if (config.dnsConfig != null && config.dnsConfig.url.isNotEmpty()) {
            val dnsBuilder = DnsOverHttps.Builder().client(okHttpClient)
                .url(config.dnsConfig.url.toHttpUrl())
            config.dnsConfig.hosts?.let { array ->
                val hosts = array.map {
                    InetAddress.getByName(it)
                }.toList()
                dnsBuilder.bootstrapDnsHosts(hosts)
            }
            val dns = dnsBuilder.build()
            return okHttpClient.newBuilder().dns(dns).build()
        }
        return okHttpClient
    }
}