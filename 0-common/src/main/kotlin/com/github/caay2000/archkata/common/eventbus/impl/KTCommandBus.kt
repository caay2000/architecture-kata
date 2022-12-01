package com.github.caay2000.archkata.common.eventbus.impl

import com.github.caay2000.archkata.common.eventbus.Command
import com.github.caay2000.archkata.common.eventbus.CommandBus
import com.github.caay2000.archkata.common.eventbus.Event
import com.github.caay2000.archkata.common.eventbus.Query

class KTCommandBus : CommandBus {

    override fun <COMMAND : Command> publish(command: COMMAND) = KTEventBus.getInstance<COMMAND, Query, Event>().publishCommand(command)
}
