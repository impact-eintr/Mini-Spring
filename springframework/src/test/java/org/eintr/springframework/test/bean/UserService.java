package org.eintr.springframework.test.bean;

import org.eintr.springframework.beans.factory.BeanNameAware;
import org.eintr.springframework.beans.factory.DisposableBean;
import org.eintr.springframework.beans.factory.InitializingBean;


public class UserService implements InitializingBean, DisposableBean, BeanNameAware {
	private String uId;
	private String location;
	public IUserDao userDao;

	public String Language;

	public String queryUserInfo() {
		return userDao.queryUserName(uId);
	}

	public String getuId() {
		return uId;
	}

	public void setuId(String uId) {
		this.uId = uId;
	}

	public IUserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(IUserDao userDao) {
		this.userDao = userDao;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getLanguage() {
		return Language;
	}

	public void setLanguage(String language) {
		Language = language;
	}

	@Override
	public void destroy() throws Exception {
		System.out.println("执行：UserService.destroy");
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("执行：UserService.afterPropertiesSet   "+this.uId+this.location+this.Language);
	}

	@Override
	public void setBeanName(String name) {
		System.out.println("感知BeanName "+name);
	}
}