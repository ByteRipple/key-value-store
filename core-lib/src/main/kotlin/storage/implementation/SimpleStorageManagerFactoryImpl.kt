package storage.implementation

import storage.api.StorageManager
import storage.api.StorageManagerFactory


/**
 * Implementation of the [StorageManagerFactory] interface.
 * This class creates instances of [StorageManager] by injecting the required dependencies.
 */
object SimpleStorageManagerFactoryImpl : StorageManagerFactory<String> {

    override fun create(): StorageManager<String> {
        val transactionFactory = TransactionFactoryImpl<String>()
        return StorageManagerImpl(transactionFactory)
    }
}