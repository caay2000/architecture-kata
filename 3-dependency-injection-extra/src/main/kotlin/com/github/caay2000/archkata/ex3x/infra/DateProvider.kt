package com.github.caay2000.archkata.ex3x.infra

import java.time.LocalDateTime

interface DateProvider {

    fun now(): LocalDateTime
}
