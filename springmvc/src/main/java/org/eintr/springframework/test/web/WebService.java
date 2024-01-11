package org.eintr.springframework.test.web;

import org.eintr.springframework.annotation.mvc.GetMapping;
import org.eintr.springframework.annotation.mvc.RequestMapping;
import org.eintr.springframework.annotation.mvc.RequestMethod;
import org.eintr.springframework.annotation.stereotype.Controller;

@Controller
public class WebService {

    @GetMapping("/api/User")
    public Object User() {
        System.out.println();
        return "data";
    }

}
