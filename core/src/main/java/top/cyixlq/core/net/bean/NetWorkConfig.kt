package top.cyixlq.core.net.bean

import javax.net.ssl.HostnameVerifier
import javax.net.ssl.X509TrustManager

data class NetWorkConfig(
    val readTimeOut: Long = 10,
    val connectTimeOut: Long = 10,
    val writeTimeOut: Long = 10,
    val baseUrl: String = "",
    val trustManager: X509TrustManager? = null,
    val hostnameVerifier: HostnameVerifier? = null,
    val dnsConfig: DnsConfig? = null
)