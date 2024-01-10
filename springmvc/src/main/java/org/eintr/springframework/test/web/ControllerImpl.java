package org.eintr.springframework.test.web;

import cn.hutool.http.HttpStatus;
import org.eintr.springframework.annotation.stereotype.Component;
import org.eintr.springframework.web.servlet.ModelAndView;
import org.eintr.springframework.web.servlet.mvc.Controller;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStreamWriter;

public class ControllerImpl implements Controller {
    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setStatus(HttpStatus.HTTP_OK);
        ServletOutputStream out = response.getOutputStream();
        OutputStreamWriter ow = new OutputStreamWriter(out,"UTF8");
        ow.write("这是测试 ControllerImpl");
        ow.flush();
        ow.close();
        return null;
    }
}
