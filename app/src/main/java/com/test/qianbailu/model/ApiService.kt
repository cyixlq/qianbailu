package com.test.qianbailu.model

import com.test.qianbailu.model.bean.LiveRooms
import com.test.qianbailu.model.bean.Platforms
import com.test.qianbailu.model.bean.UpdateAppBean
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiService {

    @GET("/")
    fun getIndexHtml(): Observable<ResponseBody>

    @GET
    fun getVideoHtml(@Url videoId: String): Observable<ResponseBody>

    @GET(ALL_CATALOG_URL)
    fun getAllCatalogHtml(): Observable<ResponseBody>

    @GET
    fun getCatalogHtml(@Url catalogUrl: String, @Query("page") page: Int): Observable<ResponseBody>

    @GET("/search$ALL_CATALOG_URL")
    fun searchVideo(@Query("search_query") keyword: String, @Query("page") page: Int): Observable<ResponseBody>

    @GET("https://raw.githubusercontent.com/cyixlq/qianbailu/develop/version.json")
    fun getVersionInfo(): Observable<UpdateAppBean>

    @GET("http://www.xiongmaoapp.net:81/mf/json.txt")
    fun getAllLivePlatforms(): Observable<Platforms>

    @GET("http://www.xiongmaoapp.net:81/mf/{platformPath}")
    fun getLiveRooms(@Path("platformPath") platformPath: String): Observable<LiveRooms>
}