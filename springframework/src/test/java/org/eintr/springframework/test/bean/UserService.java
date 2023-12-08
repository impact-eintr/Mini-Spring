package org.eintr.springframework.test.bean;

import org.eintr.springframework.stereotype.Component;

@Component("userService")
public class UserService implements IUserService {
	private String uId;
	private String location;

	public String Language;

	public String queryUserInfo() {
		return "queryUserInfo";
	}


	public String queryUserInfo(String name) {
		return "queryUserInfo: "+name;
	}

	@Override
	public String register(String userName) {
		return "register";
	}

	public String getuId() {
		return uId;
	}

	public void setuId(String uId) {
		this.uId = uId;
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
}