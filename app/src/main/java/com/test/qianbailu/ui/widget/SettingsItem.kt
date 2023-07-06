package com.test.qianbailu.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.test.qianbailu.R

class SettingsItem @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private val imgEnd: ImageView
    private val tvTitle: TextView
    private val tvSubTitle: TextView
    private val tvTips: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_settings_item, this)
        setPadding(
            resources.getDimensionPixelSize(R.dimen.settings_item_padding_start),
            resources.getDimensionPixelSize(R.dimen.settings_item_padding_top),
            resources.getDimensionPixelSize(R.dimen.settings_item_padding_end),
            0
        )
        val attr = context.obtainStyledAttributes(attrs, R.styleable.SettingsItem)
        val endIconRes = attr.getResourceId(R.styleable.SettingsItem_endIcon, -1)
        val title = attr.getString(R.styleable.SettingsItem_primaryTitle) ?: ""
        val subTitle = attr.getString(R.styleable.SettingsItem_subtitle) ?: ""
        val tips = attr.getString(R.styleable.SettingsItem_tips) ?: ""
        attr.recycle()

        imgEnd = findViewById(R.id.ivEndIcon)
        tvTitle = findViewById(R.id.tvTitle)
        tvSubTitle = findViewById(R.id.tvSubTitle)
        tvTips = findViewById(R.id.tvTip)

        if (endIconRes != -1) {
            imgEnd.setImageResource(endIconRes)
        }
        tvTitle.text = title
        tvSubTitle.text = subTitle
        tvTips.text = tips
    }

    fun setTips(tips: String) {
        tvTips.text = tips
    }

    fun setSubTitle(subTitle: String) {
        tvSubTitle.text = subTitle
    }

}