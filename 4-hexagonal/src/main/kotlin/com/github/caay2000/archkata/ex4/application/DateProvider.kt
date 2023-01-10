package com.github.caay2000.archkata.ex4.application

import kotlinx.datetime.LocalDateTime

interface DateProvider {

    fun now(): LocalDateTime
}
