package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sample")
public class SampleController {

  @GetMapping
  public String init() {
    return "sample";
  }

  @PostMapping
  public String post(
    @ModelAttribute SampleData requestBody,
    Model model
  ) {
    String code = requestBody.getCode();
    if (!code.isEmpty()) {
      model.addAttribute("data", requestBody.getCode());
    }

    return "sample";
  }
}
