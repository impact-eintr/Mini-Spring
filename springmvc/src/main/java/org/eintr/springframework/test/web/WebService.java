package org.eintr.springframework.test.web;

import org.eintr.springframework.annotation.mvc.*;
import org.eintr.springframework.annotation.stereotype.Controller;

@Controller
public class WebService {

    @PostMapping("/api/User")
    public Object User(@RequestParam(value="data") String data) throws Exception {
        if ("123".equals(data)) {
            return data;
        }
        throw new Exception("Exception Test");
    }

}
