package com.github.caay2000.archkata.ex3.infra

import java.util.UUID

class UUIDGenerator : IdGenerator {

    override fun generate(): String = UUID.randomUUID().toString()
}
