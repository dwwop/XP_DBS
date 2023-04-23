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
        if (tables.containsKey(name)){
            return tables.get(name);
        }
        throw new DatabaseError("Table with name \"" + name + "\" doesn't exist");
    }

    public void createTable(String tableName, Schema schema) throws DatabaseError {
        if (tables.containsKey(tableName)){
            throw new DatabaseError("Table with name \"" + tableName + "\" already exist");
        }
        tables.put(tableName, new Table(schema));
    }
}
