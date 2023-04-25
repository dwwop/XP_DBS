package core.parsing.tree.clauses;


import core.parsing.util.KeywordConsumer;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class OrderByClause extends Clause {

    List<String> columns;
    List<KeywordConsumer.Keyword> orders;

    public OrderByClause(List<String> columns, List<KeywordConsumer.Keyword> orders) {
        this.columns = columns;
        this.orders = orders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderByClause that = (OrderByClause) o;
        return Objects.equals(columns, that.columns) && Objects.equals(orders, that.orders);
    }

    @Override
    public int hashCode() {
        return Objects.hash(columns, orders);
    }

    @Override
    public String toString() {
        return
                "columns: " + columns + " orders: " + orders;
    }

    public List<String> getColumns(){
        return columns;
    }

    public List<KeywordConsumer.Keyword> getOrders(){
        return orders;
    }
}
