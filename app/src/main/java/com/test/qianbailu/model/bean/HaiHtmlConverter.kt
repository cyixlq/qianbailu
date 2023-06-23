package com.test.qianbailu.model.bean

import com.test.qianbailu.model.*
import org.jsoup.Jsoup
import java.net.URL
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
        val map = HashMap<String, VideoCover>()
        for (item in coversE) {
            val videoCover = VideoCover(
                item.select("span.lzbz > p.name").text(),
                item.selectFirst("img")?.attr("src") ?: "",
                item.select("a.link-hover").attr("href")
            )
            map[videoCover.videoId] = videoCover
        }
        val list = mutableListOf<VideoCover>()
        list.addAll(map.values)
        return Counter(-1, list)
    }

    override fun getVideo(videoId: String): Video {
        val doc = Jsoup.parse(getHtml(getHost() + videoId))
        return Video(
            doc.selectFirst("h1#vod_name")?.text() ?: UNKNOWN_VOD_NAME,
            getHost() + doc.selectFirst("div.urlli.clearfix")
                ?.selectFirst("a")
                ?.attr("href"),
            doc.selectFirst("img#img_src")?.attr("src") ?: "",
            "",
            PARSE_TYPE_WEB_VIEW_SCAN
        )
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

    override fun getPlayHeaders(): MutableMap<String, String>? = null

    private fun getHtml(url: String): String {
        return api.getHtmlResponse(url).execute().body()?.string() ?: ""
    }
}