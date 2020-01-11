package top.cyixlq.network.config

// 服务器端需要的公共参数
const val CLIENT_ID = "by565fa4facdb191"
const val ALG = "md5"
const val APP_TYPE = "Android"
const val CLIENT_SECRETE = "b6b27d3182d589b92424cac0f2876fcd"
const val API_VERSION = "100" // 默认API等级
fun getLanguage(): String {
    return "zh-cn"
}

// 服务器标识码
const val RESULT_OK = 0 // 返回成功（正常）
const val SERVICE_ERR = -1 // 服务器异常标志码