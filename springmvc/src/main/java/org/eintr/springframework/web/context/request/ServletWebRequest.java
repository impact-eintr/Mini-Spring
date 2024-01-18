package org.eintr.springframework.web.context.request;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletWebRequest extends ServletRequestAttributes {
    public ServletWebRequest(HttpServletRequest request) {
        super(request);
    }

    public ServletWebRequest(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }

}
