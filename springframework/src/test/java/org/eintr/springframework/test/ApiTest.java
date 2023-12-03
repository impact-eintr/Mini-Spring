package org.eintr.springframework.test;


import org.eintr.springframework.beans.PropertyValue;
import org.eintr.springframework.beans.PropertyValues;
import org.eintr.springframework.context.support.ClassPathXmlApplicationContext;
import org.eintr.springframework.core.io.DefaultResourceLoader;
import org.eintr.springframework.core.io.Resource;
import org.eintr.springframework.beans.factory.config.BeanReference;
import org.eintr.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.eintr.springframework.test.bean.UserDao;
import org.eintr.springframework.test.bean.UserService;
import org.eintr.springframework.beans.factory.config.BeanDefinition;
import org.eintr.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.eintr.springframework.test.common.MyBeanFactoryPostProcessor;
import org.eintr.springframework.test.common.MyBeanPostProcessor;
import org.junit.Before;
import org.junit.Test;

import cn.hutool.core.io.IoUtil;
import java.io.IOException;
import java.io.InputStream;


public class ApiTest {
	@Test
	public void test_BeanFactory() {
		// 1.初始化 BeanFactory
		DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

		// 2. 读取配置文件&注册Bean
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
		reader.loadBeanDefinitions("classpath:spring.xml");

		// 3. 注册类后处理函数
		MyBeanFactoryPostProcessor myBeanFactoryPostProcessor = new MyBeanFactoryPostProcessor();
		myBeanFactoryPostProcessor.postProcessBeanFactory(beanFactory);

		// 4. 注册实例后处理函数
		MyBeanPostProcessor beanPostProcessor = new MyBeanPostProcessor();
		beanFactory.addBeanPostProcessor(beanPostProcessor);

		// 5. 获取Bean对象调用方法
		UserService userService = beanFactory.getBean("userService", UserService.class);
		String result = userService.queryUserInfo();
		System.out.println("测试结果：" + result);
	}


	@Test
	public void test_xml() {
		// 1.初始化 BeanFactory
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");

		// 2. 获取Bean对象调用方法
		UserService userService = applicationContext.getBean("userService", UserService.class);
		String result = userService.queryUserInfo();
		System.out.println("测试结果：" + result);
		System.out.println(userService.getLanguage());
		System.out.println(userService.getLocation());
	}
}
