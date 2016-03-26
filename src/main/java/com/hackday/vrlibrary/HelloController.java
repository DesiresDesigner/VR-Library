package com.hackday.vrlibrary;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by DesiresDesigner on 3/25/16.
 */

@RestController
//@Controller
public class HelloController {

    @RequestMapping("/")
    public String index() {
        return "index";
    }
}
