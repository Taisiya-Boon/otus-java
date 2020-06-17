package ru.otus.core.dao;

import java.util.Optional;

import ru.otus.core.model.User;
import ru.otus.core.sessionmanager.SessionManager;

public interface UserDao {
  Optional<User> findById(long id);

  long saveUser(User user);

  void updateUser(User user);

  SessionManager getSessionManager();
}
