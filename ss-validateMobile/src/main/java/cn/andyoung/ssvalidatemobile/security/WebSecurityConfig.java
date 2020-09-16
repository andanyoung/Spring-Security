package cn.andyoung.ssvalidatemobile.security;

import cn.andyoung.ssvalidatemobile.security.handler.MyAuthenticationFailureHandler;
import cn.andyoung.ssvalidatemobile.security.handler.MyAuthenticationSuccessHandler;
import cn.andyoung.ssvalidatemobile.security.validate.smscode.SmsAuthenticationConfig;
import cn.andyoung.ssvalidatemobile.security.validate.smscode.SmsCodeFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired private MyAuthenticationSuccessHandler authenticationSuccessHandler;

  @Autowired private MyAuthenticationFailureHandler authenticationFailureHandler;

  @Autowired private SmsCodeFilter smsCodeFilter;

  @Autowired private SmsAuthenticationConfig smsAuthenticationConfig;

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http.addFilterBefore(smsCodeFilter, UsernamePasswordAuthenticationFilter.class) // 添加短信验证码校验过滤器
        .formLogin() // 表单登录
        // http.httpBasic() // HTTP Basic
        .loginPage("/authentication/require")
        .loginProcessingUrl("/login")
        .successHandler(authenticationSuccessHandler)
        .failureHandler(authenticationFailureHandler)
        .and()
        .authorizeRequests() // 授权配置
        .antMatchers(
            "/authentication/require", "/smscode.html", "/code/image", "/code/sms") // 登录跳转 URL 无需认证
        .permitAll()
        .anyRequest() // 所有请求
        .authenticated() // 都需要认证
        .and()
        .csrf()
        .disable()
        .apply(smsAuthenticationConfig); // 将短信验证码认证配置加到 Spring Security 中;
  }

  // 密码的加密方式
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
