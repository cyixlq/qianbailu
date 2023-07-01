package com.test.qianbailu.ui.adapter

import android.annotation.SuppressLint
import androidx.appcompat.widget.AppCompatCheckBox
import cn.jzvd.JZUtils
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.test.qianbailu.GlideApp
import com.test.qianbailu.R
import com.test.qianbailu.model.PAGE_COUNT
import com.test.qianbailu.model.bean.VideoCover
import com.test.qianbailu.model.bean.VideoCover.CREATOR.TYPE_VIDEO_COVER
import com.test.qianbailu.model.bean.VideoCover.CREATOR.TYPE_VIEW_ALL
import kotlin.math.abs

class VideoHistoryAdapter : BaseQuickAdapter<VideoCover, BaseViewHolder>(R.layout.item_video_history),
    LoadMoreModule {

    private var mShowCheckBox = false
    private val mCheckedList = mutableListOf<VideoCover>()

    override fun convert(holder: BaseViewHolder, item: VideoCover) {
        holder.setText(R.id.tvName, item.name)
            .setGone(R.id.checkbox, !mShowCheckBox)
            .setText(
                R.id.tvPosition,
                if (abs(item.duration - item.position) < 4 * 1000)
                    holder.itemView.context.getString(R.string.finished_reading)
                else
                    holder.itemView.context.getString(
                        R.string.seek_to,
                        JZUtils.stringForTime(item.position)
                    )
            )
        val checkBox: AppCompatCheckBox = holder.getView(R.id.checkbox)
        checkBox.isChecked = mCheckedList.contains(item)
        GlideApp.with(holder.itemView).load(item.image)
            .placeholder(R.drawable.ic_loading)
            .into(holder.getView(R.id.ivThumb))
    }

    fun getCheckedList() = mCheckedList

    @SuppressLint("NotifyDataSetChanged")
    fun setShowCheckBox(isShow: Boolean) {
        mShowCheckBox = isShow
        notifyDataSetChanged()
    }
}