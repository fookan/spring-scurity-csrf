package com.example.demo;

import org.springframework.security.web.csrf.CsrfToken;

public class SampleCsrfToken implements CsrfToken{

  private static final long serialVersionUID = 9064716119460957591L;

  private String token;

  public SampleCsrfToken() {}

  public void setToken(String token) {
    this.token = token;
  }

  @Override
  public String getHeaderName() {
    return "mycsrfheader";
  }

  @Override
  public String getParameterName() {
    return "mycsrf";
  }

  @Override
  public String getToken() {
    return token;
  }

}
