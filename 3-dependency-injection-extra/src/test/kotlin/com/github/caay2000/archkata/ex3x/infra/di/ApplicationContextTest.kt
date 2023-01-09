package com.github.caay2000.archkata.ex3x.infra.di

import com.github.caay2000.archkata.ex3x.infra.di.ApplicationContextException.BeanNotFound
import com.github.caay2000.archkata.ex3x.infra.di.ApplicationContextException.MultipleBeansFound
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class ApplicationContextTest {

    @BeforeEach
    fun setUp() {
        ApplicationContext.clear()
    }

    @Test
    fun `should register a simple bean`() {

        ApplicationContext.registerBean(SimpleBean::class)

        val bean = ApplicationContext.getBean<SimpleBean>(SimpleBean::class)
        Assertions.assertThat(bean).hasSameClassAs(SimpleBean())
    }

    @Test
    fun `should register a bean with an interface`() {

        ApplicationContext.registerBean(InterfaceBean::class)

        val bean = ApplicationContext.getBean<SimpleInterface>(InterfaceBean::class)
        Assertions.assertThat(bean).hasSameClassAs(InterfaceBean())
            .isInstanceOf(SimpleInterface::class.java)
    }

    @Test
    fun `should register a bean with simpleBean dependency`() {

        ApplicationContext.registerBean(SimpleBean::class)
        ApplicationContext.registerBean(SimpleDependencyBean::class)

        val bean = ApplicationContext.getBean<SimpleDependencyBean>(SimpleDependencyBean::class)
        Assertions.assertThat(bean).hasSameClassAs(SimpleDependencyBean(SimpleBean()))
    }

    @Test
    fun `should register a bean with simpleInterface dependency`() {

        ApplicationContext.registerBean(InterfaceBean::class)
        ApplicationContext.registerBean(InterfaceDependencyBean::class)

        val bean = ApplicationContext.getBean<InterfaceDependencyBean>(InterfaceDependencyBean::class)
        Assertions.assertThat(bean).hasSameClassAs(InterfaceDependencyBean(InterfaceBean()))
    }

    @Test
    fun `should register a bean with multiple dependencies`() {

        ApplicationContext.registerBean(SimpleBean::class)
        ApplicationContext.registerBean(InterfaceBean::class)
        ApplicationContext.registerBean(MultiDependencyBean::class)

        val bean = ApplicationContext.getBean<MultiDependencyBean>(MultiDependencyBean::class)
        Assertions.assertThat(bean).hasSameClassAs(MultiDependencyBean(SimpleBean(), InterfaceBean()))
    }

    @Test
    fun `should fail when a requested bean has not been registered`() {

        ApplicationContext.registerBean(SimpleBean::class)

        Assertions.assertThatThrownBy {
            ApplicationContext.getBean<ComplexBean>(ComplexBean::class)
        }.isInstanceOf(BeanNotFound::class.java)
    }

    @Test
    fun `should fail when one interface is implemented by multiple objects`() {

        ApplicationContext.registerBean(InterfaceBean::class)
        ApplicationContext.registerBean(AnotherInterfaceBean::class)

        Assertions.assertThatThrownBy {
            ApplicationContext.registerBean(InterfaceDependencyBean::class)
        }.isInstanceOf(MultipleBeansFound::class.java)
    }

    class SimpleBean
    interface SimpleInterface
    class InterfaceBean : SimpleInterface
    class AnotherInterfaceBean : SimpleInterface

    @Suppress("UNUSED_PARAMETER")
    data class SimpleDependencyBean(val dep: SimpleBean)

    @Suppress("UNUSED_PARAMETER")
    data class InterfaceDependencyBean(val dep: SimpleInterface)

    @Suppress("UNUSED_PARAMETER")
    data class MultiDependencyBean(val dep1: SimpleBean, val dep2: SimpleInterface)

    @Suppress("UNUSED_PARAMETER")
    data class ComplexBean(val dep1: MultiDependencyBean, val dep2: SimpleInterface)
}
