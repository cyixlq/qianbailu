package com.test.qianbailu.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.test.qianbailu.GlideApp
import com.test.qianbailu.R
import com.test.qianbailu.model.bean.VideoCover

class VideoCoverAdapter : BaseQuickAdapter<VideoCover, BaseViewHolder>(R.layout.item_video_cover),
    LoadMoreModule {

    override fun convert(holder: BaseViewHolder, item: VideoCover) {
            holder.setText(R.id.tvName, item.name)
            GlideApp.with(context)
                .load(item.image)
                .placeholder(R.drawable.ic_loading)
                .into(holder.getView(R.id.ivCover))
    }
}