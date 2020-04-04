package com.test.qianbailu.module.main

import com.test.qianbailu.model.bean.UpdateAppBean

data class MainViewState(
    val isLoading: Boolean = false,
    val throwable: Throwable? = null,
    val updateAppBean: UpdateAppBean? = null
)