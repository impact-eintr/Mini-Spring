package org.eintr.springframework.web.servlet.mvc;

import org.eintr.springframework.http.HttpMethod;
import org.eintr.springframework.util.StringUtils;
import org.eintr.springframework.web.servlet.ModelAndView;
import org.eintr.springframework.web.servlet.support.WebContentGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AbstractController extends WebContentGenerator implements Controller {
    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        // 请求方式验证
        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            response.setHeader("Allow", getAllowHeader());
            return null;
        }

        // Delegate to WebContentGenerator for checking and preparing.
        // 检查请求
        checkRequest(request);
        // 准备返回结果
        prepareResponse(response);

        // Execute handleRequestInternal in synchronized block if required.
        // FIXME 是否需要同步执行
        //if (this.synchronizeOnSession) {
        //    HttpSession session = request.getSession(false);
        //    if (session != null) {
        //        Object mutex = WebUtils.getSessionMutex(session);
        //        synchronized (mutex) {
        //            // 处理请求
        //            return handleRequestInternal(request, response);
        //        }
        //    }
        //}

        // 处理请求
        return handleRequestInternal(request, response);
    }


    protected final void checkRequest(HttpServletRequest request) throws ServletException {
        // Check whether we should support the request method.
        String method = request.getMethod();
        if (this.supportedMethods != null && !this.supportedMethods.contains(method)) {
            String[] supportedMethods = StringUtils.toStringArray(this.supportedMethods);
            StringBuilder sb = new StringBuilder();
            for (String s : supportedMethods) {
                sb.append(s+" ");
            }
            throw new ServletException(method+"no in "+sb.toString());
        }
    }


    protected final void prepareResponse(HttpServletResponse response) {
    }

    protected abstract ModelAndView handleRequestInternal(HttpServletRequest request,
                                                          HttpServletResponse response)
            throws Exception;
}
