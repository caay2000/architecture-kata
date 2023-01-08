package com.github.caay2000.archkata.ex3.infra

import java.time.LocalDateTime

interface DateProvider {

    fun now(): LocalDateTime
}
