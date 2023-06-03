package com.test.qianbailu.module.video

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import cn.jzvd.JZDataSource
import cn.jzvd.Jzvd
import cn.jzvd.JzvdStd
import com.orhanobut.logger.Logger
import com.test.qianbailu.GlideApp
import com.test.qianbailu.R
import com.test.qianbailu.databinding.ActivityVideoBinding
import com.test.qianbailu.model.PARSE_TYPE_NONE
import com.test.qianbailu.model.PARSE_TYPE_WEB_VIEW_SCAN
import com.test.qianbailu.model.bean.VideoCover
import com.test.qianbailu.ui.widget.ScanWebView
import org.koin.androidx.viewmodel.ext.android.viewModel
import top.cyixlq.core.common.activity.CommonActivity
import top.cyixlq.core.utils.toastLong
import top.cyixlq.core.utils.toastShort

class VideoActivity : CommonActivity<ActivityVideoBinding>() {

    private val mViewModel by viewModel<VideoViewModel>()

    private var videoCover: VideoCover? = null
    private var mScanWebView: ScanWebView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        videoCover = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("videoCover", VideoCover::class.java)
        } else {
            intent.getParcelableExtra("videoCover")
        }
        if (videoCover == null || TextUtils.isEmpty(videoCover?.videoId)) {
            mBinding.videoPlayer.changeUiToError()
            "视频id有误".toastShort()
            return
        }
        binds()
        videoCover?.let {
            GlideApp.with(this).load(it.image)
                .placeholder(R.drawable.ic_loading)
                .fitCenter()
                .into(mBinding.videoPlayer.thumbImageView)
            mBinding.tvName.text = it.name
            mBinding.tvDuration.text = it.duration
            mBinding.tvViewCount.text = it.viewCount
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
        mScanWebView?.release()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (Jzvd.backPress()) {
            return
        }
        @Suppress("DEPRECATION")
        super.onBackPressed()
    }

    private fun binds() {
        mViewModel.viewState.observe(this, Observer {
            mBinding.progressBar.visibility = if (it.isLoading) View.VISIBLE else View.INVISIBLE
            if (it.video != null) {
                if (it.video.parseType == PARSE_TYPE_NONE) {
                    startVideo(it.video.url, videoCover?.name, null)
                } else if (it.video.parseType == PARSE_TYPE_WEB_VIEW_SCAN) {
                    mBinding.videoPlayer.setTip("资源扫描中(扫描完成自动播放)...")
                    if (mScanWebView == null) {
                        mScanWebView = ScanWebView(this)
                        mScanWebView?.setScanListener(object : ScanWebView.ScanListener {
                            override fun onScanResult(videoUrl: String, headers: HashMap<String, String>?) {
                                mBinding.videoPlayer.setTip("")
                                "视频解析成功".toastShort()
                                Logger.t(TAG).d("播放视频地址：$videoUrl")
                                startVideo(videoUrl, videoCover?.name, headers)
                            }
                            override fun onError(msg: String) {
                                msg.toastShort()
                            }

                        })
                    }
                    mScanWebView?.loadUrl(it.video.url)
                }
            }
            if (it.throwable != null) {
                it.throwable.localizedMessage?.toastLong()
            }
        })
    }

    private fun startVideo(url: String, name: String?, headers: HashMap<String, String>?) {
        Logger.t(TAG).d("startVideo -> headers: $headers")
        val jzDataSource = JZDataSource(url, name)
        if (headers != null) {
            jzDataSource.headerMap = headers
        }
        mBinding.videoPlayer.setUp(jzDataSource, JzvdStd.SCREEN_NORMAL)
        mBinding.videoPlayer.startVideo()
    }

    companion object {
        fun launch(activity: FragmentActivity, videoCover: VideoCover) {
            activity.startActivity(Intent(activity, VideoActivity::class.java).apply {
                putExtra("videoCover", videoCover)
            })
        }
        const val TAG = "VideoActivity"
    }

    override val mViewBindingInflater: (inflater: LayoutInflater) -> ActivityVideoBinding
        get() = ActivityVideoBinding::inflate
}
