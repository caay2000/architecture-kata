package com.github.caay2000.archkata.ex3x.infra

import java.time.LocalDateTime

class LocalDateProvider : DateProvider {

    override fun now() = LocalDateTime.now()
}
