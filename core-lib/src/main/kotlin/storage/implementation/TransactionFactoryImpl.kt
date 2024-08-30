package storage.implementation

import storage.api.Storage
import storage.api.Transaction
import storage.api.TransactionFactory
import java.util.*

/**
 * An implementation of the [TransactionFactory] interface that creates transactions and nested transactions.
 *
 * @param T the type of values stored in the [Storage].
 * @see [TransactionFactory]
 */
internal class TransactionFactoryImpl<T> : TransactionFactory<T> {
    override fun createTransaction(): Transaction<T> {
        val emptyStorage = StorageImpl<T>()
        return TransactionImpl(getTransactionId(), emptyStorage)
    }

    override fun createNestedTransaction(transaction: Transaction<T>): Transaction<T> {
        val initStorage = StorageImpl<T>()
        val prefillData = transaction.context.getAll()

        for ((key, value) in prefillData) initStorage.set(key, value)

        return TransactionImpl(getTransactionId(), initStorage)
    }

    private fun getTransactionId(): String = UUID.randomUUID().toString()

}
