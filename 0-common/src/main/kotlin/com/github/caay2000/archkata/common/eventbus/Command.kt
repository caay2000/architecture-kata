package com.github.caay2000.archkata.common.eventbus

import java.util.UUID

interface Command {

    val commandId: UUID
    val type: String
        get() = this::class.java.simpleName
}
