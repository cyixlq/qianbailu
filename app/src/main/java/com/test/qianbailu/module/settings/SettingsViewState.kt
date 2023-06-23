package com.test.qianbailu.module.settings

import com.test.qianbailu.model.bean.VideoCover

data class SettingsViewState(
    val videoHistory: MutableList<VideoCover>? = null
)