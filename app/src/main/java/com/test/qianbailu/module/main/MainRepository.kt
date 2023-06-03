package com.test.qianbailu.module.main

import com.test.qianbailu.model.ApiService
import com.test.qianbailu.model.bean.UpdateAppBean
import io.reactivex.Observable

class MainDataSourceRepository(
    private val remote: MainRemoteDataSource
) {
    fun getVersionInfo(): Observable<UpdateAppBean> {
        return remote.getVersionInfo()
    }
}

class MainRemoteDataSource(private val api: ApiService) {
    fun getVersionInfo(): Observable<UpdateAppBean> {
        return Observable.just(UpdateAppBean(
            version = "1.2.0",
            code = 120,
            url = "",
            desc = "",
            must = false
        ))
    }
}