package org.eintr.springframework.web.servlet;

import org.eintr.springframework.beans.BeanWrapper;
import org.eintr.springframework.beans.BeansException;
import org.eintr.springframework.context.ApplicationContext;
import org.eintr.springframework.context.ApplicationContextAware;

import javax.servlet.ServletException;

public class FrameworkServlet extends HttpServletBean implements ApplicationContextAware {
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

    }


    protected void initBeanWrapper(BeanWrapper bw) throws BeansException {
    }

    protected void initServletBean() throws ServletException {
    }
}
