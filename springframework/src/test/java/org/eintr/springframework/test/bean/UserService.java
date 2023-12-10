package org.eintr.springframework.test.bean;

import org.eintr.springframework.beans.factory.annotation.Value;
import org.eintr.springframework.stereotype.Component;

@Component("userService")
public class UserService implements IUserService {

	@Value("天津")
	private String location;

	private IUserDao userDao;


	public String queryUserInfo(String name) {
		return "queryUserInfo: "+location;
	}

	@Override
	public String register(String userName) {
		return "register";
	}


	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
}