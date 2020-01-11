package top.cyixlq.network.convert

interface IConvert {
    fun encodeData(data: Map<String, Any>, vararg attach: String): HashMap<String, Any>
    fun decodeData(result: String): String
}