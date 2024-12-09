package com.lion.demo.webSocket;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/webSocket")
public class WebSocketController {

    @GetMapping("/echo")
    public String echo() {
        return "webSocket/echo";
    }
    @GetMapping("/echo2")
    public String echo2() {
        return "webSocket/echo2";
    }
    @GetMapping("/personal")
    public String personal() {
        return "webSocket/personal";
    }
}
