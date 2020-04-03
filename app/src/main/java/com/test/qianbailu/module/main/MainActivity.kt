package com.test.qianbailu.module.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.test.qianbailu.R
import com.test.qianbailu.ui.adapter.ViewPagerFragmentAdapter
import com.test.qianbailu.ui.widget.UpdateDialogFragment
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.scope.lifecycleScope
import org.koin.androidx.viewmodel.scope.viewModel
import org.koin.core.parameter.parametersOf
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
        vpMain.adapter = lifecycleScope.get<ViewPagerFragmentAdapter> { parametersOf(this) }
        vpMain.isUserInputEnabled = false
        btmNav.setOnNavigationItemSelectedListener {
            val index = when(it.itemId) {
                R.id.menuCatalog -> 1
                R.id.menuLive -> 2
                else -> 0
            }
            vpMain.setCurrentItem(index, false)
            return@setOnNavigationItemSelectedListener true
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
