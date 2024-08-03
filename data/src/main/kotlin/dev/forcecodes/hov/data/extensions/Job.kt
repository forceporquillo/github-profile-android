package dev.forcecodes.hov.data.extensions

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.SendChannel


/**
 * Cancel the Job when it's active.
 */
fun Job.cancelWhenActive(
    cancellationException: CancellationException? = null
) {
    if (isActive) {
        cancel(cancellationException)
    }
}

/**
 * Tries to send an element to a Channel and ignores the exception.
 */
fun <E> SendChannel<E>.tryOffer(element: E): Boolean = try {
    trySend(element).isSuccess
} catch (t: Throwable) {
    false // Ignore
}
