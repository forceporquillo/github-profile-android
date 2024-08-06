package dev.forcecodes.android.gitprofile.extensions

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun Fragment.launchWithViewLifecycle(
    delayInMillis: Long = 0L,
    activeState: Lifecycle.State = Lifecycle.State.STARTED,
    block: suspend () -> Unit
) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.lifecycle.repeatOnLifecycle(activeState) {
            // automatically disregards delay when set to 0.
            delay(delayInMillis)
            block()
        }
    }
}

fun Fragment.launchWithViewLifecycleScope(
    activeState: Lifecycle.State = Lifecycle.State.STARTED,
    block: CoroutineScope.() -> Unit
) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.lifecycle.repeatOnLifecycle(activeState) {
            block()
        }
    }
}