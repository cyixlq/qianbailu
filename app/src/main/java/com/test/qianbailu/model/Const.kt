package com.test.qianbailu.model

const val BASE_URL = "https://www.haikouyo.com"
const val TIME_OUT = 10000 // 10s
const val CHROME_AGENT = "User-Agent: Mozilla/5.0 " +
        "(Macintosh; Intel Mac OS X 10_15_7) " +
        "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36"
const val PARSE_TYPE_NONE = 0 // 不需要解析
const val PARSE_TYPE_WEB_VIEW_SCAN = 1 // 需要WebView嗅探

const val TEN_MINUTE = 10 * 60 * 1000 // 10分钟