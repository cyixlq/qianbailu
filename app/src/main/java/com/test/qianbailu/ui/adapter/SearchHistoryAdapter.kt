package com.test.qianbailu.ui.adapter

import android.annotation.SuppressLint
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.test.qianbailu.R
import com.test.qianbailu.model.bean.SearchHistory

class SearchHistoryAdapter : BaseQuickAdapter<SearchHistory, BaseViewHolder>(R.layout.item_search_history) {

    private var mShowDeleteIcon = false

    override fun convert(holder: BaseViewHolder, item: SearchHistory) {
        holder.setText(R.id.tvKeyword, item.keyword)
            .setVisible(R.id.ivClear, mShowDeleteIcon)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun showDeleteIcon(isShow: Boolean) {
        if (isShow == mShowDeleteIcon) return
        mShowDeleteIcon = isShow
        notifyDataSetChanged()
    }
}