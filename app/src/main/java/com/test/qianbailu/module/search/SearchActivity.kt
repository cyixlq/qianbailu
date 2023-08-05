package com.test.qianbailu.module.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.test.qianbailu.R
import com.test.qianbailu.databinding.ActivitySearchBinding
import com.test.qianbailu.model.bean.SearchHistory
import com.test.qianbailu.ui.adapter.SearchHistoryAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import top.cyixlq.core.common.activity.CommonActivity
import top.cyixlq.core.utils.toastShort

class SearchActivity : CommonActivity<ActivitySearchBinding>() {

    private val mViewModel by viewModel<SearchViewModel>()

    // 搜索记录
    private lateinit var historyAdapter: SearchHistoryAdapter
    private lateinit var historyEmptyView: View

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
                val keyword = mBinding.edtSearch.text.trim().toString()
                startSearchResult(keyword)
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
        historyAdapter = SearchHistoryAdapter()
        historyAdapter.addChildClickViewIds(R.id.ivClear)
        historyAdapter.setOnItemChildClickListener { _, view, position ->
            if (view.id == R.id.ivClear) {
                val history = historyAdapter.getItem(position)
                mViewModel.deleteHistory(mutableListOf(history))
            }
        }
        historyAdapter.setOnItemClickListener { _, _, position ->
            val history = historyAdapter.getItem(position)
            val keyword = history.keyword
            mBinding.edtSearch.setText(keyword)
            startSearchResult(keyword)
        }
        historyEmptyView = LayoutInflater.from(this).inflate(R.layout.layout_empty, mBinding.rvSearchHistory, false)
        historyEmptyView.findViewById<TextView>(R.id.tvInfo).setText(R.string.no_history)
        val historyHeader = LayoutInflater.from(this).inflate(R.layout.layout_search_history_header, mBinding.rvSearchHistory, false)
        val ivDelete: ImageView = historyHeader.findViewById(R.id.ivDel)
        val tvDelAll: TextView = historyHeader.findViewById(R.id.tvDelAll)
        val tvDone: TextView = historyHeader.findViewById(R.id.tvDone)
        ivDelete.setOnClickListener {
            historyAdapter.showDeleteIcon(true)
            it.visibility = View.GONE
            tvDelAll.visibility = View.VISIBLE
            tvDone.visibility = View.VISIBLE
        }
        tvDone.setOnClickListener {
            historyAdapter.showDeleteIcon(false)
            it.visibility = View.GONE
            ivDelete.visibility = View.VISIBLE
            tvDelAll.visibility = View.GONE
        }
        tvDelAll.setOnClickListener {
            mViewModel.deleteHistory(historyAdapter.data)
            it.visibility = View.GONE
            ivDelete.visibility = View.VISIBLE
            tvDone.visibility = View.GONE
        }
        historyAdapter.setHeaderView(historyHeader)
        historyAdapter.headerWithEmptyEnable = true
        mBinding.rvSearchHistory.adapter = historyAdapter
        mBinding.ivBack.setOnClickListener { finish() }
        mBinding.tvAction.setOnClickListener {
            val keyword = mBinding.edtSearch.text.trim().toString()
            startSearchResult(keyword)
        }
    }

    private fun startSearchResult(keyword: String) {
        if (keyword.isEmpty()) {
            getString(R.string.keyword_not_empty).toastShort()
            return
        }
        val realHistory: SearchHistory = findSameSearchHistory(keyword) ?: SearchHistory(keyword)
        mViewModel.insertHistory(realHistory)
        SearchResultActivity.launchForResult(this, keyword)
    }

    // 从SearchHistoryAdapter中找到相同关键词的搜索记录
    private fun findSameSearchHistory(keyword: String): SearchHistory? {
        for (item in historyAdapter.data) {
            if (item.keyword == keyword) return item
        }
        return null
    }

    private fun binds() {
        mViewModel.observeAllHistory()
        mViewModel.mSearchHistoryViewState.observe(this) {
            if (it.histories != null) {
                if (it.histories.isEmpty()) historyAdapter.setEmptyView(historyEmptyView)
                historyAdapter.setNewInstance(it.histories)
            }
        }
    }

    @Suppress("DEPRECATION", "OVERRIDE_DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SearchResultActivity.REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                data ?: return
                val isNeedClear = data.getBooleanExtra(SearchResultActivity.RESULT_KEY_NEED_CLEAR, true)
                if (isNeedClear) {
                    mBinding.edtSearch.setText("")
                } else {
                    mBinding.edtSearch.requestFocus()
                    val keyword = mBinding.edtSearch.text
                    mBinding.edtSearch.setSelection(keyword.length)
                    mBinding.edtSearch.postDelayed({
                        val inputMethodManager = applicationContext
                            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        inputMethodManager.showSoftInput(mBinding.edtSearch, InputMethodManager.SHOW_IMPLICIT)
                    }, 300)
                }
            }
        }
    }

    companion object {
        fun launch(activity: FragmentActivity) {
            activity.startActivity(Intent(activity, SearchActivity::class.java))
        }
    }

    override val mViewBindingInflater: (inflater: LayoutInflater) -> ActivitySearchBinding
        get() = ActivitySearchBinding::inflate
}
