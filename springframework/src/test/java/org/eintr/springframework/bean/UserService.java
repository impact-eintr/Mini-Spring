package org.eintr.springframework.bean;

public class UserService {
	public UserService(String name) {
		System.out.println("this is "+name);
	}
	public void queryUserInfo() {
		System.out.println("查询用户信息");
	}

}
