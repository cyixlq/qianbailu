package com.test.qianbailu.model.bean

import com.test.qianbailu.model.ApiService
import com.test.qianbailu.model.BASE_URL
import com.test.qianbailu.model.PARSE_TYPE_NONE
import com.test.qianbailu.model.PARSE_TYPE_WEB_VIEW_SCAN
import com.test.qianbailu.model.UNKNOWN_CAT_NAME
import com.test.qianbailu.model.UNKNOWN_VOD_NAME
import org.jsoup.Jsoup
import java.util.regex.Pattern


/**
 *  还口哟网站Html转换器
 */
class HaiHtmlConverter(private val api: ApiService) : IHtmlConverter {
    override fun getHost(): String {
        return BASE_URL
    }

    override fun getHomeVideoCovers(): Counter<VideoCover> {
        val doc = Jsoup.parse(getHtml(getHost()))
        val coversE = doc.select("li.col-md-6.col-sm-4.col-xs-3")
        val addedVideos = mutableListOf<String>()
        val list = mutableListOf<VideoCover>()
        for (item in coversE) {
            val elementA = item.select("a.stui-vodlist__thumb.lazyload")
            val videoId = elementA.attr("href")
            if (addedVideos.contains(videoId)) {
                continue
            }
            val videoCover = VideoCover(
                elementA.attr("title"),
                elementA.attr("data-original") ?: "",
                videoId
            )
            addedVideos.add(videoId)
            list.add(videoCover)
        }
        return Counter(-1, list)
    }

    override fun getVideo(videoCover: VideoCover): Video {
        val detailUri = if (videoCover.videoId.startsWith("http")) videoCover.videoId
        else getHost() + videoCover.videoId
        val doc = Jsoup.parse(getHtml(detailUri))
        val likes = mutableListOf<VideoCover>()
        val vodListE = doc.selectFirst("ul.stui-vodlist__bd.clearfix")
            ?.select("a.stui-vodlist__thumb.lazyload")
        vodListE?.forEach { item ->
            val itemVideoCover = VideoCover(
                item.attr("title"),
                item.attr("data-original") ?: "",
                item.attr("href")
            )
            likes.add(itemVideoCover)
        }
        val playerUrl = doc.selectFirst("ul.stui-content__playlist.clearfix")
            ?.selectFirst("a")
            ?.attr("href") ?: ""
        val url = if (!playerUrl.startsWith("http")) {
            getHost() + playerUrl
        } else {
            playerUrl
        }
        /*val playDoc = Jsoup.parse(getHtml(url))
        val divPlayer = playDoc.selectFirst("div.player")
        val scriptE = divPlayer?.selectFirst("script")
        val scriptText = scriptE?.html()
        val parsePlayUrl = parsePlayUrl(scriptText)*/
        val playerHtml = getHtml(url)
        val playerDoc = Jsoup.parse(playerHtml)
        val playerDiv = playerDoc.select("div.stui-player__video.clearfix")
        val scriptE = playerDiv.select("script")
        val parsePlayUrl = parsePlayUrl(scriptE.html())
        val playUrl = parsePlayUrl.ifEmpty { url }
        return Video(
            doc.selectFirst("h1.title")?.text() ?: UNKNOWN_VOD_NAME,
            playUrl,
            videoCover.image,
            "",
            if (parsePlayUrl.isEmpty()) PARSE_TYPE_WEB_VIEW_SCAN else PARSE_TYPE_NONE,
            likes
        )
    }

    private fun parsePlayUrl(scriptText: String?): String {
        if (scriptText.isNullOrEmpty())
            return ""
        else {
            val regex = "thisUrl = \"(.*?)\";"
            val pattern = Pattern.compile(regex)
            val matcher = pattern.matcher(scriptText)
            if (matcher.find()) {
                return matcher.group(1) ?: ""
            }
        }
        return ""
    }

    override fun getCatalogList(): Counter<Catalog> {
        val doc = Jsoup.parse(getHtml(getHost() + "/category.html"))
        val catalogs = doc.selectFirst("ul.stui-screen__list.type-slide.bottom-line-dot.clearfix")
            ?.select("a.text-muted")
        val list = mutableListOf<Catalog>()
        catalogs?.forEach {
            val name = it.text() ?: UNKNOWN_CAT_NAME
            val id = it.attr("href")
            val catalog = Catalog(id, name)
            list.add(catalog)
        }
        return Counter(-1, list)
    }

    override fun getVideoCoversByCatalog(catalog: Catalog, page: Int): Counter<VideoCover> {
        val url = catalog.catalogUrl.replace(".html", "/time-$page.html")
        val doc = Jsoup.parse(getHtml(url))
        val ul = doc.selectFirst("ul.stui-vodlist.clearfix")
        val lis = ul?.select("li.col-md-6.col-sm-4.col-xs-3")
        val list = mutableListOf<VideoCover>()
        lis?.forEach {
            val elementA = it.selectFirst("a.stui-vodlist__thumb.lazyload")
            val videoId = elementA?.attr("href") ?: ""
            val img = elementA?.attr("data-original") ?: ""
            val name = elementA?.attr("title") ?: UNKNOWN_VOD_NAME
            val videoCover = VideoCover(name, img, videoId)
            list.add(videoCover)
        }
        val lastPageElement = doc.selectFirst("input#number")
        var count = -1
        if (lastPageElement != null) {
            val totalPage = lastPageElement.attr("max").toIntOrNull()
            if (totalPage != null && totalPage > 0) {
                count = totalPage
            }
        }
        return Counter(count, list)
    }

    private fun findPage(href: String?): Int {
        if (href.isNullOrEmpty()) {
            return -1
        }
        val p = Pattern.compile("[0-9]+(?=[^0-9]*$)")
        val m = p.matcher(href)
        if (m.find()) {
            return m.group().toIntOrNull() ?: -1
        }
        return -1
    }

    override fun search(keyword: String, page: Int): Counter<VideoCover> {
        //https://www.haikouyo.com/search/丝袜/time-2.html
        val doc = Jsoup.parse(getHtml("${getHost()}/search/$keyword/time-$page.html"))
        val videos = doc.selectFirst("ul.stui-vodlist.clearfix")?.select("li")?.select("a.stui-vodlist__thumb.lazyload")
        val list = mutableListOf<VideoCover>()
        videos?.forEach {
            val videoId = it.attr("href") ?: ""
            val img = it.attr("data-original") ?: ""
            val name = it.attr("title") ?: UNKNOWN_VOD_NAME
            val videoCover = VideoCover(name, img, videoId)
            list.add(videoCover)
        }
        val lastPageElement = doc.selectFirst("ul.stui-page.text-center.clearfix")?.select("li")?.last()?.selectFirst("a")
        var count = -1
        if (lastPageElement != null) {
            val lastHref = lastPageElement.attr("href")
            val totalPage = findPage(lastHref)
            if (totalPage > 0) {
                count = totalPage
            }
        }
        return Counter(count, list)
    }

    override fun getPlayHeaders(video: Video): HashMap<String, String> = hashMapOf(
        "User-Agent" to "Mozilla/5.0 (Linux; Android 6.0.1; MuMu Build/V417IR; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/66.0.3359.158 Mobile Safari/537.36",
        "Referer" to video.url,
        "Origin" to getHost()
    )

    private fun getHtml(url: String): String {
        return api.getHtmlResponse(url).execute().body()?.string() ?: ""
    }
}