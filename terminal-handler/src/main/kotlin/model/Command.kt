package model

import storage.api.StorageManager

/**
 * Represents the result of a command execution.
 *
 * @property output The output of the command execution.
 */
data class Result(val output: String = "")

/**
 * Represents a command that can be executed on a storage manager.
 *
 * @param T the type of the value stored in the storage manager
 */
interface Command<T> {
    fun executeIn(storageManager: StorageManager<T>): Result
}