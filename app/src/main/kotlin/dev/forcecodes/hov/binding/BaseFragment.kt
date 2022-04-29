@file:Suppress("unused")

package dev.forcecodes.hov.binding

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<T: ViewBinding>(@LayoutRes layoutId: Int): Fragment(layoutId) {

    open val binding: T? = null

    /**
     * Convenient binding calls.
     *
     * Same to scoping functions -> Foo.apply{}
     */
    fun requireBinding(lambda: T.() -> Unit) {
        binding?.apply { lambda(this) }
    }

}