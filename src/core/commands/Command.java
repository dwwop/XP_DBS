package core.commands;

import core.db.TableManager;

public abstract class Command {

    public abstract Result execute(TableManager tableManager);
}
