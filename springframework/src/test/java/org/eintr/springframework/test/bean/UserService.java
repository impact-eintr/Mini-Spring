package org.eintr.springframework.test.bean;

public class UserService {
	private String uId;
	private String location;
	public UserDao userDao;

	public String queryUserInfo() {
		return userDao.queryUserName(uId);
	}

	public String getuId() {
		return uId;
	}

	public void setuId(String uId) {
		this.uId = uId;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
}