package org.eintr.springframework.test.bean;

public interface IUserService {
    String queryUserInfo(String name);

    String register(String userName);
}
