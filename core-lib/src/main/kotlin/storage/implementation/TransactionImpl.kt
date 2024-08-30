package storage.implementation

import storage.api.Storage
import storage.api.Transaction

/**
 * Represents an implementation of the [Transaction] class.
 */
internal data class TransactionImpl<T>(override val id: String, override val context: Storage<T>) : Transaction<T>()
