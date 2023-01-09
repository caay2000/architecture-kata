package com.github.caay2000.archkata.common.eventbus

interface QueryResponse {

    val value: Any
    val type: String
        get() = this::class.java.simpleName
}
