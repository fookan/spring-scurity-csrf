package com.example.demo;

import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashSet;
import java.util.Objects;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

// @Component
public class SampleFilter extends OncePerRequestFilter {

  // トークンのチェックをしないアクセスの定義
  private final HashSet<String> allowedMethodList = new HashSet<String>(
      Arrays.asList("GET", "HEAD", "TRACE", "OPTIONS"));

  // csrfトークンエラー時のハンドラー
  private AccessDeniedHandler accessDeniedHandler = new AccessDeniedHandlerImpl();

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    try {
      // tokenを生成
      CsrfToken csrfToken = genereteToken(request);

      // tokenチェックを行う
      if (needCsrfTokenCheck(request)) {
        // tokenを取り出す
        String token = request.getHeader(csrfToken.getHeaderName());
        if (Objects.isNull(token)) {
          token = request.getParameter(csrfToken.getParameterName());
        }
        checkToken(token, csrfToken.getToken());
      }

      // tokenをセット
      request.setAttribute(CsrfToken.class.getName(), csrfToken);
      request.setAttribute(csrfToken.getParameterName(), csrfToken);
    } catch (SampleException ex) {
      accessDeniedHandler.handle(request, response, ex);
    }

    filterChain.doFilter(request, response);
  }

  // トークンのチェックが必要なリクエストか確認
  private boolean needCsrfTokenCheck(HttpServletRequest request) {
    if (allowedMethodList.contains(request.getMethod())) {
      return false;
    }

    return true;
  }

  // user情報の取り出し
  private String getUserName(HttpServletRequest request) throws SampleException {
    // ユーザー情報の取り出し
    Principal userInfo = request.getUserPrincipal();
    String name = Objects.isNull(userInfo) ? null : userInfo.getName();
    if (!StringUtils.hasLength(name)) {
      // throw new SampleException("user name is empty");
      name = "first access";
    }

    return name;
  }

  // token生成
  private CsrfToken genereteToken(HttpServletRequest request) throws SampleException {
    // パスの取り出し
    // これはマッピングしていないのかとれない
    // String path = request.getPathInfo();
    String path = request.getRequestURI();
    // usernameの取り出し
    String name = getUserName(request);
    // tokenの生成
    String seed = path + name;
    String token = Base64.getEncoder().encodeToString(seed.getBytes());
    // csrfトークンを作成
    SampleCsrfToken csrfToken = new SampleCsrfToken();
    csrfToken.setToken(token);

    return csrfToken;
  }

  // tokenのチェック
  private void checkToken(String token1, String token2) throws SampleException {
    String decodedToken1 = new String(Base64.getDecoder().decode(token1));
    String decodedToken2 = new String(Base64.getDecoder().decode(token2));
    if (!decodedToken1.equals(decodedToken2)) {
      throw new SampleException("token is diffrent");
    }

    return;
  }
}
