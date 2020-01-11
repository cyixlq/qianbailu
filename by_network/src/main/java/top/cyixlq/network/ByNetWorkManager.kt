package top.cyixlq.network

import top.cyixlq.core.net.RetrofitManager
import top.cyixlq.core.utils.VersionUtil
import top.cyixlq.network.convert.IConvert
import top.cyixlq.network.convert.SampleConvert
import top.cyixlq.network.service.CommonService


object ByNetWorkManager {

    private var commonService: CommonService = RetrofitManager.getInstance().create(CommonService::class.java)

    private var convert: IConvert? = null
    private var version = "un_know"

    fun setConvert(convert: IConvert): ByNetWorkManager {
        this.convert = convert
        return this
    }

    fun init() {
        if (this.convert == null) {
            this.convert = SampleConvert()
        }
        this.version = VersionUtil.getVersionName()
    }

    fun getConvert(): IConvert {
        this.convert?.let {
            return it
        }
        throw RuntimeException("你必须初始化ByNetWorkManager")
    }

    fun getVersionName(): String {
        return this.version
    }

    fun getCommonService(): CommonService {
        return commonService
    }
}