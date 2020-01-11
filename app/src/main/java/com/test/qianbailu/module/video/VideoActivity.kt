package com.test.qianbailu.module.video

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import cn.jzvd.Jzvd
import com.test.qianbailu.GlideApp
import com.test.qianbailu.R
import kotlinx.android.synthetic.main.activity_video.*
import top.cyixlq.core.common.activity.CommonActivity
import top.cyixlq.core.utils.toastLong
import top.cyixlq.core.utils.toastShort

class VideoActivity : CommonActivity() {

    private val mViewModel by lazy {
        ViewModelProviders.of(this, VideoViewModelFactory(VideoDataSourceRepository()))
            .get(VideoViewModel::class.java)
    }

    override val layoutId: Int = R.layout.activity_video
    private lateinit var name: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        val videoId = intent.getStringExtra("videoId")
        if (TextUtils.isEmpty(videoId)) {
            "视频id有误".toastShort()
            return
        }
        val imageUrl = intent.getStringExtra("cover")
        if (!TextUtils.isEmpty(imageUrl)) {
            videoPlayer.thumbImageView.scaleType = ImageView.ScaleType.FIT_CENTER
            GlideApp.with(this).load(imageUrl)
                .placeholder(R.drawable.ic_loading)
                .fitCenter()
                .into(videoPlayer.thumbImageView)
        }
        name = intent.getStringExtra("name") ?: "未知视频名称"
        binds()
        mViewModel.getVideo(videoId)
    }

    override fun onPause() {
        super.onPause()
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
                videoPlayer.setUp(it.video.url, name)
            }
            if (it.throwable != null) {
                it.throwable.localizedMessage.toastLong()
            }
        })
    }

    companion object {
        fun launch(activity: FragmentActivity, videoId: String, cover: String, name: String) {
            val intent = Intent(activity, VideoActivity::class.java)
            intent.putExtra("videoId", videoId)
            intent.putExtra("cover", cover)
            intent.putExtra("name", name)
            activity.startActivity(intent)
        }
    }
}
