package edu.kis.powp.jobs2d.command.gui;

import edu.kis.powp.jobs2d.command.DriverCommand;
import edu.kis.powp.jobs2d.command.OperateToCommand;
import edu.kis.powp.jobs2d.command.SetPositionCommand;

public abstract class GuiCommandRepresentation {
}

class GuiAtomicCommandRepresentation extends GuiCommandRepresentation {
    String type;
    int x, y;

    GuiAtomicCommandRepresentation(SetPositionCommand cmd) {
        type = "SetPosition";
        x = cmd.getPosX();
        y = cmd.getPosY();
    }

    GuiAtomicCommandRepresentation(OperateToCommand cmd) {
        type = "OperateTo";
        x = cmd.getPosX();
        y = cmd.getPosY();
    }

    DriverCommand createCommand() {
        if ("SetPosition".equals(type)) return new SetPositionCommand(x, y);
        return new OperateToCommand(x, y);
    }

    @Override
    public String toString() {
        return type + " [" + x + ", " + y + "]";
    }
}

class GuiCompositeCommandRepresentation extends GuiCommandRepresentation {
    String name;

    GuiCompositeCommandRepresentation(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return name;
    }
}
