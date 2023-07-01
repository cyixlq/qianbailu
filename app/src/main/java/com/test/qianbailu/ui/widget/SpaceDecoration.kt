package com.test.qianbailu.ui.widget

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SpaceDecoration(private val space: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager) {
            val position = parent.getChildAdapterPosition(view)
            val columnIndex = position / layoutManager.spanCount
            val totalColumn = layoutManager.itemCount / layoutManager.spanCount +
                    if (layoutManager.itemCount % layoutManager.spanCount > 0) 1 else 0
            if (columnIndex >= totalColumn - 1) {
                // 最后一行
                outRect.top = space
                outRect.bottom = space
            } else {
                outRect.top = space
                outRect.bottom = 0
            }
            outRect.left = space
            outRect.right = 0
        } else if (layoutManager is LinearLayoutManager) {
            val position = parent.getChildAdapterPosition(view)
            // 是否是最后一个
            val isLastOne = position >= layoutManager.itemCount - 1
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