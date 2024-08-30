package implementation.exceptions

import model.CommandParser

/**
 * Represents an exception that occurs during command parsing.
 *
 * @property message The detail message for this exception.
 */
internal sealed class CommandParserException(override val message: String) : Exception()

/**
 * Exception thrown when the command input is empty.
 */
internal class CommandParserEmptyInputCommandException : CommandParserException("please write a command")

/**
 * Exception that indicates an unsupported command in the [CommandParser].
 */
internal class CommandParserUnsupportedCommandException : CommandParserException("command not supported")

/**
 * Exception thrown when the command arguments are invalid.
 */
internal class CommandParserInvalidCommandArgsCommandException : CommandParserException("Invalid command args")
