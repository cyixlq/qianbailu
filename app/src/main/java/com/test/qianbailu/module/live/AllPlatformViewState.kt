package com.test.qianbailu.module.live

import com.test.qianbailu.model.bean.Platforms

data class AllPlatformViewState(
    val isLoading: Boolean = false,
    val throwable: Throwable? = null,
    val platforms: Platforms? = null
)