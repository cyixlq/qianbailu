package com.test.qianbailu.module.search

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.test.qianbailu.R
import com.test.qianbailu.databinding.ActivitySearchBinding
import com.test.qianbailu.module.video.VideoActivity
import com.test.qianbailu.ui.adapter.VideoCoverAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import top.cyixlq.core.common.activity.CommonActivity
import top.cyixlq.core.utils.toastShort

class SearchActivity : CommonActivity<ActivitySearchBinding>() {

    private val mViewModel by viewModel<SearchViewModel>()

    private var keyword = ""
    private var page = 1
    private lateinit var videoCoverAdapter: VideoCoverAdapter
    private lateinit var emptyView: View
    private lateinit var infoText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        binds()
    }

    private fun initView() {
        mBinding.edtSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val inputMethodManager = applicationContext
                    .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager
                    .hideSoftInputFromWindow(currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
                keyword = mBinding.edtSearch.text.trim().toString()
                page = 1
                doSearch()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
        videoCoverAdapter = VideoCoverAdapter()
        videoCoverAdapter.loadMoreModule.setOnLoadMoreListener {
            page++
            doSearch()
        }
        videoCoverAdapter.setOnItemClickListener { _, _, position ->
            videoCoverAdapter.getItem(position).let {
                VideoActivity.launch(this, it)
            }
        }
        mBinding.rvVideoCover.adapter = videoCoverAdapter
        mBinding.srl.setColorSchemeColors(getColor(R.color.colorPrimary))
        mBinding.srl.setOnRefreshListener {
            page = 1
            doSearch()
        }
        emptyView = LayoutInflater.from(this).inflate(R.layout.layout_empty, mBinding.rvVideoCover, false)
        emptyView.setOnClickListener { doSearch() }
        infoText = emptyView.findViewById(R.id.tvInfo)
        infoText.text = "空空如也"
        videoCoverAdapter.setEmptyView(emptyView)
    }

    private fun doSearch() {
        if (keyword.isEmpty()) {
            "关键字不能为空".toastShort()
            return
        }
        mViewModel.searchVideo(keyword, page)
    }

    @SuppressLint("SetTextI18n")
    private fun binds() {
        mViewModel.mViewState.observe(this, Observer {
            mBinding.srl.isRefreshing = it.isLoading
            if (it.counts != null) {
                if (page <= 1) {
                    videoCoverAdapter.setNewInstance(it.counts.children)
                    // 防止一页就加载完全部数据，而网站传下一页的话仍然会返回数据，所以在第一页的时候就判断是否超过总数，超过就要加载所有完成
                    if (videoCoverAdapter.itemCount >= it.counts.count) {
                        videoCoverAdapter.loadMoreModule.loadMoreEnd()
                    }
                } else {
                    videoCoverAdapter.addData(it.counts.children)
                    if (videoCoverAdapter.itemCount >= it.counts.count) {
                        videoCoverAdapter.loadMoreModule.loadMoreEnd()
                    } else {
                        videoCoverAdapter.loadMoreModule.loadMoreComplete()
                    }
                }
                if (videoCoverAdapter.itemCount - (if (videoCoverAdapter.hasEmptyView()) 1 else 0) <= 0) {
                    infoText.text = "没有结果，点击重试"
                    videoCoverAdapter.setEmptyView(emptyView)
                }
            }
            if (it.throwable != null) {
                infoText.text = "发生错误：${it.throwable.localizedMessage}\n点击重试"
                videoCoverAdapter.setEmptyView(emptyView)
                videoCoverAdapter.setNewInstance(null)
            }
        })
    }

    companion object {
        fun launch(activity: FragmentActivity) {
            activity.startActivity(Intent(activity, SearchActivity::class.java))
        }
    }

    override val mViewBindingInflater: (inflater: LayoutInflater) -> ActivitySearchBinding
        get() = ActivitySearchBinding::inflate
}
