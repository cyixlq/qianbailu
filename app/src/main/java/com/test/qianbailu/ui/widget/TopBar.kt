package com.test.qianbailu.ui.widget

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.test.qianbailu.R

class TopBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attrs, defStyleAttr) {

    private val ivBack: ImageView
    private val tvTitle: TextView
    private val tvRight: TextView

    init {
        val attr = context.obtainStyledAttributes(attrs, R.styleable.TopBar)
        val rightImgRes = attr.getResourceId(R.styleable.TopBar_rightImg, -1)
        val title = attr.getString(R.styleable.TopBar_title) ?: ""
        val rightText = attr.getString(R.styleable.TopBar_rightTxt) ?: ""
        val titleColor = attr.getColor(R.styleable.TopBar_titleColor, 0xffffffff.toInt())
        val showBack = attr.getBoolean(R.styleable.TopBar_showBack, true)
        attr.recycle()

        LayoutInflater.from(context).inflate(R.layout.layout_top_bar, this)
        tvTitle = findViewById(R.id.tvTitle)
        tvTitle.text = title
        tvTitle.setTextColor(titleColor)
        tvRight = findViewById(R.id.tvRight)
        if (rightImgRes != -1) {
            val drawable = ResourcesCompat.getDrawable(resources, rightImgRes, context.theme)
            drawable?.let {
                DrawableCompat.setTint(it, titleColor)
                it.setBounds(0, 0, it.minimumWidth, it.minimumHeight)
                tvRight.setCompoundDrawablesRelative(drawable, null, null, null)
            }
        }
        tvRight.text = rightText
        ivBack = findViewById(R.id.ivBack)
        ivBack.setOnClickListener {
            if (context is Activity) {
                @Suppress("DEPRECATION")
                context.onBackPressed()
            }
        }
        if (!showBack) {
            ivBack.visibility = View.INVISIBLE
        }
    }

    fun setRightImgClickListener(listener: OnClickListener) {
        tvRight.setOnClickListener(listener)
    }

    fun showBack(isShow: Boolean) {
        ivBack.visibility = if (isShow) VISIBLE else INVISIBLE
    }

    fun setTitle(title: String) {
        tvTitle.text = title
    }
}