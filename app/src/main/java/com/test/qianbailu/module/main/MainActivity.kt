package com.test.qianbailu.module.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.test.qianbailu.R
import com.test.qianbailu.module.video.VideoActivity
import com.test.qianbailu.ui.adapter.VideoCoverAdapter
import kotlinx.android.synthetic.main.activity_main.*
import top.cyixlq.core.common.activity.CommonActivity
import top.cyixlq.core.utils.toastShort

class MainActivity : CommonActivity() {

    private val mViewModel by lazy {
        ViewModelProviders.of(this, MainViewModelFactory(MainDataSourceRepository()))
            .get(MainViewModel::class.java)
    }

    override val layoutId: Int = R.layout.activity_main

    private lateinit var infoText: TextView
    private lateinit var videoCoverAdapter: VideoCoverAdapter
    private lateinit var emptyView: View
    private var lastTime = 0L
    private val duration = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        initView()
        binds()
        refresh()
    }

    @SuppressLint("SetTextI18n")
    private fun binds() {
        mViewModel.viewState.observe(this, Observer {
            progressBar.visibility = if (it.isLoading) View.VISIBLE else View.INVISIBLE
            if (it.list != null) {
                if (it.list.isEmpty()) {
                    infoText.text = "空空如也，点击重试"
                    videoCoverAdapter.setEmptyView(emptyView)
                }
                videoCoverAdapter.setNewData(it.list)
            }
            if (it.throwable != null) {
                infoText.text = "发生错误：${it.throwable.localizedMessage}\n点击重试"
                videoCoverAdapter.setEmptyView(emptyView)
                videoCoverAdapter.setNewData(null)
            }
        })
    }

    private fun initView() {
        videoCoverAdapter = VideoCoverAdapter()
        videoCoverAdapter.setOnItemClickListener { _, _, position ->
            videoCoverAdapter.getItem(position)?.let {
                VideoActivity.launch(this, it)
            }
        }
        emptyView = LayoutInflater.from(this).inflate(R.layout.layout_empty, rvVideoCover, false)
        emptyView.setOnClickListener { refresh() }
        infoText = emptyView.findViewById(R.id.tvInfo)
        rvVideoCover.adapter = videoCoverAdapter

    }

    private fun refresh() {
        mViewModel.getIndexVideoCovers()
    }

    override fun onBackPressed() {
        val currentTime = System.currentTimeMillis()
        if ((currentTime - lastTime) > duration) {
            "再按一次退出".toastShort()
            lastTime = currentTime
        } else {
            super.onBackPressed()
        }
    }

    companion object {
        fun launch(activity: FragmentActivity) {
            activity.startActivity(Intent(activity, MainActivity::class.java))
        }
    }
}
