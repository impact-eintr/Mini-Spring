package org.eintr.springframework.test.web;

import org.eintr.springframework.annotation.mvc.RequestMapping;
import org.eintr.springframework.annotation.mvc.RequestMethod;
import org.eintr.springframework.annotation.stereotype.Component;
import org.eintr.springframework.annotation.stereotype.Controller;

@Controller
@RequestMapping(value = "api")
public class WebService {
    @RequestMapping(value = "User", method = RequestMethod.GET)
    public String getMapping() {
        return "data";
    }

}
