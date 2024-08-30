package model

/**
 * Represents a command parser that can parse a string input into [Command].
 *
 * @param T the type of the value stored in the command
 */
interface CommandParser<T> {
    fun parse(s: String): Command<T>
}