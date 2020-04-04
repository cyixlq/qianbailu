package com.test.qianbailu.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.test.qianbailu.GlideApp
import com.test.qianbailu.R
import com.test.qianbailu.model.bean.LiveRoom

class LiveRoomAdapter :
    BaseQuickAdapter<LiveRoom, BaseViewHolder>(R.layout.item_live_platform) {

    override fun convert(helper: BaseViewHolder, item: LiveRoom?) {
        if (item == null) return
        helper.setText(R.id.tvPlatformName, item.title)
            .setGone(R.id.tvOnlineCount, true)
        GlideApp.with(context).load(item.img).placeholder(R.mipmap.ic_launcher)
            .into(helper.getView(R.id.ivPlatformIcon))
    }
}