package core.db;

import core.db.table.Schema;
import core.db.table.Table;
import exceptions.DatabaseError;

import java.util.HashMap;
import java.util.Map;

public class TableManager {

    private final Map<String, Table> tables = new HashMap<>();

    public Table getTable(String name) throws DatabaseError {
        // TODO: no such table - raise DatabaseError

        return tables.get(name);
    }

    public void createTable(String tableName, Schema schema) throws DatabaseError {

    }
}
