package org.eintr.springframework.test.bean;

import org.eintr.springframework.stereotype.Component;

public interface IUserService {
    String queryUserInfo(String name);

    String register(String userName);
}
