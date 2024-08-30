package storage.implementation.exceptions

import storage.api.Storage
import storage.api.StorageManager
import storage.implementation.StorageManagerImpl

/**
 * Base class for exceptions related to the [StorageManagerImpl].
 *
 * @param message a descriptive message of the exception
 */
internal sealed class StorageManagerException(override val message: String) : Exception()

/**
 * Exception that is thrown when trying to delete a key that does not exist in the [Storage].
 * @see [StorageManager.delete]
 */
internal class StorageManagerDeletionException : StorageManagerException("not existed key to delete")

/**
 * Exception thrown when attempting to retrieve a value from the [Storage] but the key is not set.
 * @see [StorageManager.get]
 */
internal class StorageManagerGetException : StorageManagerException("key not set")

/**
 * This exception is thrown when a transaction commit or rollback is attempted, but there is no active transaction.
 * @see [StorageManager.commit] and [StorageManager.rollback]
 */
internal class StorageManagerNoTransactionException : StorageManagerException("no transaction")

