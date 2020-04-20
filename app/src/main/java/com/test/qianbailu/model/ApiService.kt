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

    @GET("/host/henhenlu.com")
    fun getHost(): Observable<ResponseBody>

    @GET
    fun getIndexHtml(@Url indexUrl: String): Observable<ResponseBody>

    @GET
    fun getVideoHtml(@Url videoPath: String): Observable<ResponseBody>

    @GET
    fun getAllCatalogHtml(@Url allCatalogUrl: String): Observable<ResponseBody>

    @GET
    fun getCatalogHtml(@Url catalogUrl: String, @Query("page") page: Int): Observable<ResponseBody>

    @GET
    fun searchVideo(@Url searchUrl: String, @Query("search_query") keyword: String,
                    @Query("page") page: Int): Observable<ResponseBody>

    @GET("http://www.xiongmaoapp.net:81/mf/json.txt")
    fun getAllLivePlatforms(): Observable<Platforms>

    @GET("http://www.xiongmaoapp.net:81/mf/{platformPath}")
    fun getLiveRooms(@Path("platformPath") platformPath: String): Observable<LiveRooms>
}