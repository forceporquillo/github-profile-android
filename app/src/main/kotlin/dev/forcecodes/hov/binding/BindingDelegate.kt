package dev.forcecodes.hov.binding

import androidx.viewbinding.ViewBinding

interface BindingDelegate<VB: ViewBinding> {
    fun binding(lambda: VB.() -> Unit)
}