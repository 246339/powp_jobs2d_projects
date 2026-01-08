package edu.kis.powp.jobs2d.features;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import edu.kis.powp.appbase.Application;
import edu.kis.powp.jobs2d.Job2dDriver;

/**
 * Monitoring helper used from TestJobs2dApp: decorates any driver and
 * logs total travel (all moves) plus drawing usage (ink/filament) to the logger panel.
 */
public final class MonitoringFeature {

    // Every monitored driver lives here so menu actions can iterate them.
    private static final List<UsageTrackingDriver> trackedDrivers = new ArrayList<>();
    // Logger target; defaults to the application's main logger panel.
    private static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private MonitoringFeature() {
    }

    /**
     * Called once in TestJobs2dApp during startup.
     * Adds a "Monitoring" menu with two actions: print summaries and reset counters.
     */
    public static void setupMonitoringPlugin(Application app, Logger monitoringLogger) {
        if (monitoringLogger != null) {
            logger = monitoringLogger;
        }

        app.addComponentMenu(MonitoringFeature.class, "Monitoring", 0);
        app.addComponentMenuElement(MonitoringFeature.class, "Report usage summary", MonitoringFeature::logAllUsage);
        app.addComponentMenuElement(MonitoringFeature.class, "Reset usage counters", MonitoringFeature::resetAll);
    }

    /**
     * Used when drivers are registered (see TestJobs2dApp.setupDrivers).
     * Wrap a driver so every move/draw is counted without touching its code.
     */
    public static Job2dDriver monitoredDriver(Job2dDriver baseDriver, String label) {
        UsageTrackingDriver driver = new UsageTrackingDriver(baseDriver, label, logger);
        trackedDrivers.add(driver);
        return driver;
    }

    // Menu action: print a short summary for each monitored driver.
    private static void logAllUsage(ActionEvent e) {
        if (trackedDrivers.isEmpty()) {
            logger.info("Monitoring: no drivers registered for tracking");
            return;
        }
        trackedDrivers.forEach(UsageTrackingDriver::logSummary);
    }

    // Menu action: reset counters on every monitored driver.
    private static void resetAll(ActionEvent e) {
        trackedDrivers.forEach(UsageTrackingDriver::reset);
        logger.info("Monitoring: counters reset");
    }

    /**
     * Decorator that counts distance while delegating real work to the wrapped driver.
     */
    private static final class UsageTrackingDriver implements Job2dDriver {
        private final Job2dDriver delegate;
        private final Logger logger;
        private final String label;

        private int lastX = 0;
        private int lastY = 0;
        private double travelDistance = 0.0;
        private double drawingDistance = 0.0;

        UsageTrackingDriver(Job2dDriver delegate, String label, Logger logger) {
            this.delegate = delegate;
            this.label = label;
            this.logger = logger == null ? Logger.getLogger(Logger.GLOBAL_LOGGER_NAME) : logger;
        }

        // Called whenever the app repositions without drawing.
        @Override
        public void setPosition(int x, int y) {
            registerMovement(x, y, false);
            delegate.setPosition(x, y);
            updatePosition(x, y);
        }

        // Called whenever the app draws a line to a point.
        @Override
        public void operateTo(int x, int y) {
            registerMovement(x, y, true);
            delegate.operateTo(x, y);
            updatePosition(x, y);
        }

        // Shared counting logic for move/draw.
        private void registerMovement(int x, int y, boolean drawing) {
            double segment = Math.hypot(x - lastX, y - lastY);
            travelDistance += segment; // Every move counts as travel.
            if (drawing) {
                drawingDistance += segment; // Drawing uses ink/filament.
            }
            logger.info(String.format("[%s] %s to (%d, %d); segment=%.2f; travel=%.2f; ink=%.2f", label,
                    drawing ? "draw" : "move", x, y, segment, travelDistance, drawingDistance));
        }

        private void updatePosition(int x, int y) {
            this.lastX = x;
            this.lastY = y;
        }

        // Prints current counters for this driver.
        void logSummary() {
            logger.info(String.format("[%s] usage summary -> travel=%.2f, ink=%.2f", label, travelDistance,
                    drawingDistance));
        }

        // Resets counters to zero.
        void reset() {
            travelDistance = 0.0;
            drawingDistance = 0.0;
            lastX = 0;
            lastY = 0;
            logger.info(String.format("[%s] monitoring counters reset", label));
        }

        @Override
        public String toString() {
            return String.format("%s [monitored]", delegate.toString());
        }
    }
}
