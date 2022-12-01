package com.github.caay2000.archkata.common.eventbus.impl

import com.github.caay2000.archkata.common.eventbus.Command
import com.github.caay2000.archkata.common.eventbus.Event
import com.github.caay2000.archkata.common.eventbus.EventPublisher
import com.github.caay2000.archkata.common.eventbus.Query

class KTEventPublisher : EventPublisher {

    override fun <EVENT : Event> publish(events: List<EVENT>) {
        events.forEach { event ->
            publish(event)
        }
    }

    private fun <EVENT : Event> publish(event: EVENT) = KTEventBus.getInstance<Command, Query, EVENT>().publishEvent(event)
}
