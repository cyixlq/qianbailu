package com.test.qianbailu.module.catalog

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.test.qianbailu.R
import com.test.qianbailu.model.ALL_CATALOG_URL
import com.test.qianbailu.model.bean.Catalog
import com.test.qianbailu.module.search.SearchActivity
import com.test.qianbailu.module.video.VideoActivity
import com.test.qianbailu.ui.adapter.MenuAdapter
import com.test.qianbailu.ui.adapter.VideoCoverAdapter
import kotlinx.android.synthetic.main.fragment_catalog.*
import org.koin.androidx.scope.lifecycleScope
import org.koin.androidx.viewmodel.scope.viewModel
import top.cyixlq.core.common.fragment.CommonFragment
import top.cyixlq.core.utils.CLog
import top.cyixlq.core.utils.toastLong

class CatalogFragment : CommonFragment() {

    private val mViewModel by lifecycleScope.viewModel<CatalogViewModel>(this)

    override val layoutId: Int = R.layout.fragment_catalog
    private var page = 1
    private var catalogUrl = ALL_CATALOG_URL
    private lateinit var videoCoverAdapter: VideoCoverAdapter
    private lateinit var menuAdapter: MenuAdapter
    private var lastSelectIndex = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        binds()
        mViewModel.getAllCatalog()
    }

    private fun initView() {
        val context = activity
        if (context != null) {
            flSearchBar.setOnClickListener {
                SearchActivity.launch(context)
            }
            srl.setColorSchemeColors(context.getColor(R.color.colorPrimary))
        }
        srl.setOnRefreshListener { mViewModel.getAllCatalog() }
        menuAdapter = MenuAdapter()
        menuAdapter.setOnItemClickListener { _, _, position ->
            if (lastSelectIndex == position) return@setOnItemClickListener
            val catalog: Catalog? = menuAdapter.getItem(position)
            if (catalog != null) {
                page = 1
                catalogUrl = catalog.catalogUrl
                mViewModel.getCatalogContent(catalogUrl, page)
                catalog.isChecked = true
                menuAdapter.notifyItemChanged(position)
                if (lastSelectIndex >= 0) {
                    val oldCatalog: Catalog? = menuAdapter.getItem(lastSelectIndex)
                    if (oldCatalog != null) {
                        oldCatalog.isChecked = false
                        menuAdapter.notifyItemChanged(lastSelectIndex)
                    }
                }
                lastSelectIndex = position
            }
        }
        rvMenu.adapter = menuAdapter
        videoCoverAdapter = VideoCoverAdapter()
        //videoCoverAdapter.loadMoreModule?.isEnableLoadMoreIfNotFullPage = false
        videoCoverAdapter.loadMoreModule?.setOnLoadMoreListener {
            page++
            mViewModel.getCatalogContent(catalogUrl, page)
        }
        videoCoverAdapter.setOnItemClickListener { _, _, position ->
            videoCoverAdapter.getItem(position)?.let {
                val parent = activity
                if (parent != null)
                    VideoActivity.launch(parent, it)
            }
        }
        rvContent.adapter = videoCoverAdapter
    }

    private fun binds() {
        mViewModel.mViewState.observe(this, Observer {
            srl.isRefreshing = it.isLoading
            if (it.allCatalog != null) {
                it.allCatalog.catalogs[0].isChecked = true
                menuAdapter.setNewData(it.allCatalog.catalogs)
                videoCoverAdapter.setNewData(it.allCatalog.videocCovers)
            }
            if (it.videoCovers != null) {
                if (page <= 1) {
                    rvContent.scrollToPosition(0)
                    videoCoverAdapter.setNewData(it.videoCovers.list)
                    // 防止一页就加载完全部数据，而网站传下一页的话仍然会返回数据，所以在第一页的时候就判断是否超过总数，超过就要加载所有完成
                    if (videoCoverAdapter.itemCount >= it.videoCovers.count) {
                        videoCoverAdapter.loadMoreModule?.loadMoreEnd()
                    }
                } else {
                    videoCoverAdapter.addData(it.videoCovers.list)
                    if (videoCoverAdapter.itemCount >= it.videoCovers.count) {
                        videoCoverAdapter.loadMoreModule?.loadMoreEnd()
                    } else {
                        videoCoverAdapter.loadMoreModule?.loadMoreComplete()
                    }
                }
            }
            if (it.throwable != null) {
                "加载出了点小问题：${it.throwable.localizedMessage}".toastLong()
                CLog.e("" + it.throwable.localizedMessage)
                if (page > 1) {
                    videoCoverAdapter.loadMoreModule?.loadMoreFail()
                }
            }
        })
    }

    companion object {
        fun instance(): CatalogFragment = CatalogFragment()
    }
}