package implementation

import model.TerminalCommandHandler
import model.TerminalCommandHandlerFactory
import storage.implementation.SimpleStorageManagerFactoryImpl

/**
 * Factory implementation for creating instances of [TerminalCommandHandler].
 */
internal object TerminalCommandHandlerFactoryImpl : TerminalCommandHandlerFactory {
    override fun create(): TerminalCommandHandler {
        val simpleStorageManagerFactoryImpl = SimpleStorageManagerFactoryImpl.create()
        val commandParser = CommandParserImpl()
        return TerminalCommandHandlerImpl(commandParser, simpleStorageManagerFactoryImpl)
    }
}