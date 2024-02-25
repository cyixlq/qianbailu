package com.test.qianbailu.utils

import android.content.Intent
import androidx.fragment.app.FragmentActivity
import com.test.qianbailu.R
import top.cyixlq.core.utils.toastShort

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
}