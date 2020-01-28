package com.test.qianbailu.utils

import com.test.qianbailu.model.bean.Catalog
import com.test.qianbailu.model.bean.Counts
import com.test.qianbailu.model.bean.Video
import com.test.qianbailu.model.bean.VideoCover
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

fun String.html2VideoCoverList(doc: Document? = null): MutableList<VideoCover> {
    val list = ArrayList<VideoCover>()
    val document = doc ?: Jsoup.parse(this)
    val elements = document.select("div.col-sm-6.col-md-4.col-lg-4")
    for (element in elements) {
        val aElement = element.selectFirst("div.well.well-sm > a")
        val thumbElement = aElement.selectFirst("div.thumb-overlay")
        val durationElement = thumbElement.selectFirst("div.duration") ?: continue
        val name = element.selectFirst("span.video-title.title-truncate.m-t-5").text()
        var image = thumbElement.selectFirst("img.lazy.img-responsive")?.attr("data-original")
        if (image == null) {
            image = thumbElement.selectFirst("img.img-responsive")?.attr("src")
        }
        if (image == null) {
            image = ""
        }
        val videoId = aElement.attr("href")
        val viewCount = element.selectFirst("div.video-views.pull-left").text()
        val duration = durationElement.text()
        list.add(VideoCover(name, image, videoId, duration, viewCount))
    }
    return list
}

fun String.html2CatalogList(): MutableList<Catalog> {
    val list = ArrayList<Catalog>()
    val document = Jsoup.parse(this)
    val divElement = document.selectFirst("div.list-group")
    val aElements = divElement.select("a")
    for (a in aElements) {
        val name = a.text()
        val catalogUrl = a.attr("href")
        list.add(Catalog(catalogUrl, name))
    }
    return list
}

fun String.html2VideoCoverCounts(): Counts<VideoCover> {
    var count = 0
    val document = Jsoup.parse(this)
    val divElement = document.selectFirst("div.col-md-9.col-sm-8 > div.well.well-sm")
    val spanElements = divElement.select("span.text-white")
    if (spanElements != null && spanElements.size > 1) {
        spanElements.forEachIndexed { index, element ->
            if (index == spanElements.size - 1) {
                count = element.text().toInt()
            }
        }
    }
    val list = this.html2VideoCoverList(document)
    return Counts(count, list)
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