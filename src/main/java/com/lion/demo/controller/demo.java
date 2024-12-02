package com.lion.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/demo")
public class demo {
    @GetMapping("/hello")
    @ResponseBody
    public String hello(){
        return "<h1>hello new world</h1>";
    }

    @GetMapping("/bye")
    @ResponseBody
    public String bye() {
        return "<h1>GoodBye Cruel World!</h1>";
    }
}
