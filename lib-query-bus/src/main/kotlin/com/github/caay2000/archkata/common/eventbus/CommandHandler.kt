package com.github.caay2000.archkata.common.eventbus

interface CommandHandler<in COMMAND : Command> {

    fun invoke(command: COMMAND)
}
