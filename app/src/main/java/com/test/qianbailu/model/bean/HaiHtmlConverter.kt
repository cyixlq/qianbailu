package com.test.qianbailu.model.bean

import com.test.qianbailu.model.ApiService
import com.test.qianbailu.model.BASE_URL
import com.test.qianbailu.model.PARSE_TYPE_WEB_VIEW_SCAN
import com.test.qianbailu.model.TIME_OUT
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
        val res = api.getHtmlResponse(getHost())
        val html = res.execute().body()?.string()
        val doc = Jsoup.parse(html)
        val coversE = doc.select("li.p2.m1")
        val list = mutableListOf<VideoCover>()
        for (item in coversE) {
            val videoCover = VideoCover(
                item.select("span.lzbz > p.name").text(),
                item.selectFirst("img").attr("src"),
                item.select("a.link-hover").attr("href"),
                "",
                ""
            )
            list.add(videoCover)
        }
        return Counter(-1, list)
    }

    override fun getVideo(videoId: String): Video {
        val doc = Jsoup.parse(URL(BASE_URL + videoId), TIME_OUT)
        return Video(
            doc.selectFirst("h1#vod_name").text(),
            BASE_URL + doc.selectFirst("div.urlli.clearfix").selectFirst("a").attr("href"),
            doc.selectFirst("img#img_src").attr("src"),
            "",
            PARSE_TYPE_WEB_VIEW_SCAN
        )
    }

    override fun getCatalogList(): Counter<Catalog> {
        val doc = Jsoup.parse(URL(BASE_URL), TIME_OUT)
        val catalogs1 = doc.selectFirst("div#headers")
            .selectFirst("div.nav-down.clearfix")
            .selectFirst("ul")
            .select("li")
        val list = mutableListOf<Catalog>()
        for (element in catalogs1) {
            val name = element.selectFirst("a").text()
            val id = element.selectFirst("a").attr("href").replace("1.html", "")
            val catalog = Catalog(id, name)
            list.add(catalog)
        }
        val catalogs2 = doc.selectFirst("div.main > div.link-jingpin").select("a")
        for (element in catalogs2) {
            val name = element.selectFirst("a").text()
            val id = element.selectFirst("a").attr("href").replace("1.html", "")
            val catalog = Catalog(id, name)
            list.add(catalog)
        }
        val catalogs3 = doc.selectFirst("div.main > div.link-top").select("a")
        for (element in catalogs3) {
            val name = element.selectFirst("a").text()
            val id = element.selectFirst("a").attr("href").replace("1.html", "")
            val catalog = Catalog(id, name)
            list.add(catalog)
        }
        return Counter(-1, list)
    }

    override fun getVideoCoversByCatalog(catalogId: String, page: Int): Counter<VideoCover> {
        val doc = Jsoup.parse(URL("$BASE_URL$catalogId$page.html"), TIME_OUT)
        val videos = doc.selectFirst("div.index-area.clearfix")
            .selectFirst("ul").select("li")
        val list = mutableListOf<VideoCover>()
        for (element in videos) {
            val videoId = element.selectFirst("a.link-hover").attr("href")
            val img = element.selectFirst("img#img_src").attr("src")
            val name = element.selectFirst("p.name").text()
            val videoCover = VideoCover(name, img, videoId, "","")
            list.add(videoCover)
        }
        val lastPageElement = doc.selectFirst("div.born-page").select("a").last()
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
        val doc = Jsoup.parse(URL("$BASE_URL/search/$keyword-$page.html"), TIME_OUT)
        val videos = doc.selectFirst("div.index-area.clearfix")
            .selectFirst("ul").select("li")
        val list = mutableListOf<VideoCover>()
        for (element in videos) {
            val videoId = element.selectFirst("a.link-hover").attr("href")
            val img = element.selectFirst("img#img_src").attr("src")
            val name = element.selectFirst("p.name").text()
            val videoCover = VideoCover(name, img, videoId, "","")
            list.add(videoCover)
        }
        val lastPageElement = doc.selectFirst("div.born-page").select("a").last()
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
}