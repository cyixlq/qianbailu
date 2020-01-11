package top.cyixlq.core.utils

import android.annotation.SuppressLint
import android.content.Context
import top.cyixlq.core.CoreManager

object DisplayUtil {

    /**
     * 获取状态栏高度
     */
    @SuppressLint("PrivateApi")
    fun getStatusBarHeight(): Int {
        var statusBarHeight = 0
        try {
            val c = Class.forName("com.android.internal.R\$dimen")
            val o = c.newInstance()
            val field = c.getField("status_bar_height")
            val x = field.get(o) as Int
            statusBarHeight = CoreManager.getApplication().resources.getDimensionPixelSize(x)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return statusBarHeight
    }

    /**
     *  获取除去状态栏后屏幕的高度
     */
    fun getScreenHeightExcludeStatusbar(context: Context): Int {
        return context.resources.displayMetrics.heightPixels - getStatusBarHeight()
    }

    fun px2dp(pxValue: Float): Int {
        val density = CoreManager.getApplication().resources.displayMetrics.density
        return (pxValue / density + 0.5f).toInt()
    }

    fun dp2px(dpValue: Float): Int {
        val density = CoreManager.getApplication().resources.displayMetrics.density
        return (dpValue * density + 0.5f).toInt()
    }

    fun px2sp(pxValue: Float): Int {
        val scaleDensity = CoreManager.getApplication().resources.displayMetrics.scaledDensity
        return (pxValue / scaleDensity + 0.5f).toInt()
    }

    fun sp2px(context: Context, spValue: Float): Int {
        val scaleDensity = context.resources.displayMetrics.scaledDensity
        return (spValue * scaleDensity + 0.5f).toInt()
    }
}
