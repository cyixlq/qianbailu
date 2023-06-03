package top.cyixlq.core.common.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.scope.fragmentScope
import org.koin.core.scope.Scope

abstract class CommonFragment<VB : ViewBinding> : AutoDisposeFragment(), AndroidScopeComponent {

    override val scope: Scope by fragmentScope()
    private var _binding: VB? = null
    protected lateinit var mBinding: VB
    abstract val mViewBindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = mViewBindingInflater.invoke(inflater, container, false)
        mBinding = _binding!!
        return _binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
