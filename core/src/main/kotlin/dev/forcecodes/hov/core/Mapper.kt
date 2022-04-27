package dev.forcecodes.hov.core

interface Mapper<in T, R> {
    operator fun invoke(data: T): R
}