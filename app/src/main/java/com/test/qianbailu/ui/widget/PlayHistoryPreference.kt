package com.test.qianbailu.ui.widget

import android.content.Context
import android.util.AttributeSet
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import androidx.recyclerview.widget.RecyclerView
import com.test.qianbailu.R

class PlayHistoryPreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.preferenceStyle,
) : Preference(context, attrs, defStyleAttr) {

    private lateinit var mRecyclerView: RecyclerView

    init {
        widgetLayoutResource = R.layout.layout_play_history_preference
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)
        mRecyclerView = holder.findViewById(R.id.rvHistory) as RecyclerView
    }

}