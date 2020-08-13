package ru.otus.core.service;

import ru.otus.core.model.Account;

import java.util.Optional;

public interface DbServiceAccount {

    long saveAccount(Account account);

    void updateAccount(Account account);

    Optional<Account> getAccount(long no);

}
