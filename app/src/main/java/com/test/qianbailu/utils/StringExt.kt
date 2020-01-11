package com.test.qianbailu.utils

import android.util.Log
import com.test.qianbailu.model.bean.Video
import com.test.qianbailu.model.bean.VideoCover
import org.jsoup.Jsoup

fun String.html2VideoCoverList(): MutableList<VideoCover> {
    val list = ArrayList<VideoCover>()
    val document = Jsoup.parse(this)
    val elements = document.select("div.col-sm-6.col-md-4.col-lg-4")
    for (element in elements) {
        val name = element.selectFirst("span.video-title.title-truncate.m-t-5").text()
        val aElement = element.selectFirst("div.well.well-sm > a")
        val thumbElement = aElement.selectFirst("div.thumb-overlay")
        val image = thumbElement.selectFirst("img.lazy.img-responsive").attr("data-original")
        val videoId = aElement.attr("href").replace("/video/", "").replace("/", "")
        Log.e("CY_TAG", "videoId:$videoId")
        val duration = thumbElement.selectFirst("div.duration").text()
        val viewCount = element.select("div.well.well-sm > div.video-views.pull-left").text()
        list.add(VideoCover(name, image, videoId, duration, viewCount))
    }
    return list
}

fun String.html2Video(): Video {
    val document = Jsoup.parse(this)
    //val name = document.selectFirst("h3.hidden-xs.big-title-truncate.m-t-0").text()
    val videoElement = document.selectFirst("video#video")
    val url = "https:${videoElement.selectFirst("source").attr("src")}"
    /*val cover = videoElement.attr("poster")
    val downloadUrl = "https:" + document
        .selectFirst("div.pull-right.m-t-15.m-r-5 > div.btn-group.open > ul.dropdown-menu > li > a")
        .attr("href")*/
    return Video("", url, "", "")
}