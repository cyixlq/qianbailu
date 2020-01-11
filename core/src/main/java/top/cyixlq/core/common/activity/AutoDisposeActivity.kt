package top.cyixlq.core.common.activity

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider

abstract class AutoDisposeActivity : AppCompatActivity() {

    protected val scopeProvider: AndroidLifecycleScopeProvider by lazy {
        AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY)
    }
}