package core.commands;

import core.db.TableManager;
import core.db.table.Schema;
import core.db.table.Table;

public class InsertCommand extends Command {

    @Override
    public Result execute(TableManager tableManager) {
        return new Result(false, "", new Table(new Schema()));
    }
}
