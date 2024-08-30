package implementation

import model.CommandParser
import model.Result
import storage.api.StorageManager
import model.TerminalCommandHandler

/**
 * Implementation of [TerminalCommandHandler].
 *
 * @property commandParser The command parser used to parse the input string into a command.
 * @property storageManager The storage manager used to execute the command.
 */
internal class TerminalCommandHandlerImpl(
    private val commandParser: CommandParser<String>,
    private val storageManager: StorageManager<String>
) :
    TerminalCommandHandler {
    override fun handleInput(input: String) {
        try {
            val command = commandParser.parse(input)
            val result = command.executeIn(storageManager)

            processResult(result)
        } catch (e: Exception) {
            System.err.println(e.message)
        }
    }

    private fun processResult(result: Result) {
        if (result.output.isNotEmpty()) println(result.output)
    }
}