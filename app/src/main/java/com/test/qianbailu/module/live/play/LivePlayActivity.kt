package com.test.qianbailu.module.live.play

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.FragmentActivity
import cn.jzvd.Jzvd
import com.test.qianbailu.databinding.ActivityLivePlayBinding
import com.test.qianbailu.ui.widget.JZMediaIjk
import top.cyixlq.core.common.activity.CommonActivity
import top.cyixlq.core.utils.CLog
import top.cyixlq.core.utils.toastShort

class LivePlayActivity : CommonActivity<ActivityLivePlayBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding.player.setShowNormalTitle(true)
        mBinding.player.setShowNormalBack(true)
        mBinding.player.showSpeed(false)
        val url = intent.getStringExtra("address") ?: ""
        val title = intent.getStringExtra("title") ?: "未知主播名称"
        if (url.isEmpty()) {
            "直播地址无效".toastShort()
            return
        }
        CLog.d("live url ->$url")
        mBinding.player.setUp(url, title, Jzvd.SCREEN_NORMAL, JZMediaIjk::class.java)
        mBinding.player.startVideo()
    }

    override fun onDestroy() {
        super.onDestroy()
        Jzvd.releaseAllVideos()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (Jzvd.backPress()) {
            return
        }
        @Suppress("DEPRECATION")
        super.onBackPressed()
    }

    companion object {
        fun launch(activity: FragmentActivity, title: String, address: String) {
            activity.startActivity(Intent(activity, LivePlayActivity::class.java)
                .putExtra("title", title)
                .putExtra("address", address))
        }
    }

    override val mViewBindingInflater: (inflater: LayoutInflater) -> ActivityLivePlayBinding
        get() = ActivityLivePlayBinding::inflate
}
