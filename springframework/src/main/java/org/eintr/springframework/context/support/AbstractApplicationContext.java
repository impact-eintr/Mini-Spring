package org.eintr.springframework.context.support;

import org.eintr.springframework.beans.BeansException;
import org.eintr.springframework.beans.factory.ConfigurableListableBeanFactory;
import org.eintr.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.eintr.springframework.beans.factory.config.BeanPostProcessor;
import org.eintr.springframework.context.ConfigurableApplicationContext;
import org.eintr.springframework.core.io.DefaultResourceLoader;

import java.util.Map;

// 抽象应用上下文
public abstract class AbstractApplicationContext extends DefaultResourceLoader implements ConfigurableApplicationContext {

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

    // TODO 注意这里区分了spring内部自动实例化的单例和用户自定义的Bean
    @Override
    public void refresh() throws BeansException {
        refreshBeanFactory(); // 创建 BeanDenifition 并加载
        ConfigurableListableBeanFactory beanFactory = getBeanFactory(); // 获取BeanFactory
        invokeBeanFactoryPostProcessors(beanFactory); // TODO
        registerBeanPostProcessors(beanFactory); // TODO
        beanFactory.preInstantiateSingletons(); // 提前实例化单例Bean对象 FIXME
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
}
