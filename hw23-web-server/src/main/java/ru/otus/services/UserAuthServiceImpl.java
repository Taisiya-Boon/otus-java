package ru.otus.services;

import javafx.util.Pair;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.model.User;
import ru.otus.core.service.DbServiceUserWeb;

import java.util.Optional;

@RequiredArgsConstructor
public class UserAuthServiceImpl implements UserAuthService {
    private static Logger logger = LoggerFactory.getLogger(UserAuthServiceImpl.class);

    private final DbServiceUserWeb dbService;

    @Override
    public Pair<Boolean, User> authenticate(String login, String password) {
        Optional<User> user = dbService.getUserByLogin(login);
        if (user.isPresent()) {
            if (user.get().getPassword().equals(password)) {
                logger.info("Успешно зашёл пользователь с логином {}", login);
                return new Pair<>(true, user.get());
            }
            logger.info("Неверный пароль для пользователя {}", login);
            return new Pair<>(false, null);
        }
        logger.info("Пользователь с логином {} не существует", login);
        return new Pair<>(false, null);
    }
}
