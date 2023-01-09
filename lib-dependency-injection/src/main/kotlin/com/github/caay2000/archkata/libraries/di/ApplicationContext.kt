package com.github.caay2000.archkata.libraries.di

import com.github.caay2000.archkata.libraries.di.ApplicationContextException.BeanNotFound
import com.github.caay2000.archkata.libraries.di.ApplicationContextException.MultipleBeansCannotBeInitialized
import com.github.caay2000.archkata.libraries.di.ApplicationContextException.MultipleBeansFound
import mu.KLogger
import mu.KotlinLogging
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KType
import kotlin.reflect.full.createType
import kotlin.reflect.full.primaryConstructor

object ApplicationContext {

    private val logger: KLogger = KotlinLogging.logger {}

    private val context: Context = Context()

    fun registerBean(clazz: KClass<*>) {
        context.registerBean(clazz)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getBean(clazz: KClass<*>): T {
        return context.getBean(clazz)
    }

    fun clear() = context.clear()

    private class Context {

        private val context: MutableMap<BeanContext, Any> = mutableMapOf()
        private val uninitializedBeans: MutableSet<KClass<*>> = mutableSetOf()

        @Suppress("UNCHECKED_CAST")
        fun <T> getBean(clazz: KClass<*>): T {
            if (uninitializedBeans.isNotEmpty()) {
                initializeBeans(uninitializedBeans)
            }
            return findBeanByType(clazz.createType()) as T
        }

        fun registerBean(clazz: KClass<*>) {
            val constructor: KFunction<Any> = clazz.primaryConstructor!!
            if (constructor.parameters.isEmpty()) {
                addBeanToContext(constructor.call())
            } else {
                uninitializedBeans.add(clazz)
            }
        }

        fun clear() {
            context.clear()
            uninitializedBeans.clear()
        }

        private fun addBeanToContext(bean: Any) {
            val beanContext = BeanContext(
                name = bean::class.qualifiedName!!,
                interfaceNames = bean::class.java.interfaces.map { it.canonicalName }
            )
            context[beanContext] = bean
            logger.debug("registering bean {}", beanContext)
        }

        private fun registerBeanWithDependencies(clazz: KClass<*>) {
            val constructor: KFunction<Any> = clazz.primaryConstructor!!
            val parameters = findParameters(constructor)
            addBeanToContext(constructor.call(*parameters))
        }

        private fun initializeBeans(beansToInitialize: Set<KClass<*>>) {
            val uninitializedBeans = mutableSetOf<KClass<*>>()
            beansToInitialize.forEach {
                try {
                    registerBeanWithDependencies(it)
                } catch (e: BeanNotFound) {
                    uninitializedBeans.add(it)
                }
            }
            if (uninitializedBeans.isNotEmpty()) {
                if (beansToInitialize.size != uninitializedBeans.size) initializeBeans(uninitializedBeans)
                else throw MultipleBeansCannotBeInitialized(beansToInitialize)
            }
        }

        private fun findBeanByType(type: KType): Any {
            val items = context.keys.filter { it.name == type.toString() || it.interfaceNames.contains(type.toString()) }
            return when {
                items.isEmpty() -> throw BeanNotFound(type.toString())
                items.size > 1 -> throw MultipleBeansFound(type.toString())
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
}
