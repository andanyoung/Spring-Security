package cn.andyoung.springsecurity.ssauthentication.security;

import cn.andyoung.springsecurity.ssauthentication.security.handler.MyAuthenticationFailureHandler;
import cn.andyoung.springsecurity.ssauthentication.security.handler.MyAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired private MyAuthenticationSuccessHandler authenticationSuccessHandler;

  @Autowired private MyAuthenticationFailureHandler authenticationFailureHandler;

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http.formLogin() // 表单登录
        // http.httpBasic() // HTTP Basic
        .loginPage("/authentication/require")
        .loginProcessingUrl("/login")
        .successHandler(authenticationSuccessHandler)
        .failureHandler(authenticationFailureHandler)
        .and()
        .authorizeRequests() // 授权配置
        .antMatchers("/authentication/require", "/login.html") // 登录跳转 URL 无需认证
        .permitAll()
        .anyRequest() // 所有请求
        .authenticated() // 都需要认证
        .and()
        .csrf()
        .disable();
  }

  // 密码的加密方式
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
