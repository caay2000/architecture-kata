package com.github.caay2000.archkata.common.eventbus

import com.github.caay2000.archkata.common.eventbus.impl.KTEventBus
import com.github.caay2000.archkata.common.eventbus.impl.KTEventPublisher
import com.github.caay2000.archkata.common.eventbus.impl.KTEventSubscriber
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.UUID

internal class EventBusTest {

    @Test
    internal fun `subscribers receive the published event`() {

        KTEventBus.init<Command, Query, Event>()
        val sut = StringSubscriber()
        val event = StringTestEvent(value = "hi")
        KTEventPublisher().publish(listOf(event))

        assertThat(sut.events).isEqualTo(listOf(event))
    }

    @Test
    internal fun `multiple subscribers receive the published event`() {

        KTEventBus.init<Command, Query, Event>()
        val subscriber1 = StringSubscriber()
        val subscriber2 = StringSubscriber()
        val event = StringTestEvent(value = "hi")
        KTEventPublisher().publish(listOf(event))

        assertThat(subscriber1.events).isEqualTo(listOf(event))
        assertThat(subscriber2.events).isEqualTo(listOf(event))
    }

    @Test
    internal fun `subscriber of different type does not receive the event`() {

        KTEventBus.init<Command, Query, Event>()
        val sut = IntSubscriber()
        KTEventPublisher().publish(listOf(StringTestEvent(value = "hi")))

        assertThat(sut.events).hasSize(0)
    }

    inner class StringSubscriber : KTEventSubscriber<StringTestEvent>(StringTestEvent::class) {
        val events = mutableListOf<StringTestEvent>()
        override fun handle(event: StringTestEvent) {
            events.add(event)
        }
    }

    inner class IntSubscriber : KTEventSubscriber<NumberTestEvent>(NumberTestEvent::class) {
        val events = mutableListOf<NumberTestEvent>()
        override fun handle(event: NumberTestEvent) {
            events.add(event)
        }
    }

    internal data class StringTestEvent(val value: String) : Event {
        override val aggregateId: String = UUID.randomUUID().toString()
        override val eventId: UUID = UUID.randomUUID()
    }

    internal data class NumberTestEvent(val value: Number) : Event {
        override val aggregateId: String = UUID.randomUUID().toString()
        override val eventId: UUID = UUID.randomUUID()
    }
}
