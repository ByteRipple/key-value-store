package model

/**
 * * Represents a class that handles terminal commands by parsing the input, executing the command on a storage manager,
 *  * and printing the output if any.
 */
interface TerminalCommandHandler {
    fun handleInput(input: String)
}