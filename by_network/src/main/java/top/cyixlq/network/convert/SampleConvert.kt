package top.cyixlq.network.convert

import top.cyixlq.network.ByNetWorkManager
import top.cyixlq.network.config.*
import top.cyixlq.network.utils.NetSecurityUtil

class SampleConvert: IConvert {

    // 将业务参数加密上传到服务器(其中attach为一些附加参数, 这一部分自己定义所需) 这里的[0]为type [1]为apiVersion
    override fun encodeData(data: Map<String, Any>, vararg attach: String): HashMap<String, Any> {
        val paramsString = NetSecurityUtil.dataEncrypt(data)  // 将用户传入的键值对转换成json，加密后变为String
        val params = HashMap<String, Any>() // 重新封装成后台需要的数据格式
        params["api_ver"] = attach[1]
        params["type"] = attach[0]
        val timeStamp = System.currentTimeMillis() / 100
        params["notify_id"] = timeStamp
        params["time"] = timeStamp
        params["alg"] = ALG
        params["data"] = paramsString
        params["app_version"] = ByNetWorkManager.getVersionName()
        params["lang"] = getLanguage()
        params["app_type"] = APP_TYPE
        val sign = NetSecurityUtil.getMD5Sign(timeStamp.toString(), attach[0], paramsString, CLIENT_SECRETE, timeStamp.toString())
        params["sign"] = sign
        val map = HashMap<String, Any>()
        map["client_id"] = CLIENT_ID
        map["itboye"] = NetSecurityUtil.encodeData(NetSecurityUtil.desEncodeUserParam(params, CLIENT_SECRETE))
        return map
    }

    // 将服务端返回的String解密成json字符串返回
    override fun decodeData(result: String): String {
        return NetSecurityUtil.decodeData(result)
    }
}