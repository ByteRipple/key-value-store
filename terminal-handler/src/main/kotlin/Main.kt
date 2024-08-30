package org

import implementation.TerminalCommandHandlerFactoryImpl

class Main {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val terminalCommandHandler = TerminalCommandHandlerFactoryImpl.create()

            println("Ready to work:")
            while (true) {
                val input = readLine() ?: continue
                if (input.uppercase() == "EXIT") break

                terminalCommandHandler.handleInput(input)
            }
        }
    }
}