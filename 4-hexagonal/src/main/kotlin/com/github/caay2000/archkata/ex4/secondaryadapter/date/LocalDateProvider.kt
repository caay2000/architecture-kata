package com.github.caay2000.archkata.ex4.secondaryadapter.date

import com.github.caay2000.archkata.ex4.application.DateProvider
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class LocalDateProvider : DateProvider {

    override fun now() = Clock.System.now().toLocalDateTime(TimeZone.UTC)
}
