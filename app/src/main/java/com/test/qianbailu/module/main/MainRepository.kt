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
        return api.getUpdateInfo()
    }
}