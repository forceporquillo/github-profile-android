package dev.forcecodes.hov.binding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

typealias BindingInflater<T> = (LayoutInflater, ViewGroup, Boolean) -> T

inline fun <T : ViewBinding> ViewGroup.viewBinding(
    crossinline factory: BindingInflater<T>,
    attach: Boolean = false
): T = factory(LayoutInflater.from(context), this, attach)