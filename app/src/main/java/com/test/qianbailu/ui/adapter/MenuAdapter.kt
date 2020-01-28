package com.test.qianbailu.ui.adapter

import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.test.qianbailu.R
import com.test.qianbailu.model.bean.Catalog

class MenuAdapter : BaseQuickAdapter<Catalog, BaseViewHolder>(R.layout.item_menu) {

    override fun convert(helper: BaseViewHolder, item: Catalog?) {
        if (item == null) return
        val flParent: FrameLayout = helper.getView(R.id.flParent)
        val tvName: TextView = helper.getView(R.id.tvName)
        val line: View = helper.getView(R.id.line)
        if (item.isChecked) {
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
}