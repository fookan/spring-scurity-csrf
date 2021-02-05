package com.example.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashSet;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.util.StringUtils;

public class SampleTokenRepository implements CsrfTokenRepository {

  // トークンのチェックをしないアクセスの定義
  private final HashSet<String> allowedMethodList = new HashSet<String>(
      Arrays.asList("GET", "HEAD", "TRACE", "OPTIONS"));

  @Override
  public CsrfToken generateToken(HttpServletRequest request) {
    // パスを取り出す
    String path = request.getRequestURI();
    Principal userInfo = request.getUserPrincipal();
    // 認証済みの場合は、ここのnullチェックは不要
    String name = Objects.isNull(userInfo) ? null : userInfo.getName();

    String token = path + name;
    String encodedToken = Base64.getEncoder().encodeToString(token.getBytes());
    SampleCsrfToken csrfToken = new SampleCsrfToken();
    csrfToken.setToken(encodedToken);

    return csrfToken;
  }

  @Override
  public void saveToken(CsrfToken token, HttpServletRequest request, HttpServletResponse response) {
    System.out.println("SAVETOKEN");
    return;
  }

  @Override
  public CsrfToken loadToken(HttpServletRequest request) {
    // これがあるとparameterでとれなくなる。
    // try {
    //   // バッファを読み込む
    //   BufferedReader reader = request.getReader();
    //   String line = null;
    //   while (!Objects.isNull((line = reader.readLine()))) {
    //     System.out.println(line);
    //   }
    //   reader.close();
    // } catch (IOException ex) {
    //   ex.printStackTrace();
    // }

    System.out.println("METHOD: " + request.getMethod());
    System.out.println("REQUESTURL: " + request.getRequestURI());
    System.out.println("SERVLETPATH: " + request.getServletPath());
    System.out.println("PATHINFO: " + request.getPathInfo());

    SampleCsrfToken csrfToken = (SampleCsrfToken)generateToken(request);
    // POST処理時は必ずチェックする
    if (needCsrfTokenCheck(request)) {
      String token = request.getParameter(csrfToken.getParameterName());
      if (!isSameToken(token, csrfToken.getToken())) {
        // tokenいれかえ
        token += "BAD";
        csrfToken.setToken(token);
      }
    }

    return csrfToken;
  }

  // トークンのチェックが必要なリクエストか確認
  private boolean needCsrfTokenCheck(HttpServletRequest request) {
    if (allowedMethodList.contains(request.getMethod())) {
      return false;
    }

    return true;
  }

  // tokenのチェック
  private boolean isSameToken(String token1, String token2) throws SampleException {
    String decodedToken1 = new String(Base64.getDecoder().decode(token1));
    String decodedToken2 = new String(Base64.getDecoder().decode(token2));
    if (!decodedToken1.equals(decodedToken2)) {
      return false;
    }

    return true;
  }
}
