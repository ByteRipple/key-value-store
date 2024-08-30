package implementation

import model.Command
import model.Result
import storage.api.StorageManager

/**
 * Represents a command to set a value in a storage manager.
 * [equals] and [hashCode] predefined by data class and required in tests.
 *
 * @param T the type of the value stored in the storage manager
 * @property key the key of the value to set
 * @property value the value to set in the storage manager
 */
internal data class Set<T>(private val key: String, private val value: T) : Command<T> {
    override fun executeIn(storageManager: StorageManager<T>) = storageManager.asResult { set(key, value) }
}

/**
 * Represents a command to retrieve a value from a storage manager.
 * [equals] and [hashCode] predefined by data class and required in tests.
 *
 * @param T the type of the value stored in the storage manager
 * @property key the key of the value to retrieve
 */
internal data class Get<T>(private val key: String) : Command<T> {
    override fun executeIn(storageManager: StorageManager<T>) =
        storageManager.asResultWithOutput { get(key).toString() }
}

/**
 * Represents a command that deletes a value from a storage manager.
 * [equals] and [hashCode] predefined by data class and required in tests.
 *
 * @param key The key of the value to be deleted.
 * @param T The type of the value stored in the storage manager.
 */
internal data class Delete<T>(private val key: String) : Command<T> {
    override fun executeIn(storageManager: StorageManager<T>) = storageManager.asResult { delete(key) }
}

/**
 * Represents a command that counts the number of value's occurrences in a storage manager.
 * [equals] and [hashCode] predefined by data class and required in tests.
 *
 * @param T the type of the value stored in the storage manager
 * @property value the value to count
 */
internal data class Count<T>(private val value: T) : Command<T> {
    override fun executeIn(storageManager: StorageManager<T>) =
        storageManager.asResultWithOutput { count(value).toString() }
}

/**
 * Represents a command that initializes a transaction in a storage manager.
 * [equals] and [hashCode] explicitly declared and required in tests.
 *
 * @param T the type of the value stored in the storage manager
 */
internal class InitTransaction<T> : Command<T> {
    override fun executeIn(storageManager: StorageManager<T>) = storageManager.asResult { initTransaction() }
    override fun equals(other: Any?): Boolean = other is InitTransaction<*>
    override fun hashCode(): Int = javaClass.hashCode()
}

/**
 * Represents a command that commits changes made in a storage manager.
 * [equals] and [hashCode] explicitly declared and required in tests.
 *
 * @param T the type of the value stored in the storage manager
 */
internal class Commit<T> : Command<T> {
    override fun executeIn(storageManager: StorageManager<T>) = storageManager.asResult { commit() }
    override fun equals(other: Any?): Boolean = other is Commit<*>
    override fun hashCode(): Int = javaClass.hashCode()
}

/**
 * Represents a command that rolls back a transaction in a storage manager.
 * [equals] and [hashCode] explicitly declared and required in tests.
 *
 * @param T the type of the value stored in the storage manager
 */
internal class Rollback<T> : Command<T> {
    override fun executeIn(storageManager: StorageManager<T>) = storageManager.asResult { rollback() }
    override fun equals(other: Any?): Boolean = other is Rollback<*>
    override fun hashCode(): Int = javaClass.hashCode()
}

private inline fun <T> StorageManager<T>.asResult(command: StorageManager<T>.() -> Unit): Result {
    command()
    return Result()
}

private inline fun <T> StorageManager<T>.asResultWithOutput(command: StorageManager<T>.() -> String): Result = Result(command())