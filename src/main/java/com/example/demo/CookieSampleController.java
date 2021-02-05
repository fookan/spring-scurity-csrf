package com.example.demo;

import java.security.Principal;
import java.util.Objects;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cookie")
public class CookieSampleController {
  @GetMapping
  public String init (
      HttpServletRequest httpServletRequest,
      HttpServletResponse response,
      Model model
  ) {
    boolean hasCookie = false;
    Cookie[] cookieList = httpServletRequest.getCookies();
    if (Objects.isNull(cookieList)) {
      System.out.println("**** NO COOKIE");
    } else {
      for (int i = 0; i < cookieList.length; i += 1) {
        String name = cookieList[i].getName();
        System.out.println("[" + name + "] " + cookieList[i].getValue());

        if (name.equals("mk-test")) {
          hasCookie = true;
        }
      }
    }

    if (!hasCookie) {
      Cookie cookie = new Cookie("mktest", "thistest");
      cookie.setMaxAge(60 * 60);
      cookie.setPath("/");
      response.addCookie(cookie);
    }

    Principal userInfo = httpServletRequest.getUserPrincipal();
    if (!Objects.isNull(userInfo)) {
      System.out.println(">> USERNAME: " + userInfo.getName());
    } else {
      System.out.println(">> NO USER INFO");
    }

    return "cookie";
  }

}
