package ru.otus.jdbc.SQLQuery;

import ru.otus.jdbc.SQLQuery.query.impl.SQLQueryCreateTable;
import ru.otus.jdbc.SQLQuery.query.impl.SQLQueryInsert;
import ru.otus.jdbc.SQLQuery.query.impl.SQLQuerySelect;
import ru.otus.jdbc.SQLQuery.query.impl.SQLQueryUpdate;

public class SQLQueryFactory {

    public static SQlQuery getSQLQuery(Query query) {
        switch (query) {
            case INSERT:
                return new SQLQueryInsert();
            case SELECT:
                return new SQLQuerySelect();
            case CREATE_TABLE:
                return new SQLQueryCreateTable();
            case UPDATE:
                return new SQLQueryUpdate();
            default:
                throw new IllegalArgumentException();
        }
    }

}
