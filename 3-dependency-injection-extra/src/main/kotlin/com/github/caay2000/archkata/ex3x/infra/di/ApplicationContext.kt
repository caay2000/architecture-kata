package com.github.caay2000.archkata.ex3x.infra.di

import mu.KLogger
import mu.KotlinLogging
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KType
import kotlin.reflect.full.createType
import kotlin.reflect.full.primaryConstructor

object ApplicationContext {

    private val logger: KLogger = KotlinLogging.logger {}

    private val context: MutableMap<BeanContext, Any> = mutableMapOf()

    fun registerBean(clazz: KClass<*>) {
        val constructor: KFunction<Any> = clazz.primaryConstructor!!
        if (constructor.parameters.isEmpty()) {
            addBeanToContext(constructor.call())
        } else {
            val parameters = findParameters(constructor)
            addBeanToContext(constructor.call(*parameters))
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getBean(clazz: KClass<*>): T = findBeanByType(clazz.createType()) as T

    fun clear() = context.clear()

    private fun addBeanToContext(bean: Any) {
        val beanContext = BeanContext(
            name = bean::class.qualifiedName!!,
            interfaceNames = bean::class.java.interfaces.map { it.canonicalName }
        )
        context[beanContext] = bean
        logger.debug("registering bean {}", beanContext)
    }

    private fun findBeanByType(type: KType): Any {
        val items = context.keys.filter { it.name == type.toString() || it.interfaceNames.contains(type.toString()) }
        return when {
            items.isEmpty() -> throw ApplicationContextException.BeanNotFound(type.toString())
            items.size > 1 -> throw ApplicationContextException.MultipleBeansFound(type.toString())
            else -> context[items[0]]!!
        }
    }

    private fun findParameters(constructor: KFunction<Any>): Array<Any> =
        constructor.parameters.map {
            findBeanByType(it.type)
        }.toTypedArray()

    private data class BeanContext(
        val name: String,
        val interfaceNames: List<String>
    )
}
