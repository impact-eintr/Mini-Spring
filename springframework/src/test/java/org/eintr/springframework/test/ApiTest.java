package org.eintr.springframework.test;
import org.eintr.springframework.PropertyValue;
import org.eintr.springframework.PropertyValues;
import org.eintr.springframework.factory.config.BeanReference;
import org.eintr.springframework.test.bean.UserDao;
import org.eintr.springframework.test.bean.UserService;
import org.eintr.springframework.factory.config.BeanDefinition;
import org.eintr.springframework.factory.support.DefaultListableBeanFactory;
import org.junit.Test;

public class ApiTest {
	@Test
	public void test_BeanFactory() {
		DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

		// 注册UserDao
		beanFactory.registerBeanDefinition("userDao", new BeanDefinition(UserDao.class));

		PropertyValues propertyValues = new PropertyValues();
		propertyValues.addPropertyValue(new PropertyValue("uId", "aaa"));
		propertyValues.addPropertyValue(new PropertyValue("userDao", new BeanReference("userDao")));

		// 注册UserService
		BeanDefinition beanDefinition = new BeanDefinition(UserService.class, propertyValues);
		beanFactory.registerBeanDefinition("userservice", beanDefinition);

		UserService userService = (UserService) beanFactory.getBean("userservice");
		userService.queryUserInfo();

		System.out.println(userService);

		UserService userService_s = (UserService) beanFactory.getSingleton("userservice");
		userService_s.queryUserInfo();
	}


	@Test
	public void test_xml() {
		DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

		// 注册UserDao
		beanFactory.registerBeanDefinition("userDao", new BeanDefinition(UserDao.class));

		PropertyValues propertyValues = new PropertyValues();
		propertyValues.addPropertyValue(new PropertyValue("uId", "aaa"));
		propertyValues.addPropertyValue(new PropertyValue("i", 100));
		BeanDefinition beanDefinition = new BeanDefinition(UserService.class, propertyValues);
		beanFactory.registerBeanDefinition("userservice", beanDefinition);

		UserService userService = (UserService) beanFactory.getBean("userservice", 1);
		userService.queryUserInfo();

		System.out.println(userService);

		UserService userService_s = (UserService) beanFactory.getSingleton("userservice");
		userService_s.queryUserInfo();
	}

}
