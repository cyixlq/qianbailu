package com.test.qianbailu.ui.adapter

import cn.jzvd.JZUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.test.qianbailu.GlideApp
import com.test.qianbailu.R
import com.test.qianbailu.model.bean.VideoCover

class VideoHistoryAdapter : BaseQuickAdapter<VideoCover, BaseViewHolder>(R.layout.item_video_history) {
    override fun convert(holder: BaseViewHolder, item: VideoCover) {
        holder.setText(R.id.tvName, item.name)
            .setText(
                R.id.tvPosition,
                holder.itemView.context.getString(R.string.seek_to, JZUtils.stringForTime(item.position))
            )
        GlideApp.with(holder.itemView).load(item.image)
            .placeholder(R.drawable.ic_loading)
            .into(holder.getView(R.id.ivThumb))
    }
}