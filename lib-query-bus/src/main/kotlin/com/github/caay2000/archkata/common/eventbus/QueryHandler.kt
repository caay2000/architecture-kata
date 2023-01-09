package com.github.caay2000.archkata.common.eventbus

interface QueryHandler<in QUERY : Query, out RESPONSE : Any> {

    fun handle(query: QUERY): RESPONSE
}
