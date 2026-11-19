package com.camorix.camorixspring.auth.controller;

import com.camorix.camorixspring.auth.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/main")
@RequiredArgsConstructor
public class MainController {
    private final SecurityContextHolderUtil securityContextHolderUtil;

    @GetMapping("/user")
    public String userAPI(){
        return "user main";
    }
    @GetMapping("/nurse")
    public String nurseAPI(){
        String email = securityContextHolderUtil.getCurrentUserEmail();
        String name = securityContextHolderUtil.getCurrentUserName();
        String role = securityContextHolderUtil.getCurrentUserRole();

        System.out.println("Controller (/nurse)");
        System.out.println("email : "+email);
        System.out.println("name : "+name);
        System.out.println("role : "+role);

        return "nurse main";
    }

    @GetMapping("/default")
    public String defaultAPI(){
        return "default main";
    }
}
