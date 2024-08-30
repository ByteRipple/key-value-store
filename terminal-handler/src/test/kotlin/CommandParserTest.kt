import implementation.*
import implementation.Set
import implementation.exceptions.CommandParserEmptyInputCommandException
import implementation.exceptions.CommandParserInvalidCommandArgsCommandException
import implementation.exceptions.CommandParserUnsupportedCommandException
import model.Command
import model.CommandParser
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.assertEquals

/**
 * Represents a class for testing the [CommandParserImpl].
 */
class CommandParserTest {
    companion object {
        @JvmStatic
        fun emptyInputSource() = listOf(Arguments.of(""), Arguments.of("  "))

        @JvmStatic
        fun parseValidCommandSource() = listOf(
            Arguments.of("SET a b", Set("a", "b")),
            Arguments.of("    set    a    b", Set("a", "b")),
            Arguments.of("GET a", Get<String>("a")),
            Arguments.of("DELETE a", Delete<String>("a")),
            Arguments.of("COUNT a", Count("a")),
            Arguments.of("BEGIN", InitTransaction<String>()),
            Arguments.of("COMMIT", Commit<String>()),
            Arguments.of("ROLLBACK", Rollback<String>()),
        )
    }


    private lateinit var commonParser: CommandParser<String>

    @BeforeEach
    fun beforeEach() {
        commonParser = CommandParserImpl()
    }


    @ParameterizedTest
    @CsvSource("test", "co mmit")
    fun invalidCommandTest(input: String) {
        assertThrows<CommandParserUnsupportedCommandException> { commonParser.parse(input) }
    }

    @ParameterizedTest
    @MethodSource("emptyInputSource")
    fun emptyInputTest(input: String) {
        assertThrows<CommandParserEmptyInputCommandException> { commonParser.parse(input) }
    }

    @ParameterizedTest
    @CsvSource(
        "SET",
        "SET 1",
        "GET",
        "GET K l",
        "DELETE",
        "DELETE k k",
        "COUNT",
        "COUNT l l",
        "BEGIN d",
        "ROLLBACK l",
        "COMMIT l"
    )
    fun invalidArgumentsTest(input: String) {
        assertThrows<CommandParserInvalidCommandArgsCommandException> { commonParser.parse(input) }
    }

    @ParameterizedTest
    @MethodSource("parseValidCommandSource")
    fun parseValidCommandTest(input: String, command: Command<String>) {
        assertEquals(command, commonParser.parse(input))
    }
}