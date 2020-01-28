package com.test.qianbailu.module.home

import com.test.qianbailu.model.bean.VideoCover

data class HomeViewState(
    val isLoading: Boolean = false,
    val throwable: Throwable? = null,
    val list: MutableList<VideoCover>? = null
)