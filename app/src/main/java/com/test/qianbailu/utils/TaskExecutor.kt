package com.test.qianbailu.utils

import android.os.Handler
import android.os.Looper

object TaskExecutor {

    private val mMainHandler = Handler(Looper.getMainLooper())

    fun executeMain(runnable: Runnable) {
        mMainHandler.post(runnable)
    }
}