package org.eintr.springframework.test.bean;

import org.eintr.springframework.annotation.beans.Autowired;
import org.eintr.springframework.annotation.beans.Qualifier;
import org.eintr.springframework.annotation.beans.Value;
import org.eintr.springframework.annotation.stereotype.Component;

@Component("userService")
public class UserService implements IUserService {

	@Value("天津")
	private String location;
	private String Language;
	@Autowired
	@Qualifier("userDao")
	private IUserDao userDao;


	public String queryUserInfo(String name) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
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