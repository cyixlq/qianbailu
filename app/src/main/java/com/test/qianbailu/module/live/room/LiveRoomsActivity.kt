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
import com.test.qianbailu.ui.adapter.LiveRoomAdapter
import kotlinx.android.synthetic.main.activity_live_rooms.*
import kotlinx.android.synthetic.main.activity_live_rooms.topBar
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.androidx.scope.lifecycleScope
import org.koin.androidx.viewmodel.scope.viewModel
import top.cyixlq.core.common.activity.CommonActivity

class LiveRoomsActivity : CommonActivity() {

    private val mViewModel by lifecycleScope.viewModel<LiveRoomsViewModel>(this)
    private val mAdapter: LiveRoomAdapter by lifecycleScope.inject()
    private var mPlatformPath = ""

    override val layoutId: Int = R.layout.activity_live_rooms
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
        topBar.setTitle(intent.getStringExtra("platformTitle") ?: "未知平台")
        slRefresh.setColorSchemeResources(R.color.colorPrimary)
        slRefresh.setOnRefreshListener { refresh() }
        rvLiveRooms.layoutManager = GridLayoutManager(this, 3)
        rvLiveRooms.adapter = mAdapter
        emptyView = LayoutInflater.from(this).inflate(R.layout.layout_empty, rvVideoCover, false)
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
            slRefresh.isRefreshing = it.isLoading
            if (it.throwable != null) {
                mAdapter.setEmptyView(emptyView)
                infoText.text = "出错了：${it.throwable.localizedMessage}\n点击重试"
                mAdapter.setNewData(null)
            }
            if (it.liveRooms != null) {
                if (it.liveRooms.liveRoomList.isEmpty()) {
                    infoText.text = "空空如也\n点击重试"
                    mAdapter.setEmptyView(emptyView)
                }
                mAdapter.setNewData(it.liveRooms.liveRoomList)
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
}
