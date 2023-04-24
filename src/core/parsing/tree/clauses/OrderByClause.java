package core.parsing.tree.clauses;


import core.parsing.util.KeywordConsumer;

import java.util.Map;

public class OrderByClause extends Clause {

    Map<String, KeywordConsumer.Keyword> columnsAndOrders;

    public OrderByClause(Map<String, KeywordConsumer.Keyword> columnsAndOrders) {
        this.columnsAndOrders = columnsAndOrders;
    }

    public Map<String, KeywordConsumer.Keyword> getColumnsAndOrders() {
        return columnsAndOrders;
    }
}
