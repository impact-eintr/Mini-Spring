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
	public void test_xml() {
		// 1.初始化 BeanFactory
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
		applicationContext.registerShutdownHook();

		// 2. 获取Bean对象调用方法
		UserService userService = applicationContext.getBean("userService", UserService.class);
		String result = userService.queryUserInfo();
		System.out.println("测试结果：" + result);
		System.out.println(userService.getLanguage());
		System.out.println(userService.getLocation());
	}

	@Test
	public void test_hook() {
		Runtime.getRuntime().addShutdownHook(new Thread(() -> System.out.println("close！")));
	}
}
