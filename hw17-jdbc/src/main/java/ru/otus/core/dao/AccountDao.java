package ru.otus.core.dao;

import ru.otus.core.model.Account;
import ru.otus.core.sessionmanager.SessionManager;

import java.util.Optional;

public interface AccountDao {
    Optional<Account> findByNo(long no);

    long saveAccount(Account account);

    void updateAccount(Account account);

    SessionManager getSessionManager();
}
