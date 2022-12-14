package com.github.caay2000.archkata.common.eventbus.impl

import com.github.caay2000.archkata.common.eventbus.Command
import com.github.caay2000.archkata.common.eventbus.Event
import com.github.caay2000.archkata.common.eventbus.Query
import com.github.caay2000.archkata.common.eventbus.QueryHandler
import com.github.caay2000.archkata.common.eventbus.QueryResponse
import kotlin.reflect.KClass

abstract class KTQueryHandler<in QUERY : Query, out RESPONSE : QueryResponse>(type: KClass<QUERY>) : QueryHandler<QUERY, RESPONSE> {

    init {
        subscribeTo(type)
    }

    private fun subscribeTo(type: KClass<QUERY>) {
        KTEventBus.getInstance<Command, QUERY, Event>().subscribe(this, type)
    }

    @Suppress("UNCHECKED_CAST")
    internal fun <RESPONSE_1 : @UnsafeVariance RESPONSE> execute(query: QUERY): RESPONSE_1 =
        this.handle(query) as RESPONSE_1

    abstract override fun handle(query: QUERY): RESPONSE
}

internal fun <QUERY : Query, RESPONSE : QueryResponse> instantiateQueryHandler(clazz: KClass<QUERY>, queryHandler: QueryHandler<QUERY, RESPONSE>): KTQueryHandler<QUERY, RESPONSE> =
    object : KTQueryHandler<QUERY, RESPONSE>(clazz) {
        override fun handle(query: QUERY): RESPONSE = queryHandler.handle(query)
    }
