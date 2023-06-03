package com.test.qianbailu.ui.adapter

import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.test.qianbailu.R
import com.test.qianbailu.model.bean.Catalog

class MenuAdapter : BaseQuickAdapter<Catalog, BaseViewHolder>(R.layout.item_menu) {

    private var mSelect = 0
    private var mLastSelect = mSelect

    override fun convert(holder: BaseViewHolder, item: Catalog) {
        val flParent: FrameLayout = holder.getView(R.id.flParent)
        val tvName: TextView = holder.getView(R.id.tvName)
        val line: View = holder.getView(R.id.line)
        if (holder.absoluteAdapterPosition == mSelect) {
            flParent.setBackgroundColor(0xfff3f3f3.toInt())
            tvName.setTextColor(context.getColor(R.color.colorPrimary))
            line.visibility = View.VISIBLE
        } else {
            flParent.setBackgroundColor(0xffffffff.toInt())
            tvName.setTextColor(0xff000000.toInt())
            line.visibility = View.INVISIBLE
        }
        tvName.text = item.name
    }

    fun setSelect(index: Int) {
        if (index == mSelect) return
        mSelect = index
        notifyItemChanged(mSelect)
        notifyItemChanged(mLastSelect)
        mLastSelect = mSelect
    }
}