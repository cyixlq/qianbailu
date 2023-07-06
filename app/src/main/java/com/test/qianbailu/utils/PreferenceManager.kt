package com.test.qianbailu.utils

import android.content.Context
import android.content.SharedPreferences
import com.test.qianbailu.MyApplication
import com.test.qianbailu.R
import com.test.qianbailu.model.PREF_NAME

object PreferenceManager {

    private fun getDefaultSharedPreferences(): SharedPreferences =
        MyApplication.INSTANCE.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun getDnsUrl(): String? {
        val key = MyApplication.INSTANCE.getString(R.string.key_dns_url)
        return getDefaultSharedPreferences().getString(key, "")
    }

    fun setDnsUrl(url: String) {
        val  key = MyApplication.INSTANCE.getString(R.string.key_dns_url)
        val editor = getDefaultSharedPreferences().edit()
        editor.putString(key, url)
        editor.apply()
    }

}