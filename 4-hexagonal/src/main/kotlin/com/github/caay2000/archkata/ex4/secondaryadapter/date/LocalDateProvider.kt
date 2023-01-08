package com.github.caay2000.archkata.ex4.secondaryadapter.date

import com.github.caay2000.archkata.ex4.application.DateProvider
import java.time.LocalDateTime

class LocalDateProvider : DateProvider {

    override fun now() = LocalDateTime.now()
}
