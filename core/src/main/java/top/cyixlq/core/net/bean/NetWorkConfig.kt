package top.cyixlq.core.net.bean

data class NetWorkConfig(
    val readTimeOut: Long = 10,
    val connectTimeOut: Long = 10,
    val writeTimeOut: Long = 10,
    val baseUrl: String = ""
)