package com.test.qianbailu.module.live

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.test.qianbailu.R
import com.test.qianbailu.databinding.FragmentAllPlatformBinding
import com.test.qianbailu.module.live.room.LiveRoomsActivity
import com.test.qianbailu.ui.adapter.LivePlatformAdapter
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import top.cyixlq.core.common.fragment.CommonFragment

class AllPlatformFragment : CommonFragment<FragmentAllPlatformBinding>() {


    private val mViewModel by viewModel<AllPlatformViewModel>()
    private val mAdapter by inject<LivePlatformAdapter>()

    private lateinit var emptyView: View
    private lateinit var infoText: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        refresh()
        initView()
        binds()
    }

    private fun initView() {
        mBinding.slRefresh.setColorSchemeResources(R.color.colorPrimary)
        mBinding.slRefresh.setOnRefreshListener { refresh() }
        mAdapter.setOnItemClickListener { _, _, position ->
            val platform = mAdapter.getItem(position)
            LiveRoomsActivity.launch(requireActivity(), platform.address, platform.title)
        }
        mBinding.rvPlatforms.layoutManager = GridLayoutManager(context, 3)
        mBinding.rvPlatforms.adapter = mAdapter
        emptyView = LayoutInflater.from(context).inflate(R.layout.layout_empty, mBinding.rvPlatforms, false)
        emptyView.setOnClickListener { refresh() }
        infoText = emptyView.findViewById(R.id.tvInfo)
    }

    private fun refresh() {
        mViewModel.getAllLivePlatforms()
    }

    @SuppressLint("SetTextI18n")
    private fun binds() {
        mViewModel.viewState.observe(viewLifecycleOwner, Observer {
            mBinding.slRefresh.isRefreshing = it.isLoading
            if (it.throwable != null) {
                mAdapter.setEmptyView(emptyView)
                infoText.text = "出错了：${it.throwable.localizedMessage}\n点击重试"
                mAdapter.setNewInstance(null)
            }
            if (it.platforms != null) {
                if (it.platforms.platformList.isEmpty()) {
                    infoText.text = "空空如也\n点击重试"
                    mAdapter.setEmptyView(emptyView)
                }
                mAdapter.setNewInstance(it.platforms.platformList)
            }
        })
    }

    companion object {
        fun instance(): AllPlatformFragment = AllPlatformFragment()
    }

    override val mViewBindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentAllPlatformBinding
        get() = FragmentAllPlatformBinding::inflate
}
