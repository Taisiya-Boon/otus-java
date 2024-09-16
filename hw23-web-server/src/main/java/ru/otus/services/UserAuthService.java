package ru.otus.services;

import javafx.util.Pair;
import ru.otus.core.model.User;

public interface UserAuthService {
    Pair<Boolean, User> authenticate(String login, String password);
}
