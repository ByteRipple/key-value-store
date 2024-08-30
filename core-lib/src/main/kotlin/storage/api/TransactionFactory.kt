package storage.api


/**
 * Represents a factory for creating transactions and nested transactions.
 *
 * @param T the type of values stored in the [Storage]. @see [Transaction]
 */
interface TransactionFactory<T> {
    fun createTransaction(): Transaction<T>
    fun createNestedTransaction(transaction: Transaction<T>): Transaction<T>
}