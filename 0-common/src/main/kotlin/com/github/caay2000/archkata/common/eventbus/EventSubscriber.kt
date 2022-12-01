package com.github.caay2000.archkata.common.eventbus

interface EventSubscriber<in EVENT : Event> {

    fun handle(event: EVENT)
}
