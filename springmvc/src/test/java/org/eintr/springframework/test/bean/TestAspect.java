package org.eintr.springframework.test.bean;

import org.eintr.springframework.annotation.aop.After;
import org.eintr.springframework.annotation.aop.Aspect;
import org.eintr.springframework.annotation.aop.Before;
import org.eintr.springframework.annotation.aop.PointCut;
import org.eintr.springframework.annotation.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component("TestAspect")
public class TestAspect {
    @PointCut("org.eintr.springframework.test.bean.UserService.queryUserInfo()")
    public void pointcut1() {}

    @PointCut("org.eintr.springframework.test.bean.UserService.register()")
    public void pointcut2() {}

    @Before
    public void before() {
        System.out.println("----Teach start teach time: "+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }
    @After
    public void after() {
        System.out.println("----Teach end teach time: "+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }
}
