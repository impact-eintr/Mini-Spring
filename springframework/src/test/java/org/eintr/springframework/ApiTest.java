package org.eintr.springframework;
import org.eintr.springframework.bean.UserService;
import org.eintr.springframework.factory.config.BeanDefinition;
import org.eintr.springframework.factory.support.DefaultListableBeanFactory;
import org.junit.Test;

public class ApiTest {
	@Test
	public void test_BeanFactory() {
		DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

		PropertyValues propertyValues = new PropertyValues();
		propertyValues.addPropertyValue(new PropertyValue("name", "aaa"));
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
