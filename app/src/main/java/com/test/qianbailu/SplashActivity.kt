package com.test.qianbailu

import android.os.Bundle
import com.test.qianbailu.module.main.MainActivity
import com.uber.autodispose.autoDisposable
import io.reactivex.Observable
import top.cyixlq.core.common.activity.AutoDisposeActivity
import top.cyixlq.core.utils.RxSchedulers
import java.util.concurrent.TimeUnit

class SplashActivity : AutoDisposeActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Observable.timer(2, TimeUnit.SECONDS)
            .subscribeOn(RxSchedulers.io)
            .observeOn(RxSchedulers.ui)
            .autoDisposable(scopeProvider)
            .subscribe {
                MainActivity.launch(this)
                finish()
            }
    }
}
