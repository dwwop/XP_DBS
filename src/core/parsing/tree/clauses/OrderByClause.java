package core.parsing.tree.clauses;


import core.parsing.util.KeywordConsumer;

import java.util.List;

public class OrderByClause extends Clause {

    List<String> columns;
    List<KeywordConsumer.Keyword> orders;

    public OrderByClause(List<String> columns, List<KeywordConsumer.Keyword> orders) {
        this.columns = columns;
        this.orders = orders;
    }

    public List<String> getColumns() {
        return columns;
    }

    public List<KeywordConsumer.Keyword> getOrders() {
        return orders;
    }
}
