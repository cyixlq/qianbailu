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
import org.koin.androidx.viewmodel.ext.android.viewModel
import top.cyixlq.core.common.fragment.CommonFragment
import top.cyixlq.core.utils.CLog
import top.cyixlq.core.utils.toastLong

class CatalogFragment : CommonFragment<FragmentCatalogBinding>() {

    private val mViewModel by viewModel<CatalogViewModel>()

    private var page = 1
    private lateinit var videoCoverAdapter: VideoCoverAdapter
    private lateinit var menuAdapter: MenuAdapter
    private var lastSelectIndex = 0
    private var mCatalogId = ""

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
            page = 1
            mViewModel.getCatalogContent(mCatalogId, page)
        }
        menuAdapter = MenuAdapter()
        menuAdapter.setOnItemClickListener { _, _, position ->
            if (lastSelectIndex == position) return@setOnItemClickListener
            page = 1
            val catalog: Catalog = menuAdapter.getItem(position)
            mCatalogId = catalog.catalogUrl
            mViewModel.getCatalogContent(mCatalogId, page)
            videoCoverAdapter.setNewInstance(ArrayList())
            menuAdapter.setSelect(position)
            lastSelectIndex = position
        }
        mBinding.rvMenu.adapter = menuAdapter
        videoCoverAdapter = VideoCoverAdapter()
        videoCoverAdapter.loadMoreModule.setOnLoadMoreListener {
            page++
            mViewModel.getCatalogContent(mCatalogId, page)
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
                mCatalogId = it.allCatalog.children[0].catalogUrl
                menuAdapter.setNewInstance(it.allCatalog.children)
            }
            if (it.videoCovers != null) {
                if (page <= 1) {
                    mBinding.rvContent.scrollToPosition(0)
                    videoCoverAdapter.setNewInstance(it.videoCovers.children)
                    // 防止一页就加载完全部数据，而网站传下一页的话仍然会返回数据，所以在第一页的时候就判断是否超过总数，超过就要加载所有完成
                    if (videoCoverAdapter.itemCount >= it.videoCovers.count) {
                        videoCoverAdapter.loadMoreModule.loadMoreEnd()
                    }
                } else {
                    videoCoverAdapter.addData(it.videoCovers.children)
                    if (videoCoverAdapter.itemCount >= it.videoCovers.count) {
                        videoCoverAdapter.loadMoreModule.loadMoreEnd()
                    } else {
                        videoCoverAdapter.loadMoreModule.loadMoreComplete()
                    }
                }
            }
            if (it.throwable != null) {
                getString(R.string.load_error_pull_down_retry, it.throwable.localizedMessage).toastLong()
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