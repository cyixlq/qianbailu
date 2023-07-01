package com.test.qianbailu.module.settings

import com.test.qianbailu.model.bean.VideoCover

data class SettingsViewState(
    val count: Int = 0,
    val videoHistory: MutableList<VideoCover>? = null
)