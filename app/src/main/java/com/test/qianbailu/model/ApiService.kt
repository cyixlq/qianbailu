package com.test.qianbailu.model

import com.test.qianbailu.model.bean.LiveRooms
import com.test.qianbailu.model.bean.Platforms
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @Headers(CHROME_AGENT)
    @GET
    fun getHtmlResponse(@Url url: String): Call<ResponseBody>

    @Headers(CHROME_AGENT)
    @GET
    fun getHtmlResponseByParams(@Url url: String, @QueryMap params: Map<String, String>): ResponseBody

    @GET("http://www.xiongmaoapp.net:81/mf/json.txt")
    fun getAllLivePlatforms(): Observable<Platforms>

    @GET("http://www.xiongmaoapp.net:81/mf/{platformPath}")
    fun getLiveRooms(@Path("platformPath") platformPath: String): Observable<LiveRooms>
}