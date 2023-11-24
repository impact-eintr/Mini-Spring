package org.eintr.springframework.test.bean;

import java.util.HashMap;
import java.util.Map;

public class UserDao {
	private static Map<String, String> hashMap = new HashMap<>();
	static {
		hashMap.put("1000", "AAA");
		hashMap.put("1001", "BBB");
		hashMap.put("1002", "CCC");
	}

	public String queryUserName(String uId) {
		return hashMap.get(uId);
	}
}
