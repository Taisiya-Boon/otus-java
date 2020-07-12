package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

import ru.otus.core.dao.UserDao;
import ru.otus.jdbc.JdbcMapper;
import ru.otus.jdbc.SQLQuery.Query;
import ru.otus.jdbc.SQLQuery.SQLQueryFactory;
import ru.otus.jdbc.SQLQuery.SQlQuery;
import ru.otus.jdbc.dao.UserDaoJdbc;
import ru.otus.core.service.DBServiceUser;
import ru.otus.core.service.DbServiceUserImpl;
import ru.otus.jdbc.DbExecutor;
import ru.otus.h2.DataSourceH2;
import ru.otus.core.model.User;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;

public class DbServiceDemo {
    private static Logger logger = LoggerFactory.getLogger(DbServiceDemo.class);

    public static void main(String[] args) throws Exception {
        createTableUser();

        long startTimeWithoutCache = System.currentTimeMillis();
        withoutCache();
        long finishTimeWithoutCache = System.currentTimeMillis();
        logger.info("without cache, start:{}, finish:{}, time:{}", startTimeWithoutCache, finishTimeWithoutCache, finishTimeWithoutCache - startTimeWithoutCache);

        long startTimeWithCache = System.currentTimeMillis();
        withCache();
        long finishTimeWithCache = System.currentTimeMillis();
        logger.info("with cache, start:{}, finish:{}, time:{}", startTimeWithCache, finishTimeWithCache, finishTimeWithCache - startTimeWithCache);


        resettingTheCache();
    }

    private static void createTableUser() throws SQLException {
        DataSource dataSource = new DataSourceH2();
        DbServiceDemo demo = new DbServiceDemo();

        User userT = new User(1, "name", 15);

        demo.createTable(dataSource, userT);
    }

    private static void withoutCache() throws Exception {
        DBServiceUser dbServiceUser = setDbService();

        long[] idArray = new long[10];
        for (int i = 0; i < 10; i++) {
            idArray[i] = dbServiceUser.saveUser(new User(0, "user" + i, 20 + i));
        }

        ArrayList<Optional<User>> users = new ArrayList();
        for (int i = 0; i < 10; i++) {
            users.add(i, dbServiceUser.getUser(idArray[i]));
        }

    }

    private static void withCache() throws Exception {
        DBServiceUser dbServiceUser = setDbService();

        MyCache<String, User> myCache = new MyCache<>();

        long[] idArray = new long[10];
        for (int i = 0; i < 10; i++) {
            idArray[i] = dbServiceUser.saveUser(new User(0, "user" + i, 20 + i));
            myCache.put("" + idArray[i], new User(0, "user" + i, 20 + i));
        }

        ArrayList<User> users = new ArrayList();
        for (int i = 0; i < 10; i++) {
            if (myCache.get(""+idArray[i]) != null) {
                users.add(i, myCache.get(""+idArray[i]));
            } else {
                users.add(i, dbServiceUser.getUser(idArray[i]).get());
            }
        }

    }

    private static DBServiceUser setDbService() {
        DataSource dataSource = new DataSourceH2();

        SessionManagerJdbc sessionManager = new SessionManagerJdbc(dataSource);
        DbExecutor dbExecutor = new DbExecutor<>();
        JdbcMapper jdbcMapper = new JdbcMapper(sessionManager, dbExecutor);

        UserDao userDao = new UserDaoJdbc(sessionManager, jdbcMapper);

        return new DbServiceUserImpl(userDao);
    }

    private void createTable(DataSource dataSource, Object object) throws SQLException {
        SQlQuery sQlQuery = SQLQueryFactory.getSQLQuery(Query.CREATE_TABLE);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pst = connection.prepareStatement(sQlQuery.toSQLString(object.getClass()))) {
            pst.executeUpdate();
        }
        System.out.println("table created");
    }

    private static void resettingTheCache() {
        DBServiceUser dbServiceUser = setDbService();

        MyCache<String, User> myCache = new MyCache<>();

        ArrayList<Long> idArray = new ArrayList<>();
        int i = 0;
        while (true) {
            idArray.add(i, dbServiceUser.saveUser(new User(0, "user" + i, 20 + i)));
            myCache.put("" + idArray.get(i), new User(0, "user" + i, 20 + i));
            logger.info("in cache: {}", myCache.getIn());
            i++;
        }
    }
}
