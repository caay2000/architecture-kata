package com.github.caay2000.archkata.libraries.di

import com.github.caay2000.archkata.libraries.di.ApplicationContextException.BeanCannotBeInstantiated
import com.github.caay2000.archkata.libraries.di.ApplicationContextException.BeanNotFound
import com.github.caay2000.archkata.libraries.di.ApplicationContextException.InterfaceCannotBeRegistered
import com.github.caay2000.archkata.libraries.di.ApplicationContextException.MultipleBeansFound
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
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
        ApplicationContext.init()

        val bean = ApplicationContext.getBean<SimpleBean>(SimpleBean::class)
        assertThat(bean).hasSameClassAs(SimpleBean())
    }

    @Test
    fun `should register a bean with an interface`() {

        ApplicationContext.registerBean(InterfaceBean::class)
        ApplicationContext.init()

        val bean = ApplicationContext.getBean<SimpleInterface>(InterfaceBean::class)
        assertThat(bean).hasSameClassAs(InterfaceBean())
            .isInstanceOf(SimpleInterface::class.java)
    }

    @Test
    fun `should register a bean with simpleBean dependency`() {

        ApplicationContext.registerBean(SimpleBean::class)
        ApplicationContext.registerBean(SimpleDependencyBean::class)
        ApplicationContext.init()

        val bean = ApplicationContext.getBean<SimpleDependencyBean>(SimpleDependencyBean::class)
        assertThat(bean).hasSameClassAs(SimpleDependencyBean(SimpleBean()))
    }

    @Test
    fun `should register a bean with simpleInterface dependency`() {

        ApplicationContext.registerBean(InterfaceBean::class)
        ApplicationContext.registerBean(InterfaceDependencyBean::class)
        ApplicationContext.init()

        val bean = ApplicationContext.getBean<InterfaceDependencyBean>(InterfaceDependencyBean::class)
        assertThat(bean).hasSameClassAs(InterfaceDependencyBean(InterfaceBean()))
    }

    @Test
    fun `should register a bean with multiple dependencies`() {

        ApplicationContext.registerBean(SimpleBean::class)
        ApplicationContext.registerBean(InterfaceBean::class)
        ApplicationContext.registerBean(MultiDependencyBean::class)
        ApplicationContext.init()

        val bean = ApplicationContext.getBean<MultiDependencyBean>(MultiDependencyBean::class)
        assertThat(bean).hasSameClassAs(MultiDependencyBean(SimpleBean(), InterfaceBean()))
    }

    @Test
    fun `should register a bean with multiple dependencies and incorrect order on bean registers`() {

        ApplicationContext.registerBean(SimpleBean::class)
        ApplicationContext.registerBean(MultiDependencyBean::class)
        ApplicationContext.registerBean(InterfaceBean::class)
        ApplicationContext.init()

        val bean = ApplicationContext.getBean<MultiDependencyBean>(MultiDependencyBean::class)
        assertThat(bean).hasSameClassAs(MultiDependencyBean(SimpleBean(), InterfaceBean()))
    }

    @Test
    fun `should register a bean with bean multidependency and incorrect order on bean registers`() {

        ApplicationContext.registerBean(ComplexBean::class)
        ApplicationContext.registerBean(SimpleBean::class)
        ApplicationContext.registerBean(MultiDependencyBean::class)
        ApplicationContext.registerBean(InterfaceBean::class)
        ApplicationContext.init()

        val bean = ApplicationContext.getBean<ComplexBean>(ComplexBean::class)
        assertThat(bean).hasSameClassAs(ComplexBean(MultiDependencyBean(SimpleBean(), InterfaceBean()), InterfaceBean()))
    }

    @Test
    fun `should fail when a requested bean has not been registered`() {

        ApplicationContext.registerBean(SimpleBean::class)
        ApplicationContext.init()

        assertThatThrownBy {
            ApplicationContext.getBean<ComplexBean>(ComplexBean::class)
        }.isInstanceOf(BeanNotFound::class.java)
    }

    @Test
    fun `should fail when trying to register an interface as a bean`() {

        assertThatThrownBy {
            ApplicationContext.registerBean(SimpleInterface::class)
        }.isInstanceOf(InterfaceCannotBeRegistered::class.java)
    }

    @Test
    fun `should fail when one dependency cannot be instantiated`() {

        ApplicationContext.registerBean(ComplexBean::class)
        ApplicationContext.registerBean(SimpleBean::class)
        ApplicationContext.registerBean(MultiDependencyBean::class)

        assertThatThrownBy {
            ApplicationContext.init()
        }.isInstanceOf(BeanCannotBeInstantiated::class.java)
    }

    @Test
    fun `should fail when one interface is implemented by multiple objects`() {

        ApplicationContext.registerBean(SimpleBean::class)
        ApplicationContext.registerBean(InterfaceBean::class)
        ApplicationContext.registerBean(AnotherInterfaceBean::class)
        ApplicationContext.registerBean(MultiDependencyBean::class)

        assertThatThrownBy {
            ApplicationContext.init()
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
