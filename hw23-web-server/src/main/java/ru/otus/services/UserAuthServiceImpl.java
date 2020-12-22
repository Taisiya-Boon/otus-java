package ru.otus.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.service.DbServiceUserWeb;

public class UserAuthServiceImpl implements UserAuthService {
    private static Logger logger = LoggerFactory.getLogger(UserAuthServiceImpl.class);

    private DbServiceUserWeb dbService;

    public UserAuthServiceImpl(DbServiceUserWeb dbService) {
        this.dbService = dbService;
    }

    @Override
    public boolean authenticate(String login, String password) {
        if (dbService.getUserByLogin(login).isPresent()) {
            logger.info("user with this login \'{}\' is trying to authenticate.", login);
            return dbService.getUserByLogin(login).get().getPassword().equals(password);
        } else {
            logger.error("user with this login \'{}\' does not exist.", login);
            throw new IllegalArgumentException("user with this login does not exist.");
        }
    }
}
