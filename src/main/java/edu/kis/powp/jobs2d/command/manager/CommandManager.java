package edu.kis.powp.jobs2d.command.manager;

import java.util.List;

import edu.kis.powp.jobs2d.command.CompoundCommand;
import edu.kis.powp.jobs2d.command.DriverCommand;
import edu.kis.powp.jobs2d.visitor.CommandCounterVisitor;
import edu.kis.powp.observer.Publisher;

/**
 * Driver command Manager.
 */
public class CommandManager {
    private DriverCommand currentCommand = null;

    private final Publisher changePublisher = new Publisher();

    /**
     * Set current command.
     * 
     * @param commandList Set the command as current.
     */
    public synchronized void setCurrentCommand(DriverCommand commandList) {
        this.currentCommand = commandList;
        changePublisher.notifyObservers();
    }

    /**
     * Set current command.
     * 
     * @param commandList list of commands representing a compound command.
     * @param name        name of the command.
     */
    public synchronized void setCurrentCommand(List<DriverCommand> commandList, String name) {
        CompoundCommand compoundCommand = CompoundCommand.fromListOfCommands(commandList, name);
        setCurrentCommand(compoundCommand);
    }

    /**
     * Return current command.
     * 
     * @return Current command.
     */
    public synchronized DriverCommand getCurrentCommand() {
        return currentCommand;
    }

    public synchronized void clearCurrentCommand() {
        currentCommand = null;
    }

    public synchronized String getCurrentCommandString() {
        if (getCurrentCommand() == null) {
            return "No command loaded";
        } else {
            CommandCounterVisitor.CommandStats stats = CommandCounterVisitor.countCommands(getCurrentCommand());
            StringBuilder sb = new StringBuilder(getCurrentCommand().toString());
            sb.append("\n\nStats:\n");
            sb.append("Total commands: ").append(stats.getCount()).append("\n");
            sb.append("OperateTo count: ").append(stats.getOperateToCount()).append("\n");
            sb.append("SetPosition count: ").append(stats.getSetPositionCount()).append("\n");
            return sb.toString();
        }
    }

    public Publisher getChangePublisher() {
        return changePublisher;
    }
}
