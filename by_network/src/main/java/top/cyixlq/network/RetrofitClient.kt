package top.cyixlq.network

import com.google.gson.reflect.TypeToken
import com.orhanobut.logger.Logger
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import top.cyixlq.core.net.exception.CustomNetException
import top.cyixlq.core.utils.CLog
import top.cyixlq.core.utils.FormatUtil
import top.cyixlq.network.bean.BaseResponse
import top.cyixlq.network.config.API_VERSION
import top.cyixlq.network.config.RESULT_OK
import top.cyixlq.network.config.SERVICE_ERR
import top.cyixlq.network.utils.jsonToObject
import top.cyixlq.network.utils.toJson
import java.lang.IllegalArgumentException
import java.lang.reflect.Type

class RetrofitClient private constructor() {

    companion object {
        @JvmStatic
        fun create(): RetrofitClient {
            return RetrofitClient()
        }

        private const val TAG_PARAMS = "NetWork_Params"
        private const val TAG_RESPONSE = "NetWork_Response"
    }

    private var params: HashMap<String, Any> = HashMap()
    private var type: String? = null
    private var apiVersion: String = API_VERSION

    fun addParam(key: String, value: Any): RetrofitClient {
        this.params[key] = value
        return this
    }

    fun addParams(params: HashMap<String, Any>): RetrofitClient {
        this.params.putAll(params)
        return this
    }

    fun setType(type: String): RetrofitClient {
        this.type = type
        return this
    }

    fun setApiVersion(apiVersion: String): RetrofitClient {
        this.apiVersion = apiVersion
        return this
    }

    private fun getData(): HashMap<String, Any> {
        type?.let {
            CLog.t(TAG_PARAMS).json(params.toJson())
            return ByNetWorkManager.getConvert().encodeData(params, it, apiVersion)
        }
        throw RuntimeException("你必须给请求设置type")
    }

    /**
     *  返回Observable部分方法重载
     */
    @JvmOverloads
    fun <T> executeObservable(clazz: Class<T>? = null,typeToken: TypeToken<T>? = null, type: Type? = null): Observable<T> {
        return ByNetWorkManager.getCommonService().post(getData())
            .subscribeOn(Schedulers.io())
            .map {
                val baseResponse = it.string().jsonToObject<BaseResponse>()
                if (baseResponse.code != RESULT_OK) {
                    throw CustomNetException(baseResponse.code, baseResponse.msg)
                }
                baseResponse
            }
            .map {
                val result = it.data as? String ?: throw CustomNetException(SERVICE_ERR, it.msg)
                val decodeString = ByNetWorkManager.getConvert().decodeData(result)
                CLog.t(TAG_RESPONSE).json(decodeString)
                when {
                    clazz != null -> return@map decodeString.jsonToObject(clazz)
                    typeToken != null -> return@map decodeString.jsonToObject<T>(typeToken.type)
                    else -> {
                        if (type == null) {
                            throw IllegalArgumentException("clazz，typeToken，type三个参数中必须至少一个不是null")
                        }
                        return@map decodeString.jsonToObject<T>(type)
                    }
                }
            }
    }

    /**
     *  返回Flowable部分方法重载
     */
    @JvmOverloads
    fun <T> executeFlowable(clazz: Class<T>? = null, typeToken: TypeToken<T>? = null, type: Type? = null): Flowable<T> {
        return ByNetWorkManager.getCommonService().postFlowable(getData())
            .subscribeOn(Schedulers.io())
            .map {
                val baseResponse = it.string().jsonToObject<BaseResponse>()
                if (baseResponse.code != RESULT_OK) {
                    throw CustomNetException(baseResponse.code, baseResponse.msg)
                }
                baseResponse
            }
            .map {
                val result = it.data as? String ?: throw CustomNetException(SERVICE_ERR, it.msg)
                val decodeString = ByNetWorkManager.getConvert().decodeData(result)
                CLog.t(TAG_RESPONSE).json(decodeString)
                when {
                    clazz != null -> return@map decodeString.jsonToObject(clazz)
                    typeToken != null -> return@map decodeString.jsonToObject<T>(typeToken.type)
                    else -> {
                        if (type == null) {
                            throw IllegalArgumentException("clazz，typeToken，type三个参数中必须至少一个不是null")
                        }
                        return@map decodeString.jsonToObject<T>(type)
                    }
                }
            }
    }
}