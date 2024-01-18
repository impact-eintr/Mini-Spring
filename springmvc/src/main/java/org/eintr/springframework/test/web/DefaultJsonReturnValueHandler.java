package org.eintr.springframework.test.web;

import cn.hutool.http.HttpStatus;
import org.eintr.springframework.core.MethodParameter;
import org.eintr.springframework.web.context.request.ServletWebRequest;
import org.eintr.springframework.web.method.support.HandlerMethodReturnValueHandler;

import javax.servlet.ServletOutputStream;
import com.alibaba.fastjson.*;
import java.io.OutputStreamWriter;

public class DefaultJsonReturnValueHandler implements HandlerMethodReturnValueHandler {
    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return String.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ServletWebRequest webRequest) throws Exception {
        webRequest.getResponse().setStatus(HttpStatus.HTTP_OK);
        ServletOutputStream out = webRequest.getResponse().getOutputStream();
        OutputStreamWriter ow = new OutputStreamWriter(out,"UTF8");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("demo", returnValue);
        ow.write("这是测试 DefaultJsonReturnValueHandler"+jsonObject.toJSONString());
        ow.flush();
        ow.close();
    }
}
