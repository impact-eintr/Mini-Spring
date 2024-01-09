package org.eintr.springframework.test.web;

import cn.hutool.http.HttpStatus;
import org.eintr.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class HttpRequestHandlerImpl implements HttpRequestHandler {
    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setStatus(HttpStatus.HTTP_OK);
        ServletOutputStream out = response.getOutputStream();
        OutputStreamWriter ow = new OutputStreamWriter(out,"UTF8");
        ow.write("这是测试 HttpRequestHandlerImpl");
        ow.flush();
        ow.close();
    }
}
