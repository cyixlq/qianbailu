package com.test.qianbailu.utils

import com.test.qianbailu.model.ApiService
import com.test.qianbailu.model.bean.HaiHtmlConverter
import com.test.qianbailu.model.bean.IHtmlConverter

object HtmlConverterFactory {

    private var mHtmlConverter: IHtmlConverter? = null

    fun get(api: ApiService): IHtmlConverter {
        var converter = mHtmlConverter
        if (converter == null) {
            converter = HaiHtmlConverter(api)
            mHtmlConverter = converter
        }
        return converter
    }

    fun set(converter: IHtmlConverter) {
        this.mHtmlConverter = converter
    }
}