import storage.api.StorageManager
import storage.implementation.exceptions.StorageManagerGetException
import storage.implementation.exceptions.StorageManagerNoTransactionException
import storage.implementation.exceptions.StorageManagerDeletionException
import storage.implementation.SimpleStorageManagerFactoryImpl
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.MethodSource
import storage.implementation.StorageManagerImpl
import kotlin.test.assertEquals

/**
 * This class provides test cases for the [StorageManagerImpl] implementation.
 */
class SimpleStorageTest {

    companion object {
        @JvmStatic
        fun countValuesSource() = listOf(Arguments.of(listOf("key1", "key2", "key3"), "value"))
    }

    private lateinit var manager: StorageManager<String>

    @BeforeEach
    fun beforeEach() {
        manager = SimpleStorageManagerFactoryImpl.create()
    }

    @ParameterizedTest
    @CsvSource("key, value")
    fun insertValueTest(key: String, value: String) {
        manager.set(key, value)
        assertEquals(value, manager.get(key))
    }

    @ParameterizedTest
    @CsvSource("key, value")
    fun deleteValueTest(key: String, value: String) {
        manager.set(key, value)
        manager.delete(key)
        assertThrows<StorageManagerGetException> { manager.get(key) }
    }

    @ParameterizedTest
    @CsvSource("key")
    fun deleteNotExistedValueTest(key: String) {
        assertThrows<StorageManagerDeletionException> { manager.delete(key) }
    }

    @ParameterizedTest
    @CsvSource("key")
    fun setGetNotExistedValueTest(key: String) {
        assertThrows<StorageManagerGetException> { manager.get(key) }
    }

    @ParameterizedTest
    @MethodSource("countValuesSource")
    fun countNumberOfValueTest(keys: List<String>, value: String) {
        keys.forEach { manager.set(it, value) }
        assertEquals(keys.size, manager.count(value))
    }

    // TRANSACTION TEST

    @ParameterizedTest
    @CsvSource("key, value")
    fun commitTransactionTest(key: String, value: String) {
        manager.initTransaction()
        manager.set(key, value)
        manager.commit()
        assertEquals(value, manager.get(key))
    }

    @ParameterizedTest
    @CsvSource("key, value")
    fun openTransactionAndReadPrefilledValueTest(key: String, value: String) {
        manager.set(key, value)
        manager.initTransaction()

        assertEquals(value, manager.get(key))
        manager.commit()
    }

    @ParameterizedTest
    @CsvSource("key, value")
    fun rollbackTransactionTest(key: String, value: String) {
        manager.initTransaction()
        manager.set(key, value)
        manager.rollback()
        assertThrows<StorageManagerGetException> { manager.get(key) }
    }

    @ParameterizedTest
    @CsvSource("key, value")
    fun openAndRollbackTransactionWithPrefilledDataTest(key: String, value: String) {
        manager.set(key, value)
        manager.initTransaction()
        manager.rollback()
        assertEquals(value, manager.get(key))
    }

    @Test
    fun commitAndRollbackNotExistedTransactionTest() {
        assertThrows<StorageManagerNoTransactionException> { manager.commit() }
        assertThrows<StorageManagerNoTransactionException> { manager.rollback() }
    }

    @ParameterizedTest
    @CsvSource("key1, value1, key2, value2")
    fun commitedNestedTransactionTest(prefilledKey: String, prefilledValue: String, key: String, value: String) {
        manager.initTransaction()
        manager.set(prefilledKey, prefilledValue)

        manager.initTransaction()
        assertEquals(prefilledValue, manager.get(prefilledKey))
        manager.set(key, value)
        manager.commit()

        assertEquals(value, manager.get(key))
        manager.rollback()

        assertThrows<StorageManagerGetException> { manager.get(prefilledKey) }
        assertThrows<StorageManagerGetException> { manager.get(key) }
    }

    @ParameterizedTest
    @CsvSource("key1, value1, key2, value2")
    fun rollbackNestedTransactionTest(prefilledKey: String, prefilledValue: String, key: String, value: String) {
        manager.initTransaction()
        manager.set(prefilledKey, prefilledValue)

        manager.initTransaction()
        assertEquals(prefilledValue, manager.get(prefilledKey))
        manager.set(key, value)
        manager.rollback()

        assertThrows<StorageManagerGetException> { manager.get(key) }
        manager.commit()

        assertEquals(prefilledValue, manager.get(prefilledKey))
        assertThrows<StorageManagerGetException> { manager.get(key) }
    }

    // TASK TESTS

    @Test
    fun setGetDeleteTaskTest() {
        manager.set("foo", "123")
        assertEquals("123", manager.get("foo"))
        manager.delete("foo")
        assertThrows<StorageManagerGetException> { manager.get("foo") }
    }

    @Test
    fun countNumberOfValueTaskTest() {
        manager.set("foo", "123")
        manager.set("bar", "456")
        manager.set("baz", "123")
        assertEquals(2, manager.count("123"))
        assertEquals(1, manager.count("456"))
    }

    @Test
    fun commitTransactionTaskTest() {
        manager.set("bar", "123")
        assertEquals("123", manager.get("bar"))

        manager.initTransaction()
        manager.set("foo", "456")
        assertEquals("123", manager.get("bar"))
        manager.delete("bar")
        manager.commit()
        assertThrows<StorageManagerGetException> { manager.get("bar") }
        assertThrows<StorageManagerNoTransactionException> { manager.rollback() }
        assertEquals("456", manager.get("foo"))
    }

    @Test
    fun rollbackTransactionTaskTest() {
        manager.set("foo", "123")
        manager.set("bar", "abc")

        manager.initTransaction()
        manager.set("foo", "456")
        assertEquals("456", manager.get("foo"))
        manager.set("bar", "def")
        assertEquals("def", manager.get("bar"))
        manager.rollback()
        assertEquals("123", manager.get("foo"))
        assertEquals("abc", manager.get("bar"))

        assertThrows<StorageManagerNoTransactionException> { manager.commit() }
    }

    @Test
    fun nestedTransactionTaskTest() {
        manager.set("foo", "123")
        manager.set("bar", "456")

        manager.initTransaction()
        manager.set("foo", "456")
        manager.initTransaction()
        assertEquals(2, manager.count("456"))
        assertEquals("456", manager.get("foo"))
        manager.set("foo", "789")
        assertEquals("789", manager.get("foo"))
        manager.rollback()

        assertEquals("456", manager.get("foo"))
        manager.delete("foo")
        assertThrows<StorageManagerGetException> { manager.get("foo") }
        manager.rollback()

        assertEquals("123", manager.get("foo"))
    }
}