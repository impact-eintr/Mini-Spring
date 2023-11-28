package org.eintr.springframework.factory.xml;

import cn.hutool.core.util.XmlUtil;
import cn.hutool.core.util.StrUtil;
import org.eintr.springframework.BeansException;
import org.eintr.springframework.PropertyValue;
import org.eintr.springframework.core.io.Resource;
import org.eintr.springframework.core.io.ResourceLoader;
import org.eintr.springframework.factory.config.BeanDefinition;
import org.eintr.springframework.factory.config.BeanReference;
import org.eintr.springframework.factory.support.AbstractBeanDefinitionReader;
import org.eintr.springframework.factory.support.BeanDefinitionRegistry;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;

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
		} catch (IOException | ClassNotFoundException e) {
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

	// 根据 spring.xml 配置 BeanDefinition
	protected void doLoadBeanDenifitions(InputStream inputStream) throws ClassNotFoundException {
		Document doc = XmlUtil.readXML(inputStream);
		Element root = doc.getDocumentElement();
		NodeList childNodes = root.getChildNodes();
		for (int i  = 0;i < childNodes.getLength();i++) {
			if (!(childNodes.item(i) instanceof Element)) { // 判断的元素
				continue;
			}
			if (!"bean".equals(childNodes.item(i).getNodeName())) { // 跳过不是bean的标签
				continue;
			}
			Element bean = (Element)  childNodes.item(i);
			String id = bean.getAttribute("id");
			String name = bean.getAttribute("name");
			String className = bean.getAttribute("class");
			Class<?> clazz = Class.forName(className);
			String beanName = StrUtil.isNotEmpty(id) ? id : name; // id 优先级高于 name
			if (StrUtil.isEmpty(beanName)) {
				beanName = StrUtil.lowerFirst(clazz.getSimpleName());
			}

			/*
			* 1. 定义bean
			* 2. 填充bean的属性
			* 3. 检查是否重复定义bean
			* 4. 注册该bean
			* */
			// 定义 Bean
			BeanDefinition beanDefinition = new BeanDefinition(clazz);

			// 读取属性并填充
			for (int j = 0;j < bean.getChildNodes().getLength();j++) {
				if (!(bean.getChildNodes().item(j) instanceof Element)) { // 判断的元素
					continue;
				}
				if (!"property".equals(bean.getChildNodes().item(j).getNodeName())) {
					continue;
				}
				Element property = (Element)  bean.getChildNodes().item(j);
				String attrName = property.getAttribute("name");
				String attrValue = property.getAttribute("value");
				String attrRef = property.getAttribute("ref");
				// 获取属性值：引入对象 值对象
				Object value = StrUtil.isNotEmpty(attrRef) ? new BeanReference(attrRef) : attrValue;
				// 创建属性信息
				PropertyValue propertyValue = new PropertyValue(attrName, value);
				beanDefinition.getPropertyValues().addPropertyValue(propertyValue);
			}
			// 检查重复元素
			if (getRegister().containsBeanDefinition(beanName)) {
				throw new BeansException("Duplicate beanName["+"] is not allowed");
			}
			//注册 Beanfinition
			getRegister().registerBeanDefinition(beanName, beanDefinition);
		}
	}
}
