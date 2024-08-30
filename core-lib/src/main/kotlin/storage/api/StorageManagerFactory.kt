package storage.api


/**
 * This interface represents a factory for creating instances of [StorageManager]. Inject all required dependencies.
 *
 * @param T the type of the value stored in the [StorageManager]
 */
interface StorageManagerFactory<T> {
    fun create(): StorageManager<T>
}