package core.db.table;

import java.util.HashMap;
import java.util.Map;

public class Schema {

    private Map<String, ColumnDefinition> columns = new HashMap<>();
    private String primaryKeyColumn;

}
