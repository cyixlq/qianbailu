package com.test.qianbailu.module.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import com.test.qianbailu.R
import com.test.qianbailu.databinding.FragmentHomeBinding
import com.test.qianbailu.module.main.MainActivity
import com.test.qianbailu.module.video.VideoActivity
import com.test.qianbailu.ui.adapter.VideoCoverAdapter
import com.test.qianbailu.utils.Utils.parseNetworkErrorMessage
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import top.cyixlq.core.common.fragment.CommonFragment
import top.cyixlq.core.utils.CLog

class HomeFragment : CommonFragment<FragmentHomeBinding>() {

    private val mViewModel by viewModel<HomeViewModel>()

    private lateinit var emptyView: View
    private lateinit var infoText: TextView
    private val videoCoverAdapter: VideoCoverAdapter by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        binds()
        refresh()
    }

    private fun binds() {
        mViewModel.viewState.observe(viewLifecycleOwner, Observer {
            mBinding.srl.isRefreshing = it.isLoading
            if (it.list != null) {
                if (it.list.isEmpty()) {
                    infoText.setText(R.string.empty_and_click_retry)
                    videoCoverAdapter.setEmptyView(emptyView)
                }
                videoCoverAdapter.setNewInstance(it.list)
                ready()
            }
            if (it.throwable != null) {
                infoText.text = getString(R.string.error_and_click_retry, parseNetworkErrorMessage(it.throwable))
                videoCoverAdapter.setEmptyView(emptyView)
                videoCoverAdapter.setNewInstance(null)
                ready()
            }
        })
    }

    private fun ready() {
        val activity = requireActivity()
        if (activity is MainActivity && !activity.isReady()) {
            activity.ready()
            CLog.t(TAG).d("ready......")
        }
    }

    private fun initView() {
        mBinding.srl.setColorSchemeResources(R.color.colorPrimary)
        mBinding.srl.setOnRefreshListener { refresh() }
        videoCoverAdapter.setOnItemClickListener { _, _, position ->
            videoCoverAdapter.getItem(position).let {
                val parent = activity
                if (parent != null)
                    VideoActivity.launch(parent, it)
            }
        }
        emptyView = LayoutInflater.from(context).inflate(R.layout.layout_empty, mBinding.rvVideoCover, false)
        emptyView.setOnClickListener { refresh() }
        infoText = emptyView.findViewById(R.id.tvInfo)
        mBinding.rvVideoCover.adapter = videoCoverAdapter
    }

    private fun refresh() {
        mViewModel.getIndexVideoCovers()
    }

    companion object {
        fun instance(): HomeFragment = HomeFragment()
        private const val TAG = "HomeFragment"
    }

    override val mViewBindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentHomeBinding
        get() = FragmentHomeBinding::inflate
}