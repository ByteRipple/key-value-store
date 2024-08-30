package storage.implementation

import storage.api.Storage

/**
 * Implementation of the [Storage] interface that stores key-value pairs in memory using a [HashMap].
 *
 * @param T the type of values stored in the storage
 */
internal class StorageImpl<T> : Storage<T> {
    private val memory = HashMap<String, T>()

    override fun get(key: String): T? = memory[key]

    override fun delete(key: String) {
        memory.remove(key)
    }

    override fun contains(key: String): Boolean = memory.containsKey(key)

    override fun getAll(): List<Pair<String, T>> = memory.entries.map { Pair(it.key, it.value) }

    override fun count(value: T): Int = memory.values.count { it == value }

    override fun set(key: String, value: T) {
        memory[key] = value
    }

}
