package com.test.qianbailu.module.search

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.test.qianbailu.R
import com.test.qianbailu.databinding.ActivitySearchResultBinding
import com.test.qianbailu.module.video.VideoActivity
import com.test.qianbailu.ui.adapter.VideoCoverAdapter
import com.test.qianbailu.utils.Utils
import org.koin.androidx.viewmodel.ext.android.viewModel
import top.cyixlq.core.common.activity.CommonActivity
import top.cyixlq.core.utils.toastShort

class SearchResultActivity : CommonActivity<ActivitySearchResultBinding>() {

    private var keyword = ""
    private var page = 1
    // 搜索结果
    private lateinit var videoCoverAdapter: VideoCoverAdapter
    private lateinit var emptyView: View
    private lateinit var infoText: TextView

    private val mViewModel by viewModel<SearchViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, 0)
        keyword = intent.getStringExtra("keyword") ?: ""
        if (keyword.isEmpty()) {
            getString(R.string.keyword_not_empty)
            return
        }
        initView()
        binds()
        doSearch()
    }

    private fun initView() {
        mBinding.edtSearch.setText(keyword)
        val needClearClick = View.OnClickListener {
            setResultAndFinish(isNeedClear = true)
        }
        mBinding.ivBack.setOnClickListener(needClearClick)
        mBinding.ivClear.setOnClickListener(needClearClick)
        mBinding.edtSearch.setOnClickListener {
            setResultAndFinish(isNeedClear = false)
        }
        mBinding.tvAction.setOnClickListener(needClearClick)
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
        emptyView = LayoutInflater.from(this).inflate(R.layout.layout_empty, mBinding.rvVideoCover, false)
        emptyView.setOnClickListener { doSearch() }
        infoText = emptyView.findViewById(R.id.tvInfo)
    }

    private fun setResultAndFinish(isNeedClear: Boolean) {
        val intent = Intent()
        intent.putExtra(RESULT_KEY_NEED_CLEAR, isNeedClear)
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun binds() {
        mViewModel.mSearchResultViewState.observe(this) {
            mBinding.progressBar.visibility = if (it.isLoading) View.VISIBLE else View.GONE
            if (it.counts != null) {
                if (page <= 1) {
                    videoCoverAdapter.setNewInstance(it.counts.children)
                    // 防止一页就加载完全部数据，而网站传下一页的话仍然会返回数据，所以在第一页的时候就判断是否超过总数，超过就要加载所有完成
                    if (page >= it.counts.totalPage) {
                        videoCoverAdapter.loadMoreModule.loadMoreEnd()
                    }
                } else {
                    videoCoverAdapter.addData(it.counts.children)
                    if (page >= it.counts.totalPage) {
                        videoCoverAdapter.loadMoreModule.loadMoreEnd()
                    } else {
                        videoCoverAdapter.loadMoreModule.loadMoreComplete()
                    }
                }
                if (videoCoverAdapter.itemCount - (if (videoCoverAdapter.hasEmptyView()) 1 else 0) <= 0) {
                    infoText.setText(R.string.no_result_and_click_retry)
                    videoCoverAdapter.setEmptyView(emptyView)
                }
                if (it.throwable != null) {
                    infoText.text =
                        getString(R.string.error_and_click_retry, Utils.parseNetworkErrorMessage(it.throwable))
                    videoCoverAdapter.setEmptyView(emptyView)
                    videoCoverAdapter.setNewInstance(null)
                }
            }
        }
    }

    private fun doSearch() {
        if (keyword.isEmpty()) {
            getString(R.string.keyword_not_empty).toastShort()
            return
        }
        mViewModel.searchVideo(keyword, page)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0)
    }

    companion object {
        private const val PARAM_KEY = "keyword"
        const val RESULT_KEY_NEED_CLEAR = "result_key_need_clear"
        const val REQUEST_CODE = 112
        @Suppress("DEPRECATION")
        fun launchForResult(activity: CommonActivity<*>, keyword: String) {
            activity.startActivityForResult(Intent(activity, SearchResultActivity::class.java).apply {
                putExtra(PARAM_KEY, keyword)
            }, REQUEST_CODE)
        }
    }

    override val mViewBindingInflater: (inflater: LayoutInflater) -> ActivitySearchResultBinding
        get() = ActivitySearchResultBinding::inflate
}