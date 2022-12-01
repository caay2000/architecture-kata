package com.github.caay2000.archkata.common.eventbus.impl

import com.github.caay2000.archkata.common.eventbus.Command
import com.github.caay2000.archkata.common.eventbus.Event
import com.github.caay2000.archkata.common.eventbus.Query
import com.github.caay2000.archkata.common.eventbus.QueryExecutor
import com.github.caay2000.archkata.common.eventbus.QueryResponse

class KTQueryExecutor : QueryExecutor {

    override fun <RESPONSE : QueryResponse> execute(query: Query): RESPONSE =
        KTEventBus.getInstance<Command, Query, Event>().executeQuery(query)
}
