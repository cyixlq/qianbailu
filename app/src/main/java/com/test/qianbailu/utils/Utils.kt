package com.test.qianbailu.utils

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.fragment.app.FragmentActivity
import com.test.qianbailu.MyApplication
import com.test.qianbailu.R
import top.cyixlq.core.utils.toastShort
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object Utils {

    fun shareText(activity: FragmentActivity, content: String) {
        val intent = Intent()
        intent.setAction(Intent.ACTION_SEND)
        intent.setType("text/plain")
        intent.putExtra(Intent.EXTRA_TEXT, content)
        val sendIntent = Intent.createChooser(intent, activity.getString(R.string.share_to))
        if (sendIntent == null) {
            activity.getString(R.string.no_apps_found_to_share).toastShort()
        } else {
            activity.startActivity(sendIntent)
        }
    }

    fun parseNetworkErrorMessage(t: Throwable): String {
        val msgId = when (t) {
            is SocketTimeoutException -> { R.string.socket_time_out }
            is UnknownHostException, is ConnectException -> {
                if (!isNetWorkConnected()) R.string.no_network else R.string.server_unresponsive
            }
            else -> { R.string.unknown_error }
        }
        return MyApplication.INSTANCE.getString(msgId)
    }

    fun isNetWorkConnected(): Boolean {
        val connectivityManager = MyApplication.INSTANCE.getSystemService(ConnectivityManager::class.java)
        if (connectivityManager != null) {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            }
        }
        return false
    }
}