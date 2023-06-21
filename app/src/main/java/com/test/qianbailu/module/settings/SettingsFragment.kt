package com.test.qianbailu.module.settings

import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import com.test.qianbailu.R
import top.cyixlq.core.utils.toastShort

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences_settings, rootKey)
        val dnsPreference = findPreference<ListPreference>(getString(R.string.key_dns_url))
        dnsPreference?.setOnPreferenceChangeListener { _, _ ->
            getString(R.string.set_success_restart_takes_effect).toastShort()
            true
        }
    }

    companion object {
        fun instance() = SettingsFragment()
    }
}