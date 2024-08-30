import implementation.TerminalCommandHandlerFactoryImpl
import implementation.TerminalCommandHandlerImpl
import model.TerminalCommandHandler
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.Test
import kotlin.test.assertEquals


/**
 * Integration test class for [TerminalCommandHandlerImpl].
 */
class TerminalCommandHandlerIntegrationTest {
    private lateinit var terminalCommandHandler: TerminalCommandHandler

    private val outContent = ByteArrayOutputStream()
    private val errContent = ByteArrayOutputStream()
    private val originalOut: PrintStream = System.out
    private val originalErr: PrintStream = System.err

    @BeforeEach
    fun beforeEach() {
        terminalCommandHandler = TerminalCommandHandlerFactoryImpl.create()
        System.setOut(PrintStream(outContent))
        System.setErr(PrintStream(errContent))
    }

    @AfterEach
    fun restoreStreams() {
        System.setOut(originalOut)
        System.setErr(originalErr)
    }

    @Test
    fun testOutput() {
        terminalCommandHandler.handleInput("SET foo 122")
        terminalCommandHandler.handleInput("GET foo")
        assertEquals("122\n", outContent.toString())

        terminalCommandHandler.handleInput("GET 123")
        assertEquals("key not set\n", errContent.toString())
    }

    // Input data from the task

    @Test
    fun setGetDeleteValueTaskTest() {
        terminalCommandHandler.handleInput("SET foo 123")
        terminalCommandHandler.handleInput("GET foo")
        assertEquals("123\n", outContent.toString())


        terminalCommandHandler.handleInput("DELETE foo")
        terminalCommandHandler.handleInput("GET foo")
        assertEquals("key not set\n", errContent.toString())
    }

    @Test
    fun countNumberOfOccurrencesTaskTest() {
        terminalCommandHandler.handleInput("SET foo 123")
        terminalCommandHandler.handleInput("SET bar 456")
        terminalCommandHandler.handleInput("SET baz 123")
        terminalCommandHandler.handleInput("COUNT 123")
        assertEquals("2\n", outContent.toString())
        outContent.reset()

        terminalCommandHandler.handleInput("COUNT 456")
        assertEquals("1\n", outContent.toString())
    }

    @Test
    fun commitTaskTest() {
        terminalCommandHandler.handleInput("SET bar 123")
        terminalCommandHandler.handleInput("GET bar")
        assertEquals("123\n", outContent.toString())
        outContent.reset()

        terminalCommandHandler.handleInput("BEGIN")
        terminalCommandHandler.handleInput("SET foo 456")
        terminalCommandHandler.handleInput("GET bar")
        assertEquals("123\n", outContent.toString())
        outContent.reset()

        terminalCommandHandler.handleInput("DELETE bar")
        terminalCommandHandler.handleInput("COMMIT")
        terminalCommandHandler.handleInput("GET bar")
        assertEquals("key not set\n", errContent.toString())
        errContent.reset()

        terminalCommandHandler.handleInput("ROLLBACK")
        assertEquals("no transaction\n", errContent.toString())
        errContent.reset()

        terminalCommandHandler.handleInput("GET foo")
        assertEquals("456\n", outContent.toString())
        outContent.reset()
    }

    @Test
    fun rollbackTaskTest() {
        terminalCommandHandler.handleInput("SET foo 123")
        terminalCommandHandler.handleInput("SET bar abc")
        terminalCommandHandler.handleInput("BEGIN")
        terminalCommandHandler.handleInput("SET foo 456")
        terminalCommandHandler.handleInput("GET foo")
        assertEquals("456\n", outContent.toString())
        outContent.reset()

        terminalCommandHandler.handleInput("SET bar def")
        terminalCommandHandler.handleInput("GET bar")
        assertEquals("def\n", outContent.toString())
        outContent.reset()

        terminalCommandHandler.handleInput("ROLLBACK")
        terminalCommandHandler.handleInput("GET foo")
        assertEquals("123\n", outContent.toString())
        outContent.reset()

        terminalCommandHandler.handleInput("GET bar")
        assertEquals("abc\n", outContent.toString())
        outContent.reset()

        terminalCommandHandler.handleInput("COMMIT")
        assertEquals("no transaction\n", errContent.toString())
        errContent.reset()
    }

    @Test
    fun nestedTaskTest() {
        terminalCommandHandler.handleInput("SET foo 123")
        terminalCommandHandler.handleInput("SET bar 456")
        terminalCommandHandler.handleInput("BEGIN")
        terminalCommandHandler.handleInput("SET foo 456")
        terminalCommandHandler.handleInput("BEGIN")
        terminalCommandHandler.handleInput("COUNT 456")
        assertEquals("2\n", outContent.toString())
        outContent.reset()

        terminalCommandHandler.handleInput("GET foo")
        assertEquals("456\n", outContent.toString())
        outContent.reset()

        terminalCommandHandler.handleInput("SET foo 789")
        terminalCommandHandler.handleInput("GET foo")
        assertEquals("789\n", outContent.toString())
        outContent.reset()

        terminalCommandHandler.handleInput("ROLLBACK")
        terminalCommandHandler.handleInput("GET foo")
        assertEquals("456\n", outContent.toString())
        outContent.reset()

        terminalCommandHandler.handleInput("DELETE foo")
        terminalCommandHandler.handleInput("GET foo")
        assertEquals("key not set\n", errContent.toString())
        errContent.reset()

        terminalCommandHandler.handleInput("ROLLBACK")
        terminalCommandHandler.handleInput("GET foo")
        assertEquals("123\n", outContent.toString())
        outContent.reset()
    }
}