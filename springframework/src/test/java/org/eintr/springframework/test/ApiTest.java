package org.eintr.springframework.test;


import org.eintr.springframework.PropertyValue;
import org.eintr.springframework.PropertyValues;
import org.eintr.springframework.core.io.DefaultResourceLoader;
import org.eintr.springframework.core.io.Resource;
import org.eintr.springframework.factory.config.BeanReference;
import org.eintr.springframework.factory.xml.XmlBeanDefinitionReader;
import org.eintr.springframework.test.bean.UserDao;
import org.eintr.springframework.test.bean.UserService;
import org.eintr.springframework.factory.config.BeanDefinition;
import org.eintr.springframework.factory.support.DefaultListableBeanFactory;
import org.junit.Before;
import org.junit.Test;

import cn.hutool.core.io.IoUtil;
import java.io.IOException;
import java.io.InputStream;


public class ApiTest {
	@Test
	public void test_BeanFactory() {
		DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

		// 注册UserDao
		beanFactory.registerBeanDefinition("userDao", new BeanDefinition(UserDao.class));

		PropertyValues propertyValues = new PropertyValues();
		propertyValues.addPropertyValue(new PropertyValue("uId", "1000"));
		propertyValues.addPropertyValue(new PropertyValue("userDao", new BeanReference("userDao")));

		// 注册UserService
		BeanDefinition beanDefinition = new BeanDefinition(UserService.class, propertyValues);
		beanFactory.registerBeanDefinition("userservice", beanDefinition);

		UserService userService = (UserService) beanFactory.getBean("userservice");
		System.out.println(userService.queryUserInfo());
	}


	private DefaultResourceLoader resourceLoader;

	@Before
	public void init() {
		resourceLoader = new DefaultResourceLoader();
	}

	@Test
	public void test_classpath() throws IOException {
		Resource resource = resourceLoader.getResource("classpath:important.properties");
		InputStream inputStream = resource.getInputStream();
		String content = IoUtil.readUtf8(inputStream);
		System.out.println(content);
	}

	@Test
	public void test_file() throws IOException {
		Resource resource = resourceLoader.getResource("src/test/resources/important.properties");
		InputStream inputStream = resource.getInputStream();
		String content = IoUtil.readUtf8(inputStream);
		System.out.println(content);
	}

	@Test
	public void test_url() throws IOException {
		Resource resource = resourceLoader.getResource("https://github.com/fuzhengwei/small-spring/important.properties");
		InputStream inputStream = resource.getInputStream();
		String content = IoUtil.readUtf8(inputStream);
		System.out.println(content);
	}

	@Test
	public void test_xml() {
		// 1.初始化 BeanFactory
		DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

		// 2. 读取配置文件&注册Bean
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
		reader.loadBeanDefinitions("classpath:spring.xml");

		// 3. 获取Bean对象调用方法
		UserService userService = beanFactory.getBean("userService", UserService.class);
		String result = userService.queryUserInfo();
		System.out.println("测试结果：" + result);
	}
}
