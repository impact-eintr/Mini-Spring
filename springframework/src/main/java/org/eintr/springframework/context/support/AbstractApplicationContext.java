package org.eintr.springframework.context.support;

import org.eintr.springframework.beans.BeansException;
import org.eintr.springframework.beans.factory.ConfigurableListableBeanFactory;
import org.eintr.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.eintr.springframework.beans.factory.config.BeanPostProcessor;
import org.eintr.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.eintr.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.eintr.springframework.context.ApplicationContext;
import org.eintr.springframework.context.ApplicationEvent;
import org.eintr.springframework.context.ApplicationListener;
import org.eintr.springframework.context.ConfigurableApplicationContext;
import org.eintr.springframework.context.event.ApplicationEventMulticaster;
import org.eintr.springframework.context.event.ContextRefreshedEvent;
import org.eintr.springframework.context.event.SimpleApplicationEventMulticaster;
import org.eintr.springframework.core.io.DefaultResourceLoader;

import java.util.Collection;
import java.util.Map;

// 抽象应用上下文
public abstract class AbstractApplicationContext extends DefaultResourceLoader implements ConfigurableApplicationContext {

    public static final String APPLICATION_EVENT_MULTICASTER_BEAN_NAME = "applicationEventMulticaster";

    private ApplicationEventMulticaster applicationEventMulticaster;

    protected abstract void refreshBeanFactory() throws BeansException;

    private void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        Map<String, BeanFactoryPostProcessor> beanFactoryPostProcessorMap = beanFactory.getBeansOfType(BeanFactoryPostProcessor.class);
        for (BeanFactoryPostProcessor beanFactoryPostProcessor : beanFactoryPostProcessorMap.values()) {
            beanFactoryPostProcessor.postProcessBeanFactory(beanFactory);
        }
    }

    private void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        Map<String, BeanPostProcessor> beanPostProcessorMap = beanFactory.getBeansOfType(BeanPostProcessor.class);
        for (BeanPostProcessor beanPostProcessor : beanPostProcessorMap.values()) {
            beanFactory.addBeanPostProcessor(beanPostProcessor);
        }
    }


    private void initApplicationEventMulticaster() {
        System.out.println("初始化事件");
        ConfigurableBeanFactory beanFactory = getBeanFactory();
        applicationEventMulticaster = new SimpleApplicationEventMulticaster(beanFactory);
        beanFactory.registerSingleton(APPLICATION_EVENT_MULTICASTER_BEAN_NAME, applicationEventMulticaster);
    }

    private void registerListeners() {
        System.out.println("注册监听器");
        Collection<ApplicationListener> applicationListeners =
                getBeansOfType(ApplicationListener.class).values();
        for (ApplicationListener listener : applicationListeners) {
            applicationEventMulticaster.addApplicationListener(listener);
        }
    }

    private void finishRefresh() {
        System.out.println("完成刷新");
        publishEvent(new ContextRefreshedEvent(this));
    }

    public void publishEvent(ApplicationEvent applicationEvent) {
        applicationEventMulticaster.multicastEvent(applicationEvent);
    }

    /*
    1 加载XML
    2 修改Bean定义
    3 注册Bean扩展
    4 初始注册事件
    5 实例化单例Bean对象
    6 容器已经加载完毕 发布容器
     */
    @Override
    public void refresh() throws BeansException {
        refreshBeanFactory(); // 创建 BeanDenifition 并加载

        ConfigurableListableBeanFactory beanFactory = getBeanFactory(); // 获取BeanFactory

        // 添加ApplicationContextAwareProcessor
        // 让继承自ApplicationContxtAwareProcessor的Bean对象都能感知所属的ApplicationContext
        beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));

        invokeBeanFactoryPostProcessors(beanFactory); // 执行类信息后处理函数

        registerBeanPostProcessors(beanFactory); // 注册实例信息后处理函数

        initApplicationEventMulticaster(); // 初始化事件发布者

        registerListeners();// 初始化事件监听器

        beanFactory.preInstantiateSingletons(); // 提前实例化除了 BeanFactoryPostProcessor 以及 BeanPostProcessor 的单例Bean对象

        finishRefresh(); // 发布容器 刷新完成事件
    }


    protected abstract ConfigurableListableBeanFactory getBeanFactory();

    @Override
    public Object getBean(String name) throws BeansException {
        return getBeanFactory().getBean(name);
    }

    @Override
    public Object getBean(String name, Object... args) throws BeansException {
        return getBeanFactory().getBean(name, args);
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return getBeanFactory().getBean(name, requiredType);

    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
        return getBeanFactory().getBeansOfType(type);
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return getBeanFactory().getBeanDefinitionNames();
    }

    @Override
    public void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
    }

    @Override
    public void close() {
        getBeanFactory().destroySingletons();
    }
}
