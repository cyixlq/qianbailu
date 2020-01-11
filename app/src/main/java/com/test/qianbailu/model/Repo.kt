package com.test.qianbailu.model

import top.cyixlq.core.net.RetrofitManager

object Repo {

    lateinit var api: ApiService

    fun init() {
        api = RetrofitManager.getInstance().create(ApiService::class.java)
    }

}