package org.eintr.springframework.beans.factory.xml;

import cn.hutool.core.util.StrUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.eintr.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.eintr.springframework.beans.BeansException;
import org.eintr.springframework.beans.PropertyValue;
import org.eintr.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.eintr.springframework.core.io.Resource;
import org.eintr.springframework.core.io.ResourceLoader;
import org.eintr.springframework.beans.factory.config.BeanDefinition;
import org.eintr.springframework.beans.factory.config.BeanReference;
import org.eintr.springframework.beans.factory.support.AbstractBeanDefinitionReader;
import org.eintr.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.eintr.springframework.web.servlet.resource.DefaultServletHttpRequestHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader {

	public XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
		super(registry);
	}

	public XmlBeanDefinitionReader(BeanDefinitionRegistry registry, ResourceLoader resourceLoader) {
		super(registry, resourceLoader);
	}

	public void loadBeanDefinitions(Resource resource) throws BeansException {
		try (InputStream inputStream = resource.getInputStream()) {
			doLoadBeanDenifitions(inputStream);
		} catch (IOException | ClassNotFoundException | DocumentException e) {
			throw new BeansException("IOException parseing XML docment from"+resource, e);
		}
	}

	public void loadBeanDefinitions(Resource... resources) throws BeansException {
		for (Resource resource : resources) {
			loadBeanDefinitions(resource);
		}
	}

	public void loadBeanDefinitions(String location) throws BeansException {
		ResourceLoader resourceLoader = getResourceLoader();
		Resource resource = resourceLoader.getResource(location);
		loadBeanDefinitions(resource);
	}

	public void loadBeanDefinitions(String... locations) throws BeansException {
		for (String location : locations) {
			loadBeanDefinitions(location);
		}
	}

	// 根据 spring.xml 配置 BeanDefinition
	protected void doLoadBeanDenifitions(InputStream inputStream) throws ClassNotFoundException, DocumentException {
		SAXReader reader = new SAXReader();
		Document document = (Document) reader.read(inputStream);
		Element root = document.getRootElement();

		// 解析 context:component-scan 标签，扫描包中的类并提取相关信息，用于组装 BeanDefinition
		List<Element> components = root.elements("component-scan");
		for (Element component : components) {
			if (null != component) {
				String scanPath = component.attributeValue("base-package");
				if (StrUtil.isEmpty(scanPath)) {
					throw new BeansException("The value of base-package attribute can not be empty or null");
				}
				scanPackage(scanPath);
			}
		}

		// 默认实现AOP
		getRegistry().registerBeanDefinition(
				StrUtil.lowerFirst(DefaultAdvisorAutoProxyCreator.class.getSimpleName()),
				new BeanDefinition(DefaultAdvisorAutoProxyCreator.class));


		List<Element> default_servlet_name = root.elements("default-servlet-name");
		if (!default_servlet_name.isEmpty()) {
			BeanDefinition defaultServletHandlerDef = new BeanDefinition(DefaultServletHttpRequestHandler.class);
			String defaultServletHandlerName  = defaultServletHandlerDef.getBeanClass().getName();
			getRegistry().registerBeanDefinition(defaultServletHandlerName, defaultServletHandlerDef);
		}


		List<Element> beanList = root.elements("bean");
		for (Element bean : beanList) {

			String id = bean.attributeValue("id");
			String name = bean.attributeValue("name");
			String className = bean.attributeValue("class");
			String initMethod = bean.attributeValue("init-method");
			String destroyMethodName = bean.attributeValue("destroy-method");
			String beanScope = bean.attributeValue("scope");

			// 获取 Class，方便获取类中的名称
			Class<?> clazz = Class.forName(className);
			// 优先级 id > name
			String beanName = StrUtil.isNotEmpty(id) ? id : name;
			if (StrUtil.isEmpty(beanName)) {
				beanName = StrUtil.lowerFirst(clazz.getSimpleName());
			}

			/*
			 * 1. 定义bean
			 * 2. 填充bean的属性
			 * 3. 检查是否重复定义bean
			 * 4. 注册该bean
			 * */
			BeanDefinition beanDefinition = new BeanDefinition(clazz);
			beanDefinition.setInitMethodName(initMethod);
			beanDefinition.setDestroyMethodName(destroyMethodName);

			if (StrUtil.isNotEmpty(beanScope)) {
				beanDefinition.setScope(beanScope);
			}

			List<Element> propertyList = bean.elements("property");
			// 读取属性并填充
			for (Element property : propertyList) {
				// 解析标签：property
				String attrName = property.attributeValue("name");
				String attrValue = property.attributeValue("value");
				String attrRef = property.attributeValue("ref");
				// 获取属性值：引入对象、值对象
				Object value = StrUtil.isNotEmpty(attrRef) ? new BeanReference(attrRef) : attrValue;
				// 创建属性信息
				PropertyValue propertyValue = new PropertyValue(attrName, value);
				beanDefinition.getPropertyValues().addPropertyValue(propertyValue);
			}
			if (getRegistry().containsBeanDefinition(beanName)) {
				throw new BeansException("Duplicate beanName[" + beanName + "] is not allowed");
			}
			// 注册 BeanDefinition
			getRegistry().registerBeanDefinition(beanName, beanDefinition);
		}
	}

	private void scanPackage(String scanPath) {
		String[] basePackages = StrUtil.splitToArray(scanPath, ',');
		ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(getRegistry());
		scanner.doScan(basePackages);
	}
}