package ru.otus.jdbc.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.dao.AccountDao;
import ru.otus.core.dao.UserDaoException;
import ru.otus.core.model.Account;
import ru.otus.core.sessionmanager.SessionManager;
import ru.otus.jdbc.JdbcMapper;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;

import java.util.Optional;

public class AccountDaoJdbc implements AccountDao {
    private static Logger logger = LoggerFactory.getLogger(AccountDaoJdbc.class);

    private final SessionManagerJdbc sessionManager;
    private final JdbcMapper jdbcMapper;

    public AccountDaoJdbc(SessionManagerJdbc sessionManager, JdbcMapper jdbcMapper) {
        this.sessionManager = sessionManager;
        this.jdbcMapper = jdbcMapper;
    }


    @Override
    public Optional<Account> findByNo(long no) {
        try {
            return jdbcMapper.load(no, Account.class);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public long saveAccount(Account account) {
        try {
            return jdbcMapper.create(account);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new UserDaoException(e);
        }
    }

    @Override
    public void updateAccount(Account account) {
        jdbcMapper.update(account);
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }
}
