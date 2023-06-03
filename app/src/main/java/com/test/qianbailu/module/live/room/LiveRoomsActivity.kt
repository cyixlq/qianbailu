package com.test.qianbailu.module.live.room

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.test.qianbailu.R
import com.test.qianbailu.databinding.ActivityLiveRoomsBinding
import com.test.qianbailu.module.live.play.LivePlayActivity
import com.test.qianbailu.ui.adapter.LiveRoomAdapter
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import top.cyixlq.core.common.activity.CommonActivity

class LiveRoomsActivity : CommonActivity<ActivityLiveRoomsBinding>() {

    private val mViewModel by viewModel<LiveRoomsViewModel>()
    private val mAdapter: LiveRoomAdapter by inject()
    private var mPlatformPath = ""

    private lateinit var emptyView: View
    private lateinit var infoText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        mPlatformPath = intent.getStringExtra("platformPath") ?: ""
        binds()
        refresh()
    }

    private fun initView() {
        mBinding.topBar.setTitle(intent.getStringExtra("platformTitle") ?: "未知平台")
        mBinding.slRefresh.setColorSchemeResources(R.color.colorPrimary)
        mBinding.slRefresh.setOnRefreshListener { refresh() }
        mAdapter.setOnItemClickListener { _, _, position ->
            val liveRoom = mAdapter.getItem(position)
            LivePlayActivity.launch(this, liveRoom.title, liveRoom.address)
        }
        mBinding.rvLiveRooms.layoutManager = GridLayoutManager(this, 3)
        mBinding.rvLiveRooms.adapter = mAdapter
        emptyView = LayoutInflater.from(this).inflate(R.layout.layout_empty, mBinding.rvLiveRooms, false)
        emptyView.setOnClickListener { refresh() }
        infoText = emptyView.findViewById(R.id.tvInfo)
    }

    private fun refresh() {
        if (mPlatformPath.isNotEmpty())
            mViewModel.getLiveRooms(mPlatformPath)
    }

    @SuppressLint("SetTextI18n")
    private fun binds() {
        mViewModel.viewState.observe(this, Observer {
            mBinding.slRefresh.isRefreshing = it.isLoading
            if (it.throwable != null) {
                mAdapter.setEmptyView(emptyView)
                infoText.text = "出错了：${it.throwable.localizedMessage}\n点击重试"
                mAdapter.setNewInstance(null)
            }
            if (it.liveRooms != null) {
                if (it.liveRooms.liveRoomList.isEmpty()) {
                    infoText.text = "空空如也\n点击重试"
                    mAdapter.setEmptyView(emptyView)
                }
                mAdapter.setNewInstance(it.liveRooms.liveRoomList)
            }
        })
    }

    companion object {
        fun launch(activity: FragmentActivity, platformPath: String, platformTitle: String) {
            activity.startActivity(Intent(activity, LiveRoomsActivity::class.java)
                .putExtra("platformPath", platformPath)
                .putExtra("platformTitle", platformTitle))
        }
    }

    override val mViewBindingInflater: (inflater: LayoutInflater) -> ActivityLiveRoomsBinding
        get() = ActivityLiveRoomsBinding::inflate
}
