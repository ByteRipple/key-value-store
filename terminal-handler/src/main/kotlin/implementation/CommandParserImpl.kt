package implementation

import implementation.exceptions.CommandParserEmptyInputCommandException
import implementation.exceptions.CommandParserInvalidCommandArgsCommandException
import implementation.exceptions.CommandParserUnsupportedCommandException
import model.Command
import model.CommandParser



/**
 * Represents a set of predefined commands that can be used in a command-line interface.
 * Each predefined command has a unique identifier and can create a corresponding [Command] object
 * based on the arguments provided.
 *
 * @property identifier The unique identifier of command.
 */
internal enum class PredefinedCommand(val identifier: String) {
    Set("SET") {
        override fun createCommand(args: List<String>): Command<String> {
            checkArgs(3, args.size)
            return Set(args[1], args.last())
        }
    },

    Get("GET") {
        override fun createCommand(args: List<String>): Command<String> {
            checkArgs(2, args.size)
            return Get(args.last())
        }
    },
    Delete("DELETE") {
        override fun createCommand(args: List<String>): Command<String> {
            checkArgs(2, args.size)
            return Delete(args.last())
        }
    },
    Count("COUNT") {
        override fun createCommand(args: List<String>): Command<String> {
            checkArgs(2, args.size)
            return Count(args.last())
        }
    },
    InitTransaction("BEGIN") {
        override fun createCommand(args: List<String>): Command<String> {
            checkArgs(1, args.size)
            return InitTransaction()
        }
    },
    Commit("COMMIT") {
        override fun createCommand(args: List<String>): Command<String> {
            checkArgs(1, args.size)
            return Commit()
        }
    },
    Rollback("ROLLBACK") {
        override fun createCommand(args: List<String>): Command<String> {
            checkArgs(1, args.size)
            return Rollback()
        }
    };

    abstract fun createCommand(args: List<String>): Command<String>

    protected fun checkArgs(expected: Int, actual: Int) {
        if (expected != actual) throw CommandParserInvalidCommandArgsCommandException()
    }
}


/**
 * Implementation of the [CommandParser] interface that can parse a string input into a [Command].
 */
internal class CommandParserImpl : CommandParser<String> {
    override fun parse(s: String): Command<String> {
        val (command, args) = getArgs(s)
        return createCommand(command, args)
    }

    private fun getArgs(s: String): Pair<String, List<String>> {
        val trimmedInput = s.trim()
        if (trimmedInput.isEmpty()) throw CommandParserEmptyInputCommandException()

        val args = trimmedInput.split(Regex("\\s+"))
        val command = args.first().uppercase()

        return Pair(command, args)
    }

    private fun createCommand(commandIdentifier: String, args: List<String>): Command<String> {
        val command = PredefinedCommand.entries.find { it.identifier == commandIdentifier }
            ?: throw CommandParserUnsupportedCommandException()
        return command.createCommand(args)
    }
}