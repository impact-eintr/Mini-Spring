package org.eintr.springframework.bean;

import java.util.WeakHashMap;

public class UserService {
	public UserService(String name) {
		System.out.println("this is "+name);
	}


	public UserService(Integer i) {
		System.out.println("this is "+i);
	}

	public void queryUserInfo() {
		System.out.println("查询用户信息");
	}

}
