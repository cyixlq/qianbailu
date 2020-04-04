package com.test.qianbailu.module.live

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.test.qianbailu.R
import com.test.qianbailu.module.live.room.LiveRoomsActivity
import com.test.qianbailu.ui.adapter.LivePlatformAdapter
import kotlinx.android.synthetic.main.fragment_all_platform.*
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.androidx.scope.lifecycleScope
import org.koin.androidx.viewmodel.scope.viewModel
import top.cyixlq.core.common.fragment.CommonFragment

class AllPlatformFragment : CommonFragment() {


    private val mViewModel by lifecycleScope.viewModel<AllPlatformViewModel>(this)
    private val mAdapter by lifecycleScope.inject<LivePlatformAdapter>()

    override val layoutId: Int = R.layout.fragment_all_platform
    private lateinit var emptyView: View
    private lateinit var infoText: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        refresh()
        initView()
        binds()
    }

    private fun initView() {
        slRefresh.setColorSchemeResources(R.color.colorPrimary)
        slRefresh.setOnRefreshListener { refresh() }
        mAdapter.setOnItemClickListener { _, _, position ->
            val platform = mAdapter.getItem(position)
            val act = activity
            if (platform != null && act != null) {
                LiveRoomsActivity.launch(act, platform.address, platform.title)
            }
        }
        rvPlatforms.layoutManager = GridLayoutManager(context, 3)
        rvPlatforms.adapter = mAdapter
        emptyView = LayoutInflater.from(context).inflate(R.layout.layout_empty, rvVideoCover, false)
        emptyView.setOnClickListener { refresh() }
        infoText = emptyView.findViewById(R.id.tvInfo)
    }

    private fun refresh() {
        mViewModel.getAllLivePlatforms()
    }

    @SuppressLint("SetTextI18n")
    private fun binds() {
        mViewModel.viewState.observe(viewLifecycleOwner, Observer {
            slRefresh.isRefreshing = it.isLoading
            if (it.throwable != null) {
                mAdapter.setEmptyView(emptyView)
                infoText.text = "出错了：${it.throwable.localizedMessage}\n点击重试"
                mAdapter.setNewData(null)
            }
            if (it.platforms != null) {
                if (it.platforms.platformList.isEmpty()) {
                    infoText.text = "空空如也\n点击重试"
                    mAdapter.setEmptyView(emptyView)
                }
                mAdapter.setNewData(it.platforms.platformList)
            }
        })
    }

    companion object {
        fun instance(): AllPlatformFragment = AllPlatformFragment()
    }
}
