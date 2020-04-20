package com.test.qianbailu.module.live.play

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import cn.jzvd.Jzvd
import com.test.qianbailu.R
import com.test.qianbailu.ui.widget.JZMediaIjk
import kotlinx.android.synthetic.main.activity_live_play.*
import top.cyixlq.core.common.activity.CommonActivity
import top.cyixlq.core.utils.CLog
import top.cyixlq.core.utils.toastShort

class LivePlayActivity : CommonActivity() {

    override val layoutId: Int = R.layout.activity_live_play

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        player.setShowNormalTitle(true)
        player.setShowNormalBack(true)
        player.showSpeed(false)
        val url = intent.getStringExtra("address") ?: ""
        val title = intent.getStringExtra("title") ?: "未知主播名称"
        if (url.isEmpty()) {
            "直播地址无效".toastShort()
            return
        }
        CLog.d("live url ->$url")
        player.setUp(url, title, Jzvd.SCREEN_NORMAL, JZMediaIjk::class.java)
        player.startVideo()
    }

    override fun onDestroy() {
        super.onDestroy()
        Jzvd.releaseAllVideos()
    }

    override fun onBackPressed() {
        if (Jzvd.backPress()) {
            return
        }
        super.onBackPressed()
    }

    companion object {
        fun launch(activity: FragmentActivity, title: String, address: String) {
            activity.startActivity(Intent(activity, LivePlayActivity::class.java)
                .putExtra("title", title)
                .putExtra("address", address))
        }
    }
}
