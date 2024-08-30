package storage.api

/**
 * Represents an abstract transaction class that provides a base implementation for transactions.
 *
 * @param T the type of values stored in the [Storage]
 *
 * @property id the ID of the transaction
 * @property context the storage context associated with the transaction
 */
abstract class Transaction<T> {
    abstract val id: String
    abstract val context: Storage<T>
}