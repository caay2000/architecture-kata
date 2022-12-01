package com.github.caay2000.archkata.common.eventbus

interface CommandBus {

    fun <COMMAND : Command> publish(command: COMMAND)
}
