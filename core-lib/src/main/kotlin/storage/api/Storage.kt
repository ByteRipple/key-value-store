package storage.api


/**
 * Represents a storage interface that provides functionality to store and retrieve values using keys.
 *
 * @param T the type of values stored in the storage
 */
interface Storage<T> {

    /**
     * Retrieves the value associated with the given key from the storage.
     *
     * @param key the key used to retrieve the value
     * @return the value associated with the key, or null if the key is not found
     */
    fun get(key: String): T?

    /**
     * Deletes the value associated with the specified key from the storage.
     *
     * @param key the key of the value to delete
     */
    fun delete(key: String)

    /**
     * Checks if the storage contains a value with the specified key.
     *
     * @param key the key to check for existence in the storage
     * @return true if the storage contains a value with the specified key, false otherwise
     */
    fun contains(key: String): Boolean

    /**
     * Sets a value in the storage with the specified key.
     *
     * @param key the key used to store the value
     * @param value the value to be stored in the storage
     */
    fun set(key: String, value: T)

    /**
     * Returns the number of specified value occurrences in the storage.
     *
     * @param value the value to count occurrences of
     * @return the number of specified value occurrences in the storage
     */
    fun count(value: T): Int

    /**
     * Retrieves all pairs of keys and values from the storage.
     *
     * @return a list of pairs representing the keys and values stored in the storage
     */
    fun getAll(): List<Pair<String, T>>
}