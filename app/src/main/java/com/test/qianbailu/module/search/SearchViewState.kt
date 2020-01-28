package com.test.qianbailu.module.search

import com.test.qianbailu.model.bean.Counts
import com.test.qianbailu.model.bean.VideoCover

data class SearchViewState(
    val isLoading: Boolean = false,
    val throwable: Throwable? = null,
    val counts: Counts<VideoCover>? = null
)