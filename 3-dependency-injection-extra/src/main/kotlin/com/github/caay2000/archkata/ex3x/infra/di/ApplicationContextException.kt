package com.github.caay2000.archkata.ex3x.infra.di

import kotlin.reflect.KClass

sealed class ApplicationContextException : RuntimeException {
    constructor(message: String) : super(message)
    constructor(throwable: Throwable) : super(throwable)

    data class BeanNotFound(val bean: String) : ApplicationContextException("bean $bean not found")
    data class MultipleBeansFound(val type: String) : ApplicationContextException("multiple beans found for type $type")
    data class MultipleBeansCannotBeInitialized(val beans: Set<KClass<*>>) : ApplicationContextException("some beans cannot be instantiated: $beans")
}
