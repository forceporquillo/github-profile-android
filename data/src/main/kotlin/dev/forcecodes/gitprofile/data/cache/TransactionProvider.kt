package dev.forcecodes.gitprofile.data.cache

import androidx.room.withTransaction
import dev.forcecodes.gitprofile.data.internal.InternalApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionProvider @Inject constructor(
    @InternalApi private val database: AppDatabase
) {
    suspend fun <R> runAsTransaction(block: suspend () -> R): R {
        return database.withTransaction(block)
    }
}