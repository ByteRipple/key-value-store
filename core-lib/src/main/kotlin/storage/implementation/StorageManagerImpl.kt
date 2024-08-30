package storage.implementation

import storage.api.StorageManager
import storage.api.Transaction
import storage.api.TransactionFactory
import storage.implementation.exceptions.StorageManagerGetException
import storage.implementation.exceptions.StorageManagerDeletionException
import storage.implementation.exceptions.StorageManagerNoTransactionException
import java.util.Stack

/**
 * This class represents a [StorageManager] implementation that operates on a generic type [T].
 *
 * @param transactionFactory The factory used to create transactions and nested transactions.
 * @param T The type of values stored in the storage.
 */
internal class StorageManagerImpl<T>(private val transactionFactory: TransactionFactory<T>) : StorageManager<T> {

    private val openTransactions: Stack<Transaction<T>> = Stack<Transaction<T>>()

    init {
        /** Initiate the main transaction */
        openTransactions.push(transactionFactory.createTransaction())
    }

    private val activeTransaction
        get() = openTransactions.peek()

    private val activeContext
        get() = openTransactions.peek().context


    override fun count(value: T): Int = activeContext.count(value)


    override fun set(key: String, value: T) {
        activeContext.set(key, value)
    }

    override fun get(key: String): T {
        return activeContext.get(key) ?: throw StorageManagerGetException()
    }

    override fun delete(key: String) {
        if (!activeContext.contains(key)) throw StorageManagerDeletionException()
        activeContext.delete(key)
    }

    override fun initTransaction() {
        val newTransaction = transactionFactory.createNestedTransaction(activeTransaction)
        openTransactions.add(newTransaction)
    }

    override fun commit() {
        if (isOnlyMainTransaction()) throw StorageManagerNoTransactionException()
        val activeTransaction = openTransactions.pop()
        openTransactions.pop()
        openTransactions.push(activeTransaction)
    }

    override fun rollback() {
        if (isOnlyMainTransaction()) throw StorageManagerNoTransactionException()
        openTransactions.removeLast()
    }

    private fun isOnlyMainTransaction() = openTransactions.size == 1

}