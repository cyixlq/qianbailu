package top.cyixlq.core.utils

import android.os.Build
import androidx.annotation.RequiresApi
import top.cyixlq.core.CoreManager

object VersionUtil {

    fun getVersionName(): String {
        val packageManager = CoreManager.getApplication().packageManager
        val packageInfo = packageManager.getPackageInfo(CoreManager.getApplication().packageName, 0)
        return packageInfo.versionName
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun getLongVersionCode(): Long {
        val packageManager = CoreManager.getApplication().packageManager
        val packageInfo = packageManager.getPackageInfo(CoreManager.getApplication().packageName, 0)
        return packageInfo.longVersionCode
    }

    fun getVersionCode(): Int {
        val packageManager = CoreManager.getApplication().packageManager
        val packageInfo = packageManager.getPackageInfo(CoreManager.getApplication().packageName, 0)
        @Suppress("DEPRECATION")
        return packageInfo.versionCode
    }

}