package com.github.caay2000.archkata.common.eventbus

import java.util.UUID

interface Query {

    val queryId: UUID
    val type: String
        get() = this::class.java.simpleName
}
