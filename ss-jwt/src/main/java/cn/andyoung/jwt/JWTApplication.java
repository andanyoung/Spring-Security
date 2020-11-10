package cn.andyoung.jwt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class JWTApplication {
  public static void main(String[] args) {
    SpringApplication.run(JWTApplication.class, args);
  }
}
