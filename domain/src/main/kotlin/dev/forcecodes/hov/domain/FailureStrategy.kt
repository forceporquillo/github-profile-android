package dev.forcecodes.hov.domain

sealed interface FailureStrategy {

    object ThrowSilently : FailureStrategy

    object ThrowOnFailure: FailureStrategy
}