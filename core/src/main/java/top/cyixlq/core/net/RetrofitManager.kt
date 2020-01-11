package top.cyixlq.core.net

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import top.cyixlq.core.net.bean.NetWorkConfig
import top.cyixlq.core.utils.FormatUtil
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


    private val retrofit: Retrofit
    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addNetworkInterceptor(
            HttpLoggingInterceptor(InterceptorLogger()).apply {
                HttpLoggingInterceptor.Level.BODY
            }
        )
        .readTimeout(config.readTimeOut, TimeUnit.SECONDS)
        .connectTimeout(config.connectTimeOut, TimeUnit.SECONDS)
        .writeTimeout(config.writeTimeOut, TimeUnit.SECONDS)
        .build()

    init {
        retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(config.baseUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(FormatUtil.getGson()))
            .build()
    }

    fun <T> create(clazz: Class<T>): T {
        return retrofit.create(clazz)
    }
}