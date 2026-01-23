package edu.kis.powp.jobs2d.features;

import edu.kis.powp.appbase.Application;
import edu.kis.powp.jobs2d.visitor.VisitableJob2dDriver;
import edu.kis.powp.jobs2d.drivers.DriverManager;
import edu.kis.powp.jobs2d.drivers.SelectDriverMenuOptionListener;

public class DriverFeature implements IFeature {

    private static DriverManager driverManager = new DriverManager();
    private static Application app;

    public DriverFeature() {
    }

    public static DriverManager getDriverManager() {
        return driverManager;
    }

    @Override
    public void setup(Application application) {
        app = application;
        setupDriverPlugin(application);
    }

    /**
     * Setup jobs2d drivers Plugin and add to application.
     *
     * @param application Application context.
     */
    private static void setupDriverPlugin(Application application) {
        app = application;
        app.addComponentMenu(DriverFeature.class, "Drivers");
    }

    /**
     * Add driver to context, create button in driver menu.
     *
     * @param name   Button name.
     * @param driver VisitableJob2dDriver object.
     */
    public static void addDriver(String name, VisitableJob2dDriver driver) {
        SelectDriverMenuOptionListener listener = new SelectDriverMenuOptionListener(driver, driverManager);
        app.addComponentMenuElement(DriverFeature.class, name, listener);
    }

    /**
     * Update driver info.
     */
    public static void updateDriverInfo() {
        app.updateInfo(driverManager.getCurrentDriver().toString());
    }

    @Override
    public String getName() {
        return "Driver";
    }

}
