package com.github.caay2000.archkata.ex4.infra

object ApplicationContext {

    private val context: MutableMap<String, Any> = mutableMapOf()

    fun registerBean(name: String, bean: Any) {
        context[name] = bean
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getBean(name: String): T = context[name]!! as T
}
