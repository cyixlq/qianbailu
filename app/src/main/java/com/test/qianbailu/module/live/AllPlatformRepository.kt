package com.test.qianbailu.module.live

import com.test.qianbailu.model.ApiService
import com.test.qianbailu.model.bean.Platforms
import io.reactivex.Observable

class AllPlatformDataSourceRepository(
    private val remote: AllPlatformRemoteDataSource
) {
    fun getAllLivePlatforms(): Observable<Platforms> {
        return remote.getAllLivePlatforms()
    }
}

class AllPlatformRemoteDataSource(private val api: ApiService) {

    fun getAllLivePlatforms(): Observable<Platforms> {
        return api.getAllLivePlatforms()
    }

}