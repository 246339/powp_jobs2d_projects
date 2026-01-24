package edu.kis.powp.jobs2d.events;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

import edu.kis.powp.jobs2d.visitor.VisitableJob2dDriver;
import edu.kis.powp.jobs2d.features.DriverFeature;
import edu.kis.powp.jobs2d.visitor.DriverCounterVisitor;

/**
 * Listener implementation that counts the number of drivers in the current driver structure using DriverCounterVisitor.
 * Similar to SelectCountCommandOptionListener but for drivers instead of commands.
 */
public class SelectCountDriverOptionListener implements ActionListener {

    private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /**
     * Triggered when the action is performed.
     * Retrieves the current driver, counts its sub-drivers, and logs the result.
     * @param e the action event.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        VisitableJob2dDriver driver = DriverFeature.getDriverManager().getCurrentDriver();

        if (driver == null) {
            logger.warning("No driver loaded.");
            return;
        }

        DriverCounterVisitor.DriverStats stats = DriverCounterVisitor.countDrivers(driver);

        logger.info("The driver structure consists of " + stats.getCount() + " drivers.\n" +
                    "LoggerDriver count: " + stats.getLoggerDriverCount() + "\n" +
                    "AnimatedDriverDecorator count: " + stats.getAnimatedDriverDecoratorCount() + "\n" +
                    "LineDriverAdapter count: " + stats.getLineDriverAdapterCount() + "\n" +
                    "TransformerDriverDecorator count: " + stats.getTransformerDriverDecoratorCount() + "\n" +
                    "UsageTrackingDriverDecorator count: " + stats.getUsageTrackingDriverDecoratorCount() + "\n" +
                    "RecordingDriverDecorator count: " + stats.getRecordingDriverDecoratorCount());
    }
}