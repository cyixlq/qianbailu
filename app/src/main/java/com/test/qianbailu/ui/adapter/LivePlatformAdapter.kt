package com.test.qianbailu.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.test.qianbailu.GlideApp
import com.test.qianbailu.R
import com.test.qianbailu.model.bean.Platform

class LivePlatformAdapter :
    BaseQuickAdapter<Platform, BaseViewHolder>(R.layout.item_live_platform) {

    override fun convert(holder: BaseViewHolder, item: Platform) {
        holder.setText(R.id.tvOnlineCount, "在线主播：${item.number}")
            .setText(R.id.tvPlatformName, item.title)
        GlideApp.with(context).load(item.image).placeholder(R.mipmap.ic_launcher)
            .into(holder.getView(R.id.ivPlatformIcon))
    }
}