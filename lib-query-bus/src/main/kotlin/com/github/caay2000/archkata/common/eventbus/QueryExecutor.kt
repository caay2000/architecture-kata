package com.github.caay2000.archkata.common.eventbus

interface QueryExecutor {

    fun <RESPONSE : @UnsafeVariance QueryResponse> execute(query: Query): RESPONSE
}
