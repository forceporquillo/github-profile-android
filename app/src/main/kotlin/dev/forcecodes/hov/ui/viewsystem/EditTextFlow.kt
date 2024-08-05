package dev.forcecodes.hov.ui.viewsystem

import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onStart

fun EditText.doOnTextChanged(
    debounce: Long = 750L,
    onClear: () -> Unit = {}
): Flow<CharSequence> {
    return callbackFlow {
        val listener = doOnTextChanged { text, _, _, _ ->
            trySend(text ?: "")
            if (text?.isEmpty() == true) {
                onClear()
            }
        }
        addTextChangedListener(listener)
        awaitClose { removeTextChangedListener(listener) }
    }.onStart { emit(text.toString()) }
        .debounce(debounce)
}