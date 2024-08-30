package model

/**
 * Factory interface for creating instances of [TerminalCommandHandler]. Factory inject required dependency.
 */
interface TerminalCommandHandlerFactory {
    fun create(): TerminalCommandHandler
}