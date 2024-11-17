package com.test.qianbailu.module.catalog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.test.qianbailu.R
import com.test.qianbailu.databinding.FragmentCatalogBinding
import com.test.qianbailu.model.bean.Catalog
import com.test.qianbailu.module.video.VideoActivity
import com.test.qianbailu.ui.adapter.MenuAdapter
import com.test.qianbailu.ui.adapter.VideoCoverAdapter
import com.test.qianbailu.utils.Utils
import org.koin.androidx.viewmodel.ext.android.viewModel
import top.cyixlq.core.common.fragment.CommonFragment
import top.cyixlq.core.utils.CLog
import top.cyixlq.core.utils.toastLong
import top.cyixlq.core.utils.toastShort

class CatalogFragment : CommonFragment<FragmentCatalogBinding>() {

    private val mViewModel by viewModel<CatalogViewModel>()

    private var page = 1
    private lateinit var videoCoverAdapter: VideoCoverAdapter
    private lateinit var menuAdapter: MenuAdapter
    private var lastSelectIndex = 0
    private var mCatalog: Catalog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        binds()
        mViewModel.getAllCatalog()
    }

    private fun initView() {
        val context = activity
        if (context != null) {
            mBinding.srl.setColorSchemeColors(context.getColor(R.color.colorPrimary))
        }
        mBinding.srl.setOnRefreshListener {
            val catalog = mCatalog
            if (catalog == null) {
                getString(R.string.no_catalog_info).toastShort()
            } else {
                page = 1
                mViewModel.getCatalogContent(catalog, page)
            }
        }
        menuAdapter = MenuAdapter()
        menuAdapter.setOnItemClickListener { _, _, position ->
            if (lastSelectIndex == position) return@setOnItemClickListener
            page = 1
            val catalog: Catalog = menuAdapter.getItem(position)
            mViewModel.getCatalogContent(catalog, page)
            videoCoverAdapter.setNewInstance(ArrayList())
            menuAdapter.setSelect(position)
            lastSelectIndex = position
        }
        mBinding.rvMenu.adapter = menuAdapter
        videoCoverAdapter = VideoCoverAdapter()
        videoCoverAdapter.loadMoreModule.setOnLoadMoreListener {
            val catalog = mCatalog
            if (catalog == null) {
                getString(R.string.no_catalog_info).toastShort()
            } else {
                page++
                mViewModel.getCatalogContent(catalog, page)
            }
        }
        videoCoverAdapter.setOnItemClickListener { _, _, position ->
            videoCoverAdapter.getItem(position).let {
                val parent = activity
                if (parent != null)
                    VideoActivity.launch(parent, it)
            }
        }
        mBinding.rvContent.adapter = videoCoverAdapter
    }

    private fun binds() {
        mViewModel.mViewState.observe(viewLifecycleOwner) {
            mBinding.srl.isRefreshing = it.isLoading
            if (it.allCatalog != null) {
                mCatalog = it.allCatalog.children.first()
                menuAdapter.setNewInstance(it.allCatalog.children)
            }
            if (it.videoCovers != null) {
                if (page <= 1) {
                    mBinding.rvContent.scrollToPosition(0)
                    videoCoverAdapter.setNewInstance(it.videoCovers.children)
                    if (page >= it.videoCovers.totalPage) {
                        videoCoverAdapter.loadMoreModule.loadMoreEnd()
                    }
                } else {
                    videoCoverAdapter.addData(it.videoCovers.children)
                    if (page >= it.videoCovers.totalPage) {
                        videoCoverAdapter.loadMoreModule.loadMoreEnd()
                    } else {
                        videoCoverAdapter.loadMoreModule.loadMoreComplete()
                    }
                }
            }
            if (it.throwable != null) {
                getString(R.string.load_error_pull_down_retry, Utils.parseNetworkErrorMessage(it.throwable)).toastLong()
                CLog.e("" + it.throwable.localizedMessage)
                if (page > 1) {
                    videoCoverAdapter.loadMoreModule.loadMoreFail()
                    page--
                }
            }
        }
    }

    companion object {
        fun instance(): CatalogFragment = CatalogFragment()
    }

    override val mViewBindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentCatalogBinding
        get() = FragmentCatalogBinding::inflate
}