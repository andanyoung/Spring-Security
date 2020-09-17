package cn.andyoung.ssrememberme.security;

import cn.andyoung.ssrememberme.security.handler.MyAuthenticationFailureHandler;
import cn.andyoung.ssrememberme.security.handler.MyAuthenticationSuccessHandler;
import cn.andyoung.ssrememberme.security.validate.imagecode.ValidateCodeFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired private MyAuthenticationSuccessHandler authenticationSuccessHandler;

  @Autowired private MyAuthenticationFailureHandler authenticationFailureHandler;

  @Autowired private ValidateCodeFilter validateCodeFilter;

  @Autowired private UserDetailService userDetailService;
  @Autowired private DataSource dataSource;

  @Bean
  public PersistentTokenRepository persistentTokenRepository() {
    JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
    jdbcTokenRepository.setDataSource(dataSource);
    jdbcTokenRepository.setCreateTableOnStartup(false);
    return jdbcTokenRepository;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http.addFilterBefore(
            validateCodeFilter, UsernamePasswordAuthenticationFilter.class) // 添加验证码校验过滤器
        .formLogin() // 表单登录
        // http.httpBasic() // HTTP Basic
        .loginPage("/authentication/require")
        .loginProcessingUrl("/login")
        .successHandler(authenticationSuccessHandler)
        .failureHandler(authenticationFailureHandler)
        .and()
        .rememberMe()
        .tokenRepository(persistentTokenRepository()) // 配置 token 持久化仓库
        .tokenValiditySeconds(3600) // remember 过期时间，单为秒
        .userDetailsService(userDetailService) // 处理自动登录逻辑
        .and()
        .authorizeRequests() // 授权配置
        .antMatchers(
            "/authentication/require", "/login.html", "/code/image", "/code/sms") // 登录跳转 URL 无需认证
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
