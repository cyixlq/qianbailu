package com.test.qianbailu.ui.widget

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LinearSpaceDecoration(private val space: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val childViewHolder: RecyclerView.ViewHolder = parent.getChildViewHolder(view)
        if (childViewHolder.itemViewType != 0) {
            outRect.set(0, 0, 0, 0)
            return
        }
        val layoutManager = parent.layoutManager
        if (layoutManager is LinearLayoutManager) {
            val position = parent.getChildAdapterPosition(view)
            // 是否是最后一个
            val isLastOne = position >= (parent.adapter?.itemCount ?: 1) - 1
            if (layoutManager.orientation == LinearLayoutManager.HORIZONTAL) {
                if (isLastOne) outRect.set(space, 0, space, 0)
                else outRect.set(space, 0, 0, 0)
            } else {
                if (isLastOne) outRect.set(0, space, 0, 0)
                else outRect.set(0, space, 0, space)
            }
        }
    }
}