package com.github.caay2000.archkata.common.eventbus.impl

import com.github.caay2000.archkata.common.eventbus.Command
import com.github.caay2000.archkata.common.eventbus.CommandHandler
import com.github.caay2000.archkata.common.eventbus.Event
import com.github.caay2000.archkata.common.eventbus.Query
import kotlin.reflect.KClass

abstract class KTCommandHandler<in COMMAND : Command>(type: KClass<COMMAND>) : CommandHandler<COMMAND> {

    init {
        subscribeTo(type)
    }

    private fun subscribeTo(type: KClass<COMMAND>) {
        KTEventBus.getInstance<COMMAND, Query, Event>().subscribe(this, type)
    }

    internal fun execute(command: Any) {
        @Suppress("UNCHECKED_CAST")
        this.invoke(command as COMMAND)
    }

    abstract override fun invoke(command: COMMAND)
}

fun <COMMAND : Command> instantiateCommandHandler(clazz: KClass<COMMAND>, commandHandler: CommandHandler<COMMAND>): KTCommandHandler<COMMAND> =
    object : KTCommandHandler<COMMAND>(clazz) {
        override fun invoke(command: COMMAND) = commandHandler.invoke(command)
    }
