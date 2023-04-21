package core.db;

import core.db.table.Schema;
import core.db.table.Table;

import java.util.HashMap;
import java.util.Map;

public class TableManager {

    private final Map<String, Table> tables = new HashMap<>();

    public Table getTable(String name) {
        // TODO: no such table - return null Table object?

        return tables.get(name);
    }

    public void createTable(Schema schema) {

    }
}
