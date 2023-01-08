package com.github.caay2000.archkata.ex4.secondaryadapter.uuid

import com.github.caay2000.archkata.ex4.application.IdGenerator
import java.util.UUID

class UUIDIdGenerator : IdGenerator {

    override fun generate(): String = UUID.randomUUID().toString()
}
