package com.test.qianbailu.ui.adapter

import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.test.qianbailu.R
import com.test.qianbailu.model.bean.LiveRoom

class LiveRoomAdapter :
    BaseQuickAdapter<LiveRoom, BaseViewHolder>(R.layout.item_live_platform) {

    override fun convert(holder: BaseViewHolder, item: LiveRoom) {
        holder.setText(R.id.tvPlatformName, item.title)
            .setGone(R.id.tvOnlineCount, true)
        Glide.with(context).load(item.img).placeholder(R.mipmap.ic_launcher)
            .into(holder.getView(R.id.ivPlatformIcon))
    }
}