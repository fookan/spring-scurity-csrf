package com.example.demo;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

@EnableWebSecurity
@Configuration
public class SampleConfig extends WebSecurityConfigurerAdapter {

  @Override
  public void configure(WebSecurity web) throws Exception {
    // 静的リソースへのアクセス制御
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // フォームログイン
    http.formLogin();

    // ページのアクセス権設定
    http.authorizeRequests()
        .antMatchers("/cookie").permitAll()
        .antMatchers("/sample").permitAll()
        .anyRequest().authenticated();

    // CSRF設定
    SampleTokenRepository sampleTokenRepository = new SampleTokenRepository();
    http.csrf().csrfTokenRepository(sampleTokenRepository);
    // http.csrf().disable();

  }

  // CSRF対策のfilterを定義
  // @Bean
  public SampleFilter sampleFilter() {
    return new SampleFilter();
  }

  // application.yamlではなくメモリを利用する方法
  // @Override
  // protected void configure(AuthenticationManagerBuilder auth) throws Exception
  // {
  // PasswordEncoder encoder =
  // PasswordEncoderFactories.createDelegatingPasswordEncoder();
  // auth.inMemoryAuthentication().withUser("user").password(encoder.encode("password")).roles("USER").and()
  // .withUser("admin").password(encoder.encode("adminpassword")).roles("ADMIN");
  // }
}
