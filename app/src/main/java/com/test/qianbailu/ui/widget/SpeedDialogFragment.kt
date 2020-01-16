package com.test.qianbailu.ui.widget

import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.test.qianbailu.R
import com.test.qianbailu.model.bean.VideoSpeedBean
import kotlinx.android.synthetic.main.dialog_video_speed.*
import top.cyixlq.core.utils.DisplayUtil

class SpeedDialogFragment : DialogFragment() {

    private var lastSpeedIndex = 1
    private var onSpeedSelectListener: OnSpeedSelectListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_video_speed, container)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.VideoSpeedDialog)
        val dialogWindow = dialog?.window
        if (dialogWindow != null) {
            dialogWindow.setGravity(Gravity.END)
            val lp = dialogWindow.attributes
            lp.width = DisplayUtil.dp2px(100f)
            lp.height = ViewGroup.LayoutParams.MATCH_PARENT
            dialogWindow.attributes = lp
        }
        val adapter = VideoSpeedItemAdapter()
        adapter.addChildClickViewIds(R.id.tvSpeed)
        adapter.setOnItemChildClickListener { _, view, position ->
            if (lastSpeedIndex == position) return@setOnItemChildClickListener
            if (view.id == R.id.tvSpeed) {
                val videoSpeedBean = adapter.getItem(position)
                if (videoSpeedBean != null) {
                    videoSpeedBean.isSelect = true
                    onSpeedSelectListener?.onSpeedSelect(videoSpeedBean.speed)
                    adapter.notifyItemChanged(position)
                }
                val lastVideoSpeedBean = adapter.getItem(lastSpeedIndex)
                if (lastVideoSpeedBean != null) {
                    lastVideoSpeedBean.isSelect = false
                    adapter.notifyItemChanged(lastSpeedIndex)
                }
            }
        }
        rvSpeed.adapter = adapter
        adapter.setNewData(arrayListOf(
            VideoSpeedBean(0.5f),
            VideoSpeedBean(1.0f, true),
            VideoSpeedBean(1.25f),
            VideoSpeedBean(1.5f),
            VideoSpeedBean(2.0f)
        ))
    }

    fun setOnSpeedSelectListener(listener: OnSpeedSelectListener) {
        this.onSpeedSelectListener = listener
    }

}

class VideoSpeedItemAdapter: BaseQuickAdapter<VideoSpeedBean, BaseViewHolder>(R.layout.item_video_speed) {

    override fun convert(helper: BaseViewHolder, item: VideoSpeedBean?) {
        if (item != null) {
            helper.setText(R.id.tvSpeed, "${item.speed}倍")
                .setTextColor(R.id.tvSpeed, if (item.isSelect) 0xff3399ff.toInt() else 0xfff3f3f3.toInt())
        }
    }

}

interface OnSpeedSelectListener {
    fun onSpeedSelect(speed: Float)
}