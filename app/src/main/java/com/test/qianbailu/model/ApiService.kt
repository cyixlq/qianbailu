package com.test.qianbailu.model

import com.test.qianbailu.model.bean.UpdateAppBean
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("/")
    fun getIndexHtml(): Observable<ResponseBody>

    @GET("/video/{videoId}/")
    fun getVideoHtml(@Path("videoId") videoId: String): Observable<ResponseBody>

    @GET("https://raw.githubusercontent.com/cyixlq/qianbailu/master/version.json")
    fun getVersionInfo(): Observable<UpdateAppBean>
}