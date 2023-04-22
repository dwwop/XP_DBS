package core.parsing.tree.clauses;


import core.parsing.util.KeywordConsumer;

import java.util.Map;
import java.util.Objects;

public class OrderByClause extends Clause {

    Map<String, KeywordConsumer.Keyword> columnsAndOrders;

    public OrderByClause(Map<String, KeywordConsumer.Keyword> columnsAndOrders) {
        this.columnsAndOrders = columnsAndOrders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderByClause that = (OrderByClause) o;
        return Objects.equals(columnsAndOrders, that.columnsAndOrders);
    }

    @Override
    public int hashCode() {
        return Objects.hash(columnsAndOrders);
    }

    @Override
    public String toString() {
        return
                "columnsAndOrders: " + columnsAndOrders;
    }
}
