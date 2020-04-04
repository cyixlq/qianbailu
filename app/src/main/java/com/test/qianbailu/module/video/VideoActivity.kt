package com.test.qianbailu.module.video

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import cn.jzvd.Jzvd
import cn.jzvd.JzvdStd
import com.test.qianbailu.GlideApp
import com.test.qianbailu.R
import com.test.qianbailu.model.bean.VideoCover
import com.test.qianbailu.ui.widget.JZMediaIjk
import kotlinx.android.synthetic.main.activity_video.*
import org.koin.androidx.scope.lifecycleScope
import org.koin.androidx.viewmodel.scope.viewModel
import top.cyixlq.core.common.activity.CommonActivity
import top.cyixlq.core.utils.toastLong
import top.cyixlq.core.utils.toastShort

class VideoActivity : CommonActivity() {

    private val mViewModel by lifecycleScope.viewModel<VideoViewModel>(this)

    override val layoutId: Int = R.layout.activity_video
    private var videoCover: VideoCover? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        videoCover = intent.getParcelableExtra("videoCover")
        if (videoCover == null || TextUtils.isEmpty(videoCover?.videoId)) {
            videoPlayer.changeUiToError()
            "视频id有误".toastShort()
            return
        }
        binds()
        videoCover?.let {
            GlideApp.with(this).load(it.image)
                .placeholder(R.drawable.ic_loading)
                .fitCenter()
                .into(videoPlayer.thumbImageView)
            tvName.text = it.name
            tvDuration.text = it.duration
            tvViewCount.text = it.viewCount
            mViewModel.getVideo(it.videoId)
        }
    }

    override fun onPause() {
        super.onPause()
        JzvdStd.goOnPlayOnPause()
    }

    override fun onResume() {
        super.onResume()
        JzvdStd.goOnPlayOnResume()
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

    private fun binds() {
        mViewModel.viewState.observe(this, Observer {
            progressBar.visibility = if (it.isLoading) View.VISIBLE else View.INVISIBLE
            if (it.video != null) {
                videoPlayer.setUp(it.video.url, videoCover?.name)
                videoPlayer.startVideo()
            }
            if (it.throwable != null) {
                it.throwable.localizedMessage?.toastLong()
            }
        })
    }

    companion object {
        fun launch(activity: FragmentActivity, videoCover: VideoCover) {
            activity.startActivity(Intent(activity, VideoActivity::class.java).apply {
                putExtra("videoCover", videoCover)
            })
        }
    }
}
