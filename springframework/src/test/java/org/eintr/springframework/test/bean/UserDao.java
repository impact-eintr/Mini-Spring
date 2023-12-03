package org.eintr.springframework.test.bean;

import java.util.HashMap;
import java.util.Map;

public class UserDao {
	private Map<String, String> hashMap = new HashMap<>();

	public void initDataMethod(){
		System.out.println("执行：init-method");
		hashMap.put("1000", "AAA");
		hashMap.put("1001", "BBB");
		hashMap.put("1002", "CCC");
	}

	public void destroyDataMethod(){
		System.out.println("执行：destroy-method");
		hashMap.clear();
	}

	public String queryUserName(String uId) {
		return hashMap.get(uId);
	}
}
