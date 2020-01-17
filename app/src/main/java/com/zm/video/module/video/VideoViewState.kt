package com.zm.video.module.video

import com.zm.video.model.bean.Video


data class VideoViewState(
    val isLoading: Boolean = false,
    val throwable: Throwable? = null,
    val video: Video? = null
)