package cn.andyoung.jwt.service;

import cn.andyoung.jwt.dao.Database;
import cn.andyoung.jwt.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  @Autowired private Database database;

  public UserEntity getUserByUsername(String username) {
    return database.getDatabase().get(username);
  }
}
