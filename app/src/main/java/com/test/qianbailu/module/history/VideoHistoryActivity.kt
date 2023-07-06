package com.test.qianbailu.module.history

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.test.qianbailu.R
import com.test.qianbailu.databinding.ActivityVideoHistoryBinding
import com.test.qianbailu.module.settings.SettingsViewModel
import com.test.qianbailu.module.video.VideoActivity
import com.test.qianbailu.ui.adapter.VideoHistoryAdapter
import com.test.qianbailu.ui.widget.SpaceDecoration
import org.koin.androidx.viewmodel.ext.android.viewModel
import top.cyixlq.core.common.activity.CommonActivity
import top.cyixlq.core.utils.DisplayUtil

class VideoHistoryActivity : CommonActivity<ActivityVideoHistoryBinding>() {

    companion object {
        fun launch(activity: FragmentActivity) {
            activity.startActivity(Intent(activity, VideoHistoryActivity::class.java))
        }
    }

    private var mPage = 1
    private val mViewModel: SettingsViewModel by viewModel()
    private lateinit var mHistoryAdapter: VideoHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        bind()
        mViewModel.getVideoHistoryByPage(mPage)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initView() {
        mHistoryAdapter = VideoHistoryAdapter()
        mHistoryAdapter.addChildClickViewIds(R.id.checkbox, R.id.ivThumb)
        mHistoryAdapter.setOnItemChildClickListener { _, view, position ->
            when (view.id) {
                R.id.ivThumb -> {
                    VideoActivity.launch(
                        this@VideoHistoryActivity,
                        mHistoryAdapter.getItem(position)
                    )
                }
                R.id.checkbox -> {
                    if (view is AppCompatCheckBox) {
                        val item = mHistoryAdapter.getItem(position)
                        if (view.isChecked)
                            mHistoryAdapter.getCheckedList().add(item)
                        else
                            mHistoryAdapter.getCheckedList().remove(item)
                        mHistoryAdapter.notifyItemChanged(position)
                    }
                }
            }
        }
        mHistoryAdapter.loadMoreModule.setOnLoadMoreListener {
            mPage++
            mViewModel.getVideoHistoryByPage(mPage)
        }
        mBinding.rvHistory.adapter = mHistoryAdapter
        val layoutManager = mBinding.rvHistory.layoutManager
        if (layoutManager is GridLayoutManager) {
            val spanCount = layoutManager.spanCount
            val screenWidth = DisplayUtil.getScreenWidth(this)
            val itemWidth = resources.getDimensionPixelSize(R.dimen.item_video_history_width)
            val space = (screenWidth - itemWidth * spanCount) / (spanCount + 1).toFloat()
            mBinding.rvHistory.addItemDecoration(SpaceDecoration(space.toInt()))
        }
        mBinding.topBar.setRightImgClickListener {
            if (mBinding.deleteBar.visibility == View.GONE) {
                mBinding.topBar.setRightText(R.string.cancel)
                mHistoryAdapter.setShowCheckBox(true)
                mBinding.deleteBar.visibility = View.VISIBLE
            } else {
                mBinding.topBar.setRightText(R.string.delete)
                mHistoryAdapter.setShowCheckBox(false)
                mBinding.deleteBar.visibility = View.GONE
            }
        }
        mBinding.btnOk.setOnClickListener {
            if (mHistoryAdapter.data.size <= 0)
                return@setOnClickListener
            if (mHistoryAdapter.getCheckedList().isNotEmpty()) {
                mViewModel.deleteVideoHistoryAndUpdateData(mHistoryAdapter.getCheckedList())
                mPage = 1
                mHistoryAdapter.setShowCheckBox(false)
                mBinding.deleteBar.visibility = View.GONE
                mBinding.topBar.setRightText(R.string.delete)
            }
        }
        mBinding.checkbox.setOnCheckedChangeListener { _, isChecked ->
            mHistoryAdapter.getCheckedList().clear()
            if (isChecked) {
                mHistoryAdapter.getCheckedList().addAll(mHistoryAdapter.data)
            }
            mHistoryAdapter.notifyDataSetChanged()
        }
    }

    private fun bind() {
        mViewModel.mViewState.observe(this) {
            if (it.videoHistory != null) {
                if (it.videoHistory.isEmpty()) {
                    mHistoryAdapter.setNewInstance(it.videoHistory)
                    mHistoryAdapter.setEmptyView(R.layout.layout_no_history)
                } else {
                    if (mPage <= 1) {
                        mHistoryAdapter.setNewInstance(it.videoHistory)
                    } else {
                        mHistoryAdapter.addData(it.videoHistory)
                    }
                }
            }
            if (it.count > 0) {
                if (mHistoryAdapter.data.size < it.count)
                    mHistoryAdapter.loadMoreModule.loadMoreComplete()
                else
                mHistoryAdapter.loadMoreModule.loadMoreEnd()
            } else {
                mHistoryAdapter.loadMoreModule.loadMoreEnd()
            }
        }
    }

    override val mViewBindingInflater: (inflater: LayoutInflater) -> ActivityVideoHistoryBinding
        get() = ActivityVideoHistoryBinding::inflate
}