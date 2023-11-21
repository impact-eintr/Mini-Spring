package org.eintr.springframework.bean;

public class UserService {
	String name;
	Integer i;
	public UserService(String name) {
		this.name = name;
		System.out.println("this is "+name);
	}


	public UserService(Integer i) {
		this.i = i;
		System.out.println("this is "+i);
	}

	public void queryUserInfo() {
		System.out.println("查询用户信息");
	}

	public String toString() {
		return name + "  "+i;
	}
}
