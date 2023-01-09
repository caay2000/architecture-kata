package com.github.caay2000.archkata.common.eventbus

import com.github.caay2000.archkata.common.eventbus.impl.KTEventBus
import com.github.caay2000.archkata.common.eventbus.impl.KTQueryExecutor
import com.github.caay2000.archkata.common.eventbus.impl.KTQueryHandler
import com.github.caay2000.archkata.common.eventbus.impl.instantiateQueryHandler
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.UUID

internal class QueryBusTest {

    @Test
    internal fun `subscribers receive the published event`() {

        KTEventBus.init<Command, Query, Event>()
        instantiateQueryHandler(TestQuery::class, TestQueryHandler())

        val resultTrue = KTQueryExecutor().execute<TestQueryResponse>(TestQuery(value = true))
        val resultFalse = KTQueryExecutor().execute<TestQueryResponse>(TestQuery(value = false))

        assertThat(resultTrue.value).isEqualTo("true")
        assertThat(resultFalse.value).isEqualTo("false")
    }

    inner class TestQueryHandler : KTQueryHandler<TestQuery, TestQueryResponse>(TestQuery::class) {
        override fun handle(query: TestQuery): TestQueryResponse {
            return TestQueryResponse(query.value.toString())
        }
    }

    internal data class TestQuery(val value: Boolean) : Query {
        override val queryId: UUID = UUID.randomUUID()
    }

    internal data class TestQueryResponse(override val value: String) : QueryResponse
}
