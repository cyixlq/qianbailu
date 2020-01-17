package com.zm.video.module.main

import com.zm.video.model.bean.UpdateAppBean
import com.zm.video.model.bean.VideoCover

data class MainViewState(
    val isLoading: Boolean = false,
    val throwable: Throwable? = null,
    val list: MutableList<VideoCover>? = null,
    val updateAppBean: UpdateAppBean? = null
)