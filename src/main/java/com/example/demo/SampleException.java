package com.example.demo;

import org.springframework.security.access.AccessDeniedException;

@SuppressWarnings("serial")
public class SampleException extends AccessDeniedException {
  public SampleException(String message) {
    super(message);
  }
}
