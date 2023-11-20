package org.eintr.springframework;
import org.eintr.springframework.bean.UserService;
import org.eintr.springframework.factory.config.BeanDefinition;
import org.eintr.springframework.factory.support.DefaultListableBeanFactory;
import org.junit.Test;

public class ApiTest {
	@Test
	public void test_BeanFactory() {
		DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
		BeanDefinition beanDefinition = new BeanDefinition(UserService.class);
		beanFactory.registerBeanDefinition("userservice", beanDefinition);

		UserService userService = (UserService) beanFactory.getBean("userservice", "eintr");
		userService.queryUserInfo();

		UserService userService_s = (UserService) beanFactory.getSingleton("userservice");
		userService_s.queryUserInfo();
	}

}