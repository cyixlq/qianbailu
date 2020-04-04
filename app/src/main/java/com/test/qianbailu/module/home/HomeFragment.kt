package com.test.qianbailu.module.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import com.test.qianbailu.R
import com.test.qianbailu.module.video.VideoActivity
import com.test.qianbailu.ui.adapter.VideoCoverAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.android.ext.android.inject
import org.koin.androidx.scope.lifecycleScope
import org.koin.androidx.viewmodel.scope.viewModel
import top.cyixlq.core.common.fragment.CommonFragment

class HomeFragment : CommonFragment() {

    private val mViewModel by lifecycleScope.viewModel<HomeViewModel>(this)

    override val layoutId: Int = R.layout.fragment_home

    private lateinit var emptyView: View
    private lateinit var infoText: TextView
    private val videoCoverAdapter: VideoCoverAdapter by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        binds()
        refresh()
    }

    @SuppressLint("SetTextI18n")
    private fun binds() {
        mViewModel.viewState.observe(viewLifecycleOwner, Observer {
            srl.isRefreshing = it.isLoading
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
        srl.setColorSchemeResources(R.color.colorPrimary)
        srl.setOnRefreshListener { refresh() }
        videoCoverAdapter.setOnItemClickListener { _, _, position ->
            videoCoverAdapter.getItem(position)?.let {
                val parent = activity
                if (parent != null)
                    VideoActivity.launch(parent, it)
            }
        }
        emptyView = LayoutInflater.from(context).inflate(R.layout.layout_empty, rvVideoCover, false)
        emptyView.setOnClickListener { refresh() }
        infoText = emptyView.findViewById(R.id.tvInfo)
        rvVideoCover.adapter = videoCoverAdapter
    }

    private fun refresh() {
        mViewModel.getIndexVideoCovers()
    }

    companion object {
        fun instance(): HomeFragment = HomeFragment()
    }
}