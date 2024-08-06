package dev.forcecodes.gitprofile.core

interface Mapper<in T, R> {
    operator fun invoke(data: T): R
}