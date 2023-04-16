package core.commands;

import core.db.table.Table;

public record Result(boolean success, String message, Table output) {
}
