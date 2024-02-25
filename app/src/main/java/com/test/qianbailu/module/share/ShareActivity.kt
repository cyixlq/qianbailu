package com.test.qianbailu.module.share

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import com.test.qianbailu.R
import com.test.qianbailu.databinding.ActivityShareBinding
import com.test.qianbailu.model.bean.VideoCover
import com.test.qianbailu.module.video.VideoActivity
import com.test.qianbailu.utils.DesUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import top.cyixlq.core.common.activity.CommonActivity
import top.cyixlq.core.utils.FormatUtil.fromJson
import top.cyixlq.core.utils.toastShort

class ShareActivity : CommonActivity<ActivityShareBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowCompat.getInsetsController(window, window.decorView).let {
            it.hide(WindowInsetsCompat.Type.systemBars())
            it.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
        if (intent.action == Intent.ACTION_VIEW) {
            val uri = intent.data
            if(uri == null) {
                videoDetailError()
                return
            }
            if (uri.path == "/videoDetail") {
                val param = uri.getQueryParameter("data")
                if (param.isNullOrEmpty()) {
                    videoDetailError()
                    return
                }
                val json = DesUtil.decrypt(param.replace(" ", "+"))
                if (json.isEmpty()) {
                    videoDetailError()
                    return
                }
                val videoCover = json.fromJson<VideoCover>()
                if (videoCover == null) {
                    videoDetailError()
                    return
                }
                lifecycleScope.launch(Dispatchers.Main) {
                    delay(300)
                    VideoActivity.launch(this@ShareActivity, videoCover)
                    finish()
                }
            } else {
                shareContentError()
            }
        } else {
            shareContentError()
        }
    }

    private fun videoDetailError() {
        getString(R.string.video_detail_error).toastShort()
        finish()
    }

    private fun shareContentError() {
        getString(R.string.share_content_error).toastShort()
        finish()
    }

    override val mViewBindingInflater: (inflater: LayoutInflater) -> ActivityShareBinding
        get() = ActivityShareBinding::inflate
}