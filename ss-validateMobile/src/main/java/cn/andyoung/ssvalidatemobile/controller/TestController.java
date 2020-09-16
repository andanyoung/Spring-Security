package cn.andyoung.ssvalidatemobile.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
  @RequestMapping("/hello")
  public String hello() {
    return "hello spring security";
  }

  @GetMapping("index")
  public Object index() {
    return SecurityContextHolder.getContext().getAuthentication();
  }
}
