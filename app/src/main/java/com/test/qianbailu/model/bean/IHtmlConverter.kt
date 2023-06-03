package com.test.qianbailu.model.bean

interface IHtmlConverter {

    // 获取网站HOST
    fun getHost(): String
    // 获取首页视频封面数据
    fun getHomeVideoCovers(): Counter<VideoCover>
    // 获取视频详情数据
    fun getVideo(videoId: String): Video
    // 获取分类列表
    fun getCatalogList(): Counter<Catalog>
    // 获取分类下的视频封面数据
    fun getVideoCoversByCatalog(catalogId: String, page: Int): Counter<VideoCover>
    // 关键字搜索视频封面
    fun search(keyword: String, page: Int): Counter<VideoCover>
    // 获取播放器头信息
    fun getPlayHeaders(): MutableMap<String, String>?

}