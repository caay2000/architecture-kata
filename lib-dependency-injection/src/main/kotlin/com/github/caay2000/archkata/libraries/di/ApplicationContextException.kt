package com.github.caay2000.archkata.libraries.di

import kotlin.reflect.KClass

sealed class ApplicationContextException : RuntimeException {
    constructor(message: String) : super(message)

    data class BeanNotFound(val bean: String) : ApplicationContextException("bean $bean not found (not registered or unable to instantiate)")
    data class MultipleBeansFound(val type: String) : ApplicationContextException("multiple beans found for type $type")
    data class InterfaceCannotBeRegistered(val type: String) : ApplicationContextException("an interface $type cannot be instantiated")
    data class BeanCannotBeInstantiated(val beans: Set<KClass<*>>) : ApplicationContextException("some beans cannot be instantiated: $beans")
}
