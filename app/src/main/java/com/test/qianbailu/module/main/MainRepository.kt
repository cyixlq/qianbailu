package com.test.qianbailu.module.main

import cn.leancloud.AVObject
import cn.leancloud.AVQuery
import com.test.qianbailu.model.bean.UpdateAppBean
import io.reactivex.Observable

class MainDataSourceRepository(
    private val remote: MainRemoteDataSource
) {
    fun getVersionInfo(): Observable<UpdateAppBean> {
        return remote.getVersionInfo()
    }
}

class MainRemoteDataSource(private val avQuery: AVQuery<AVObject>) {
    fun getVersionInfo(): Observable<UpdateAppBean> {
        return avQuery.getInBackground("5e8808a6a5a0f500089dd349")
            .map {
                UpdateAppBean(
                    version = it.getString("name"),
                    code = it.getInt("code"),
                    url = it.getString("url"),
                    desc = it.getString("desc"),
                    must = it.getBoolean("must")
                )
            }
    }
}