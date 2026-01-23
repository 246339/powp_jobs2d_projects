package edu.kis.powp.jobs2d.features;

import edu.kis.powp.appbase.Application;
import edu.kis.powp.jobs2d.command.manager.CommandManager;
import edu.kis.powp.jobs2d.command.manager.LoggerCommandChangeObserver;

public class CommandsFeature implements IFeature {

    private static CommandManager commandManager;

    public CommandsFeature() {
    }

    @Override
    public void setup(Application app) {
        setupCommandManager();
    }

    private static void setupCommandManager() {
        commandManager = new CommandManager();

        LoggerCommandChangeObserver loggerObserver = new LoggerCommandChangeObserver();
        commandManager.getChangePublisher().addSubscriber(loggerObserver);
    }

    /**
     * Get manager of application driver command.
     *
     * @return plotterCommandManager.
     */
    public static CommandManager getDriverCommandManager() {
        return commandManager;
    }

    @Override
    public String getName() {
        return "Commands";
    }
}
