package com.github.caay2000.archkata.common.eventbus

import java.util.UUID

interface Event {

    val eventId: UUID
    val type: String
        get() = this::class.java.simpleName

    val aggregateId: String
}
