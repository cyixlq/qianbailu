package com.test.qianbailu.module.video

import com.test.qianbailu.model.bean.Video

data class VideoViewState(
    val isLoading: Boolean = false,
    val throwable: Throwable? = null,
    val video: Video? = null
)