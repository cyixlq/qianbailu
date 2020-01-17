package com.zm.video.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zm.video.GlideApp
import com.zm.video.R
import com.zm.video.model.bean.VideoCover

class VideoCoverAdapter : BaseQuickAdapter<VideoCover, BaseViewHolder>(R.layout.item_video_cover) {

    override fun convert(helper: BaseViewHolder, item: VideoCover?) {
        item?.let {
            helper.setText(R.id.tvName, it.name)
                .setText(R.id.tvViewCount, it.viewCount)
                .setText(R.id.tvDuration, it.duration)
            GlideApp.with(context)
                .load(item.image)
                .placeholder(R.drawable.ic_loading)
                .into(helper.getView(R.id.ivCover))
        }
    }
}