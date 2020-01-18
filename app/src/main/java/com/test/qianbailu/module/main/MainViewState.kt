package com.test.qianbailu.module.main

import com.test.qianbailu.model.bean.UpdateAppBean
import com.test.qianbailu.model.bean.VideoCover

data class MainViewState(
    val isLoading: Boolean = false,
    val throwable: Throwable? = null,
    val list: MutableList<VideoCover>? = null,
    val updateAppBean: UpdateAppBean? = null
)