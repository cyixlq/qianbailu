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
        val coversE = doc.select("li.p2.m1")
        val addedVideos = mutableListOf<String>()
        val list = mutableListOf<VideoCover>()
        for (item in coversE) {
            val videoId = item.select("a.link-hover").attr("href")
            if (addedVideos.contains(videoId)) {
                continue
            }
            val videoCover = VideoCover(
                item.select("span.lzbz > p.name").text(),
                item.selectFirst("img")?.attr("src") ?: "",
                videoId
            )
            addedVideos.add(videoId)
            list.add(videoCover)
        }
        return Counter(-1, list)
    }

    override fun getVideo(videoId: String): Video {
        val doc = Jsoup.parse(getHtml(getHost() + videoId))
        val likes = mutableListOf<VideoCover>()
        val vodListE = doc.selectFirst("div#vods_list")?.select("li.p1.m1")
        vodListE?.forEach { item ->
            val videoCover = VideoCover(
                item.select("span.lzbz > p.name").text(),
                item.selectFirst("img")?.attr("src") ?: "",
                item.select("a.link-hover").attr("href")
            )
            likes.add(videoCover)
        }
        val url = getHost() + doc.selectFirst("div.urlli.clearfix")
            ?.selectFirst("a")
            ?.attr("href")
        /*val playDoc = Jsoup.parse(getHtml(url))
        val divPlayer = playDoc.selectFirst("div.player")
        val scriptE = divPlayer?.selectFirst("script")
        val scriptText = scriptE?.html()
        val parsePlayUrl = parsePlayUrl(scriptText)*/
        val playerHtml = getHtml(url)
        val parsePlayUrl = parsePlayUrl(playerHtml)
        val playUrl = parsePlayUrl.ifEmpty { url }
        return Video(
            doc.selectFirst("h1#vod_name")?.text() ?: UNKNOWN_VOD_NAME,
            playUrl,
            doc.selectFirst("img#img_src")?.attr("src") ?: "",
            "",
            if (parsePlayUrl.isEmpty()) PARSE_TYPE_WEB_VIEW_SCAN else PARSE_TYPE_NONE,
            likes
        )
    }

    private fun parsePlayUrl(scriptText: String?): String {
        if (scriptText.isNullOrEmpty())
            return ""
        else {
            val regex = "\"source\": \"(.*?)\", \""
            val pattern = Pattern.compile(regex)
            val matcher = pattern.matcher(scriptText)
            if (matcher.find()) {
                return matcher.group(1) ?: ""
            }
        }
        return ""
    }

    override fun getCatalogList(): Counter<Catalog> {
        val doc = Jsoup.parse(getHtml(getHost()))
        val catalogs1 = doc.selectFirst("div#headers")
            ?.selectFirst("div.nav-down.clearfix")
            ?.selectFirst("ul")
            ?.select("li")
        val list = mutableListOf<Catalog>()
        catalogs1?.forEach {
            val name = it.selectFirst("a")?.text() ?: UNKNOWN_CAT_NAME
            val id = it.selectFirst("a")
                ?.attr("href")
                ?.replace("1.html", "") ?: ""
            val catalog = Catalog(id, name)
            list.add(catalog)
        }
        val catalogs2 = doc.selectFirst("div.main > div.link-jingpin")?.select("a")
        catalogs2?.forEach {
            val name = it.selectFirst("a")?.text() ?: UNKNOWN_CAT_NAME
            val id = it.selectFirst("a")?.attr("href")
                ?.replace("1.html", "") ?: ""
            val catalog = Catalog(id, name)
            list.add(catalog)
        }
        val catalogs3 = doc.selectFirst("div.main > div.link-top")?.select("a")
        catalogs3?.forEach {
            val name = it.selectFirst("a")?.text() ?: UNKNOWN_CAT_NAME
            val id = it.selectFirst("a")?.attr("href")
                ?.replace("1.html", "") ?: ""
            val catalog = Catalog(id, name)
            list.add(catalog)
        }
        return Counter(-1, list)
    }

    override fun getVideoCoversByCatalog(catalogId: String, page: Int): Counter<VideoCover> {
        val doc = Jsoup.parse(getHtml("${getHost()}$catalogId$page.html"))
        val videos = doc.selectFirst("div.index-area.clearfix")
            ?.selectFirst("ul")?.select("li")
        val list = mutableListOf<VideoCover>()
        videos?.forEach {
            val videoId = it.selectFirst("a.link-hover")?.attr("href") ?: ""
            val img = it.selectFirst("img#img_src")?.attr("src") ?: ""
            val name = it.selectFirst("p.name")?.text() ?: UNKNOWN_VOD_NAME
            val videoCover = VideoCover(name, img, videoId)
            list.add(videoCover)
        }
        val lastPageElement = doc.selectFirst("div.born-page")?.select("a")?.last()
        var count = -1
        if (lastPageElement != null) {
            val lastHref = lastPageElement.attr("href")
            val totalPage = findPage(lastHref)
            if (totalPage > 0) {
                count = totalPage * list.size
            }
        }
        return Counter(count, list)
    }

    private fun findPage(href: String): Int {
        val p = Pattern.compile("[0-9]+(?=[^0-9]*$)")
        val m = p.matcher(href)
        if (m.find()) {
            return m.group().toIntOrNull() ?: -1
        }
        return -1
    }

    override fun search(keyword: String, page: Int): Counter<VideoCover> {
        val doc = Jsoup.parse(getHtml("${getHost()}/search/$keyword-$page.html"))
        val videos = doc.selectFirst("div.index-area.clearfix")
            ?.selectFirst("ul")?.select("li")
        val list = mutableListOf<VideoCover>()
        videos?.forEach {
            val videoId = it.selectFirst("a.link-hover")?.attr("href") ?: ""
            val img = it.selectFirst("img#img_src")?.attr("src") ?: ""
            val name = it.selectFirst("p.name")?.text() ?: UNKNOWN_VOD_NAME
            val videoCover = VideoCover(name, img, videoId)
            list.add(videoCover)
        }
        val lastPageElement = doc.selectFirst("div.born-page")?.select("a")?.last()
        var count = -1
        if (lastPageElement != null) {
            val lastHref = lastPageElement.attr("href")
            val totalPage = findPage(lastHref)
            if (totalPage > 0) {
                count = totalPage * list.size
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