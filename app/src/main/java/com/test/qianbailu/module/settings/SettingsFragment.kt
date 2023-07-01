package com.test.qianbailu.module.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.preference.PreferenceManager
import com.test.qianbailu.R
import com.test.qianbailu.databinding.FragmentSettingsBinding
import com.test.qianbailu.module.history.VideoHistoryActivity
import com.test.qianbailu.module.video.VideoActivity
import com.test.qianbailu.ui.adapter.VideoHistoryAdapter
import com.test.qianbailu.ui.widget.SpaceDecoration
import org.koin.androidx.viewmodel.ext.android.viewModel
import top.cyixlq.core.common.fragment.CommonFragment
import top.cyixlq.core.utils.toastShort

class SettingsFragment : CommonFragment<FragmentSettingsBinding>() {

    private val mViewModel by viewModel<SettingsViewModel>()
    private lateinit var mHistoryAdapter: VideoHistoryAdapter
    private lateinit var mDnsNameArray: Array<String>
    private lateinit var mDnsUrlArray: Array<String>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        bind()
        mViewModel.observeVideoHistoryFirstPage()
    }

    private fun initView() {
        mHistoryAdapter = VideoHistoryAdapter()
        mHistoryAdapter.setOnItemClickListener { _, _, position ->
            val item = mHistoryAdapter.getItem(position)
            VideoActivity.launch(requireActivity(), item)
        }
        mBinding.rvHistory.adapter = mHistoryAdapter
        mBinding.rvHistory.addItemDecoration(SpaceDecoration(resources.getDimensionPixelSize(R.dimen.dp_12)))
        mBinding.tvViewAll.setOnClickListener {
            VideoHistoryActivity.launch(requireActivity())
        }
        mDnsNameArray = resources.getStringArray(R.array.dns_names)
        mDnsUrlArray = resources.getStringArray(R.array.dns_urls)
        mBinding.dnsSettings.setSubTitle(mDnsNameArray[getDnsIndex()])
        mBinding.dnsSettings.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle(R.string.safe_dns)
                .setSingleChoiceItems(mDnsNameArray, getDnsIndex()) { dialog, which ->
                    val url = mDnsUrlArray[which]
                    PreferenceManager.getDefaultSharedPreferences(requireContext()).edit()
                        .putString(getString(R.string.key_dns_url), url).apply()
                    mBinding.dnsSettings.setSubTitle(mDnsNameArray[which])
                    getString(R.string.set_success_restart_takes_effect).toastShort()
                    dialog?.dismiss()
                }.setNegativeButton(R.string.cancel) { dialog, _ ->
                    dialog?.dismiss()
                }.show()
        }
    }

    private fun bind() {
        mViewModel.mViewState.observe(viewLifecycleOwner) {
            if (it.videoHistory != null) {
                mHistoryAdapter.setNewInstance(it.videoHistory)
                if (it.videoHistory.isEmpty()) {
                    mHistoryAdapter.setEmptyView(R.layout.layout_no_history)
                }
            }
        }
    }

    private fun getDnsIndex(): Int {
        val currentUrl = PreferenceManager.getDefaultSharedPreferences(requireContext()).getString(
            getString(R.string.key_dns_url),
            ""
        )
        if (currentUrl.isNullOrEmpty()) return 0
        mDnsUrlArray.forEachIndexed { index, url ->
            if (currentUrl == url) return index
        }
        return 0
    }

    companion object {
        fun instance() = SettingsFragment()
    }

    override val mViewBindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSettingsBinding
        get() = FragmentSettingsBinding::inflate
}