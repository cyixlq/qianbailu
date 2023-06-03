package com.test.qianbailu.ui.widget

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import com.test.qianbailu.R

class TopBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attrs, defStyleAttr) {

    private val mContext = context
    private var rightDrawable: Drawable? = null
    private var title = ""
    private var rightText = ""
    private var titleColor = 0xffffffff.toInt()
    private var showBack = true

    private lateinit var tvTitle: TextView

    init {
        val attr = context.obtainStyledAttributes(attrs, R.styleable.TopBar)
        rightDrawable = attr.getDrawable(R.styleable.TopBar_rightImg)
        title = attr.getString(R.styleable.TopBar_title) ?: ""
        rightText = attr.getString(R.styleable.TopBar_rightTxt) ?: ""
        titleColor = attr.getColor(R.styleable.TopBar_titleColor, 0xffffffff.toInt())
        showBack = attr.getBoolean(R.styleable.TopBar_showBack, true)
        attr.recycle()
        initView()
    }

    @Suppress("DEPRECATION")
    private fun initView() {
        LayoutInflater.from(context).inflate(R.layout.layout_top_bar, this)
        tvTitle = findViewById(R.id.tvTitle)
        tvTitle.text = title
        tvTitle.setTextColor(titleColor)
        val tvRight: TextView = findViewById(R.id.tvRight)
        rightDrawable?.let {
            tvRight.setCompoundDrawables(rightDrawable, null, null, null)
        }
        tvRight.text = rightText
        val ivBack: ImageView = findViewById(R.id.ivBack)
        if (!showBack) {
            ivBack.visibility = View.INVISIBLE
        } else {
            ivBack.setOnClickListener {
                if (mContext is Activity) {
                    mContext.onBackPressed()
                }
            }
        }
    }

    fun setTitle(title: String) {
        tvTitle.text = title
    }
}