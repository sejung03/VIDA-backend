package com.camorix.camorixspring.demo;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Profile("demo")
public class DemoController {

    @GetMapping("/")
    public String demoHome() {
        return "redirect:/demo.html";
    }
}
