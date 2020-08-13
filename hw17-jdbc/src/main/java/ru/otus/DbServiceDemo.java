package ru.otus;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.dao.AccountDao;
import ru.otus.core.dao.UserDao;
import ru.otus.core.model.Account;
import ru.otus.core.service.DbServiceAccount;
import ru.otus.core.service.DbServiceAccountImpl;
import ru.otus.jdbc.JdbcMapper;
import ru.otus.jdbc.SQLQuery.Query;
import ru.otus.jdbc.SQLQuery.SQLQueryFactory;
import ru.otus.jdbc.SQLQuery.SQlQuery;
import ru.otus.jdbc.dao.AccountDaoJdbc;
import ru.otus.jdbc.dao.UserDaoJdbc;
import ru.otus.core.service.DBServiceUser;
import ru.otus.core.service.DbServiceUserImpl;
import ru.otus.jdbc.DbExecutor;
import ru.otus.h2.DataSourceH2;
import ru.otus.core.model.User;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;

/**
 * @author sergey
 * created on 03.02.19.
 */
public class DbServiceDemo {
  private static Logger logger = LoggerFactory.getLogger(DbServiceDemo.class);

  public static void main(String[] args) throws Exception {
    userDemo();
//    accountDemo();
  }

  private static void userDemo() throws Exception {
    DataSource dataSource = new DataSourceH2();
    DbServiceDemo demo = new DbServiceDemo();

    User userT = new User(1, "name", 15);

    demo.createTable(dataSource, userT);

    SessionManagerJdbc sessionManager = new SessionManagerJdbc(dataSource);
    DbExecutor dbExecutor = new DbExecutor<>();
    JdbcMapper jdbcMapper = new JdbcMapper(sessionManager, dbExecutor);

    UserDao userDao = new UserDaoJdbc(sessionManager, jdbcMapper);

    DBServiceUser dbServiceUser = new DbServiceUserImpl(userDao);
    long id = dbServiceUser.saveUser(new User(1, "dbServiceUser", 20));
    Optional<User> user = dbServiceUser.getUser(id);

    System.out.println(user);
    user.ifPresentOrElse(
            crUser -> logger.info("created user, name:{}, age:{}", crUser.getName(), crUser.getAge()),
            () -> logger.info("user was not created")
    );

    dbServiceUser.updateUser(new User(1, "Viktor", 23));
  }

  private static void accountDemo() throws Exception {
    DataSource dataSource = new DataSourceH2();
    DbServiceDemo demo = new DbServiceDemo();

    Account accountT = new Account(2, "type", BigDecimal.valueOf(15.01));

    demo.createTable(dataSource, accountT);

    SessionManagerJdbc sessionManager = new SessionManagerJdbc(dataSource);
    DbExecutor dbExecutor = new DbExecutor<>();
    JdbcMapper jdbcMapper = new JdbcMapper(sessionManager, dbExecutor);

    AccountDao accountDao = new AccountDaoJdbc(sessionManager, jdbcMapper);

    DbServiceAccount dbServiceAccount = new DbServiceAccountImpl(accountDao);
    long no = dbServiceAccount.saveAccount(new Account(1, "dbServiceAccount", BigDecimal.valueOf(20.45)));
    Optional<Account> account = dbServiceAccount.getAccount(no);

    System.out.println(account);
    account.ifPresentOrElse(
            crAccount -> logger.info("created account, type:{}, rest:{}", crAccount.getType(), crAccount.getRest()),
            () -> logger.info("account was not created")
    );

    dbServiceAccount.updateAccount(new Account(1, "SuperAccount", BigDecimal.valueOf(1.11)));
  }

  private void createTable(DataSource dataSource, Object object) throws SQLException {
    SQlQuery sQlQuery = SQLQueryFactory.getSQLQuery(Query.CREATE_TABLE);
    try (Connection connection = dataSource.getConnection();
         PreparedStatement pst = connection.prepareStatement(sQlQuery.toSQLString(object.getClass()))) {
      pst.executeUpdate();
    }
    System.out.println("table created");
  }
}
