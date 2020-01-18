package com.test.qianbailu.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.test.qianbailu.GlideApp
import com.test.qianbailu.R
import com.test.qianbailu.model.bean.VideoCover

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