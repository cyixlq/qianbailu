package com.test.qianbailu.model

const val PREF_NAME = "aiqiyi"
const val BASE_URL = "https://www.haikouyo.com"
const val CHROME_AGENT = "User-Agent: Mozilla/5.0 " +
        "(Macintosh; Intel Mac OS X 10_15_7) " +
        "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36"
const val PARSE_TYPE_NONE = 0 // 不需要解析
const val PARSE_TYPE_WEB_VIEW_SCAN = 1 // 需要WebView嗅探

const val  UNKNOWN_VOD_NAME = "未获取到影片名称"
const val UNKNOWN_CAT_NAME = "未获取到分类名称"

const val PAGE_COUNT = 12

// 屏幕滑动快进到底最多只有10分钟
const val MAX_STEP_DURATION = 10 * 60000L