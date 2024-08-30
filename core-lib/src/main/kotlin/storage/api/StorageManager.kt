package storage.api

import storage.implementation.exceptions.StorageManagerDeletionException
import storage.implementation.exceptions.StorageManagerNoTransactionException

/**
 * This interface represents a storage manager that is responsible to operate [Storage].
 *
 * @param T the type of the value stored in the [Storage]
 */
interface StorageManager<T> {
    /**
     * Retrieves the value associated with the given key from the [Storage].
     *
     * @param key the key of the value to retrieve
     * @return the value associated with the key
     */
    fun get(key: String): T

    /**
     * Sets the value associated with the given key in the [Storage].
     *
     * @param key The key to set.
     * @param value The value to set in the [Storage].
     */
    fun set(key: String, value: T)


    /**
     * Deletes the value associated with the given key from the [Storage].
     *
     * @param key The key of the value to be deleted.
     * @throws [StorageManagerDeletionException] if the specified key is not found in the [Storage].
     */
    fun delete(key: String)

    /**
     * Counts the occurrences of the given value in the [Storage].
     *
     * @param value the value to be counted
     * @return the number of occurrences in the [Storage]
     */
    fun count(value: T): Int

    /**
     * Initializes a new transaction in the [StorageManager].
     *
     * Note that this method does not return any value.
     */
    fun initTransaction()

    /**
     * Commits the changes made in the current transaction started by [initTransaction].
     *
     * @throws [StorageManagerNoTransactionException] if there is no active transaction
     */
    fun commit()

    /**
     * Rolls back the current transaction in the [StorageManager].
     *
     * This method undoes all the changes made within the transaction and
     * restores the [StorageManager] to its state before the transaction
     * was initiated by [initTransaction].
     *
     * @throws [StorageManagerNoTransactionException] if there is no active transaction
     */
    fun rollback()
}