package top.cyixlq.core.common.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.scope.activityScope
import org.koin.core.scope.Scope

abstract class CommonActivity<VB : ViewBinding> : AutoDisposeActivity(), AndroidScopeComponent {

    override val scope: Scope by activityScope()
    private var _binding: VB? = null
    protected lateinit var mBinding: VB
    abstract val mViewBindingInflater: (inflater: LayoutInflater) -> VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = mViewBindingInflater.invoke(layoutInflater)
        _binding?.let {
            mBinding = it
            setContentView(it.root)
        }
    }
}