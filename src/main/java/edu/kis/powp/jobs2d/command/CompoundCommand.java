package edu.kis.powp.jobs2d.command;

import edu.kis.powp.jobs2d.Job2dDriver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * An immutable implementation of ICompoundCommand that allows executing multiple commands in sequence.
 * This class follows the immutable object pattern - all operations that would modify the command
 * return a new instance instead of modifying the existing one.
 */
public class CompoundCommand implements ICompoundCommand {

    private final List<DriverCommand> commands;

    /**
     * Private constructor to ensure controlled creation of CompoundCommand instances.
     * Creates an immutable copy of the provided command list.
     * 
     * @param commands the list of commands to be executed in sequence
     */
    private CompoundCommand(List<DriverCommand> commands) {
        this.commands = Collections.unmodifiableList(new ArrayList<>(commands));
    }

    /**
     * Executes all commands in sequence using the provided driver.
     * 
     * @param driver the Job2dDriver to execute commands with
     */
    @Override
    public void execute(Job2dDriver driver) {
        for (DriverCommand command : commands) {
            command.execute(driver);
        }
    }

    /**
     * Returns an iterator over the commands in this compound command.
     * 
     * @return an iterator over the commands
     */
    @Override
    public Iterator<DriverCommand> iterator() {
        return commands.iterator();
    }


    /**
     * Factory method to create a CompoundCommand from a list of commands.
     * 
     * @param commands the list of commands to be executed in sequence
     * @return a new CompoundCommand instance containing the provided commands
     */
    public static CompoundCommand fromListOfCommands(List<DriverCommand> commands) {
        return new CompoundCommand(commands);
    }

    /**
     * Creates a new CompoundCommand by concatenating the provided commands to this command's list.
     * This operation does not modify the existing CompoundCommand but returns a new instance.
     * 
     * @param commands the list of commands to concatenate to this compound command
     * @return a new CompoundCommand instance containing all commands from this instance
     *         followed by the provided commands
     */
    public CompoundCommand concatCommands(List<DriverCommand> commands) {
        List<DriverCommand> newCommands = new ArrayList<>(this.commands);
        newCommands.addAll(commands);
        return new CompoundCommand(newCommands);
    }

    /**
     * Returns the number of commands in this compound command.
     * 
     * @return the number of commands
     */
    public int size() {
        return commands.size();
    }

    /**
     * Checks if this compound command contains no commands.
     * 
     * @return true if this compound command contains no commands, false otherwise
     */
    public boolean isEmpty() {
        return commands.isEmpty();
    }

    /**
     * Builder class for flexible creation of CompoundCommand instances.
     * Provides a fluent interface for constructing compound commands.
     */
    public static class Builder {
        private final List<DriverCommand> commands = new ArrayList<>();

        /**
         * Adds multiple commands to the builder.
         * 
         * @param commands the list of commands to add
         * @return this builder instance for method chaining
         */
        public Builder add(List<DriverCommand> commands) {
            this.commands.addAll(commands);
            return this;
        }

        /**
         * Adds all commands from another compound command to the builder.
         * 
         * @param compoundCommand the compound command whose commands should be added
         * @return this builder instance for method chaining
         */
        public Builder add(CompoundCommand compoundCommand) {
            this.commands.addAll(compoundCommand.commands);
            return this;
        }

        /**
         * Adds a SetPositionCommand to the builder.
         * 
         * @param x the x coordinate
         * @param y the y coordinate
         * @return this builder instance for method chaining
         */
        public Builder addSetPosition(int x, int y) {
            this.commands.add(new SetPositionCommand(x, y));
            return this;
        }

        /**
         * Adds an OperateToCommand to the builder.
         * 
         * @param x the x coordinate
         * @param y the y coordinate
         * @return this builder instance for method chaining
         */
        public Builder addOperateTo(int x, int y) {
            this.commands.add(new OperateToCommand(x, y));
            return this;
        }

        /**
         * Builds the CompoundCommand instance with all added commands.
         * 
         * @return a new CompoundCommand instance containing all added commands
         */
        public CompoundCommand build() {
            return new CompoundCommand(commands);
        }
    }

    /**
     * Creates a new Builder instance for flexible command construction.
     * 
     * @return a new Builder instance
     */
    public static Builder builder() {
        return new Builder();
    }
}