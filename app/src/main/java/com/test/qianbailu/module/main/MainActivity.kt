package com.test.qianbailu.module.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.test.qianbailu.R
import com.test.qianbailu.module.catalog.CatalogFragment
import com.test.qianbailu.module.home.HomeFragment
import com.test.qianbailu.ui.adapter.ViewPagerFragmentAdapter
import com.test.qianbailu.ui.widget.UpdateDialogFragment
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.scope.lifecycleScope
import org.koin.androidx.viewmodel.scope.viewModel
import top.cyixlq.core.common.activity.CommonActivity
import top.cyixlq.core.utils.VersionUtil
import top.cyixlq.core.utils.toastShort

class MainActivity : CommonActivity() {

    private val mViewModel by lifecycleScope.viewModel<MainViewModel>(this)

    override val layoutId: Int = R.layout.activity_main

    private var lastTime = 0L
    private val duration = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        initView()
        binds()
        mViewModel.getVersionInfo()
    }

    @SuppressLint("SetTextI18n")
    private fun binds() {
        mViewModel.viewState.observe(this, Observer {
            if (it.updateAppBean != null && VersionUtil.getVersionCode() < it.updateAppBean.code) {
                UpdateDialogFragment
                    .newInstance(it.updateAppBean)
                    .show(supportFragmentManager, "tag")
            }
        })
    }

    private fun initView() {
        vpMain.adapter = ViewPagerFragmentAdapter(
            this,
            arrayListOf(HomeFragment.instance(), CatalogFragment.instance())
        )
        vpMain.isUserInputEnabled = false
        btmNav.setOnNavigationItemSelectedListener {
            return@setOnNavigationItemSelectedListener if (it.itemId == R.id.menuHome) {
                vpMain.currentItem = 0
                true
            } else {
                vpMain.currentItem = 1
                true
            }
        }
    }

    override fun onBackPressed() {
        val currentTime = System.currentTimeMillis()
        if ((currentTime - lastTime) > duration) {
            "再按一次退出".toastShort()
            lastTime = currentTime
        } else {
            super.onBackPressed()
        }
    }

    companion object {
        fun launch(activity: FragmentActivity) {
            activity.startActivity(Intent(activity, MainActivity::class.java))
        }
    }
}
