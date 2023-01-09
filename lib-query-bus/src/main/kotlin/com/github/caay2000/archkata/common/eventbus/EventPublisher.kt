package com.github.caay2000.archkata.common.eventbus

interface EventPublisher {

    fun <EVENT : Event> publish(events: List<EVENT>)
}
