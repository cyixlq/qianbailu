package com.test.qianbailu.module.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.FragmentActivity
import com.test.qianbailu.BuildConfig
import com.test.qianbailu.R
import com.test.qianbailu.databinding.ActivityMainBinding
import com.test.qianbailu.ui.adapter.ViewPagerFragmentAdapter
import com.test.qianbailu.ui.widget.UpdateDialogFragment
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import top.cyixlq.core.common.activity.CommonActivity
import top.cyixlq.core.utils.toastShort

class MainActivity : CommonActivity<ActivityMainBinding>() {

    private val mViewModel by viewModel<MainViewModel>()

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
        mViewModel.viewState.observe(this) {
            if (it.updateAppBean != null && BuildConfig.VERSION_CODE < it.updateAppBean.code) {
                UpdateDialogFragment
                    .newInstance(it.updateAppBean)
                    .show(supportFragmentManager, "tag")
            }
        }
    }

    private fun initView() {
        val adapter: ViewPagerFragmentAdapter = get { parametersOf(this) }
        mBinding.vpMain.adapter = adapter
        mBinding.vpMain.isUserInputEnabled = false
        mBinding.btmNav.setOnNavigationItemSelectedListener {
            val index = when(it.itemId) {
                R.id.menuCatalog -> 1
                R.id.menuLive -> 2
                else -> 0
            }
            mBinding.vpMain.setCurrentItem(index, false)
            return@setOnNavigationItemSelectedListener true
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val currentTime = System.currentTimeMillis()
        if ((currentTime - lastTime) > duration) {
            "再按一次退出".toastShort()
            lastTime = currentTime
        } else {
            @Suppress("DEPRECATION")
            super.onBackPressed()
        }
    }

    companion object {
        fun launch(activity: FragmentActivity) {
            activity.startActivity(Intent(activity, MainActivity::class.java))
        }
    }

    override val mViewBindingInflater: (inflater: LayoutInflater) -> ActivityMainBinding
        get() = ActivityMainBinding::inflate
}
