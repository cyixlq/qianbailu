package com.test.qianbailu.module.main

import com.test.qianbailu.model.Repo
import com.test.qianbailu.model.bean.UpdateAppBean
import io.reactivex.Observable

class MainDataSourceRepository(
    private val remote: MainRemoteDataSource = MainRemoteDataSource()
) {
    fun getVersionInfo(): Observable<UpdateAppBean> {
        return remote.getVersionInfo()
    }
}

class MainRemoteDataSource {
    fun getVersionInfo(): Observable<UpdateAppBean> {
        return Repo.api
            .getVersionInfo()
    }
}