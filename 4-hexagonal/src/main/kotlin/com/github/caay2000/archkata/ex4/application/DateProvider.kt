package com.github.caay2000.archkata.ex4.application

import java.time.LocalDateTime

interface DateProvider {

    fun now(): LocalDateTime
}
