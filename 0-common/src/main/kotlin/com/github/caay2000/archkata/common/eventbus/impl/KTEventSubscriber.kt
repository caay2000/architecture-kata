package com.github.caay2000.archkata.common.eventbus.impl

import com.github.caay2000.archkata.common.eventbus.Command
import com.github.caay2000.archkata.common.eventbus.Event
import com.github.caay2000.archkata.common.eventbus.EventSubscriber
import com.github.caay2000.archkata.common.eventbus.Query
import kotlin.reflect.KClass

abstract class KTEventSubscriber<in EVENT : Event>(type: KClass<EVENT>) {

    init {
        subscribeTo(type)
    }

    private fun subscribeTo(type: KClass<EVENT>) {
        KTEventBus.getInstance<Command, Query, EVENT>().subscribe(this, type)
    }

    internal fun execute(event: Any) {
        @Suppress("UNCHECKED_CAST")
        this.handle(event as EVENT)
    }

    abstract fun handle(event: EVENT)
}

fun <EVENT : Event> instantiateEventSubscriber(clazz: KClass<EVENT>, eventSubscriber: EventSubscriber<EVENT>): KTEventSubscriber<EVENT> =
    object : KTEventSubscriber<EVENT>(clazz) {
        override fun handle(event: EVENT) = eventSubscriber.handle(event)
    }
