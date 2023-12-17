package org.eintr.springframework.test;


import org.aopalliance.intercept.MethodInterceptor;
import org.eintr.springframework.aop.AdvisedSupport;
import org.eintr.springframework.aop.ClassFilter;
import org.eintr.springframework.aop.MethodMatcher;
import org.eintr.springframework.aop.TargetSource;
import org.eintr.springframework.aop.aspect.AspectJExpressionPointcut;
import org.eintr.springframework.aop.aspect.AspectJExpressionPointcutAdvisor;
import org.eintr.springframework.aop.framework.JdkDynamicAopProxy;
import org.eintr.springframework.aop.framework.ProxyFactory;
import org.eintr.springframework.aop.framework.ReflectiveMethodInvocation;
import org.eintr.springframework.aop.framework.adapter.MethodAdviceInterceptor;
import org.eintr.springframework.context.support.ClassPathXmlApplicationContext;
import org.eintr.springframework.core.convert.converter.Converter;
import org.eintr.springframework.core.convert.support.StringToNumberConverterFactory;
import org.eintr.springframework.test.bean.*;
import org.eintr.springframework.test.event.CustomEvent;
import org.junit.Test;
import java.lang.reflect.InvocationHandler;

import java.lang.reflect.Proxy;

public class ApiTest {

	@Test
	public void test_xml() {
		// 1.初始化 BeanFactory
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
		applicationContext.registerShutdownHook();

		// 2. 发布一个事件
		applicationContext.publishEvent(new CustomEvent(applicationContext, 114514L, "事件响应"));

		// 3. 用一个类实现一个接口
		IUserService userService = applicationContext.getBean("userService", IUserService.class);
		String result = userService.queryUserInfo("AAA");
		System.out.println("测试结果：" + result);
	}

	@Test
	public void test_StringToNumberConverterFactory() {
		StringToNumberConverterFactory converterFactory = new StringToNumberConverterFactory();

		Converter<String, Integer> stringToIntegerConverter = converterFactory.getConverter(Integer.class);
		System.out.println("测试结果：" + stringToIntegerConverter.convert("1234"));

		Converter<String, Long> stringToLongConverter = converterFactory.getConverter(Long.class);
		System.out.println("测试结果：" + stringToLongConverter.convert("1234"));
	}


	@Test
	public void test_dependency() {
		// 1.初始化 BeanFactory
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
		applicationContext.registerShutdownHook();
		A a = (A) applicationContext.getBean("A", A.class);
		B b = (B) applicationContext.getBean("B", B.class);
	}


	@Test
	public void test_advisor() {
		// 目标对象
		IUserService userService = new UserService();

		AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
		advisor.setExpression("execution(* org.eintr.springframework.test.bean.IUserService.*(..))");
		advisor.setAdvice(new MethodAdviceInterceptor(new UserServiceAdvice()));

		ClassFilter classFilter = advisor.getPointcut().getClassFilter();
		if (classFilter.matches(userService.getClass())) {
			AdvisedSupport advisedSupport = new AdvisedSupport();

			TargetSource targetSource = new TargetSource(userService);
			advisedSupport.setTargetSource(targetSource);
			advisedSupport.setMethodInterceptor((MethodInterceptor) advisor.getAdvice());
			advisedSupport.setMethodMatcher(advisor.getPointcut().getMethodMatcher());
			advisedSupport.setProxyTargetClass(false); // false/true，JDK动态代理、CGlib动态代理

			IUserService proxy = (IUserService) new ProxyFactory(advisedSupport).getProxy();
			System.out.println("测试结果：" + proxy.queryUserInfo("AAA"));
		}
	}

	@Test
	public void test_proxy_class() {
		InvocationHandler handler = (proxy, method, args) -> {
            if ("queryUserInfo".equals(method.getName())) {
                System.out.println("queryUserInfo");
                return "代理 queryUserInfo";
            } else if ("register".equals(method.getName())) {
                System.out.println("register");
                return "代理 register";
            } else {
                throw new Exception("非法的代理");
            }
        };

		IUserService userService = (IUserService) Proxy.newProxyInstance(
				Thread.currentThread().getContextClassLoader(),
				new Class[]{IUserService.class},
				handler);
		String result = userService.register("AAA");
		System.out.println("测试结果：" + result);
	}


	@Test
	public void test_proxy_method() {
		// 目标对象(可以替换成任何的目标对象)
		Object targetObj = new UserService();

		// AOP 代理
        // 方法匹配器
        IUserService proxy = (IUserService) Proxy.newProxyInstance(
				Thread.currentThread().getContextClassLoader(),
				targetObj.getClass().getInterfaces(),
				(proxy1, method, args) -> {
					MethodMatcher methodMatcher = new AspectJExpressionPointcut(
							"execution(* org.eintr.springframework.test.bean.IUserService.*(..))");
					if (methodMatcher.matches(method, targetObj.getClass())) {
						// 定义一个方法拦截器
						MethodInterceptor methodInterceptor = invocation -> {
							long start = System.currentTimeMillis();
							try {
								return invocation.proceed();
							} finally {
								System.out.println("监控 - Begin By AOP");
								System.out.println("方法名称：" + invocation.getMethod().getName());
								System.out.println("方法耗时：" + (System.currentTimeMillis() - start) + "ms");
								System.out.println("监控 - End\r\n");
							}
						};
						// 拦截下来的方式使用反射调用
						return methodInterceptor.invoke(new ReflectiveMethodInvocation(targetObj, method, args));
					}

					// 没拦截下的方法直接调用
					System.out.println("未能拦截的方法");
					return method.invoke(targetObj, args);
				});

		String result = proxy.queryUserInfo("AAA");
		System.out.println("测试结果：" + result);

	}

	@Test
	public void test_dynamic() {
		// 目标对象
		IUserService userService = new UserService();
		// 组装代理信息
		AdvisedSupport advisedSupport = new AdvisedSupport();
		advisedSupport.setTargetSource(new TargetSource(userService));
		advisedSupport.setMethodInterceptor(new UserServiceInterceptor());
		advisedSupport.setMethodMatcher(new AspectJExpressionPointcut(
				"execution(* org.eintr.springframework.test.bean.IUserService.*(..))"));

		// 代理对象(JdkDynamicAopProxy)
		IUserService proxy_jdk = (IUserService) new JdkDynamicAopProxy(advisedSupport).getProxy();
		// 测试调用
		System.out.println("测试结果：" + proxy_jdk.queryUserInfo("AAA"));
	}
}
