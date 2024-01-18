package org.eintr.springframework.test.web;

import org.eintr.springframework.annotation.mvc.GetMapping;
import org.eintr.springframework.annotation.mvc.PostMapping;
import org.eintr.springframework.annotation.mvc.RequestMapping;
import org.eintr.springframework.annotation.mvc.RequestMethod;
import org.eintr.springframework.annotation.stereotype.Controller;

@Controller
public class WebService {

    @PostMapping("/api/User")
    public Object User(String data) {
        return data;
    }

}
