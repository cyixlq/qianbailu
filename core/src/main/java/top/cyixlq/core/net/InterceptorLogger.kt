package top.cyixlq.core.net

import com.orhanobut.logger.Logger
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import org.json.JSONObject

class InterceptorLogger: HttpLoggingInterceptor.Logger {

    private val mMessage = StringBuilder()

    override fun log(message: String) {
        /*if (message.startsWith("--> POST")
            || message.startsWith("--> GET")) {
            mMessage.clear()
        }*/
        if (message.startsWith("{") && message.endsWith("}")) {
            mMessage.append(JSONObject(message).toString(2) + "\n")
        } else if (message.startsWith("[") && message.endsWith("]")) {
            mMessage.append(JSONArray(message).toString(2) + "\n")
        } else {
            mMessage.append(message + "\n")
        }
        if (message.startsWith("<-- END HTTP")) {
            Logger.d(mMessage.toString())
            mMessage.clear()
        }
    }

}