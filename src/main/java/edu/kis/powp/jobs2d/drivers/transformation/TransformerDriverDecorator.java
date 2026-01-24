package edu.kis.powp.jobs2d.drivers.transformation;

import edu.kis.powp.jobs2d.Job2dDriver;
import edu.kis.powp.jobs2d.visitor.DriverVisitor;
import edu.kis.powp.jobs2d.visitor.VisitableJob2dDriver;

public class TransformerDriverDecorator implements VisitableJob2dDriver {
    private final VisitableJob2dDriver driver;
    private final TransformStrategy strategy;

    public TransformerDriverDecorator(VisitableJob2dDriver driver, TransformStrategy strategy) {
        this.driver = driver;
        this.strategy = strategy;
    }

    public VisitableJob2dDriver getDriver() {
        return driver;
    }

    public TransformStrategy getStrategy() {
        return strategy;
    }

    @Override
    public void setPosition(int x, int y) {
        TransformCords cords = strategy.transform(new TransformCords(x, y));
        this.driver.setPosition(cords.x, cords.y);
    }

    @Override
    public void operateTo(int x, int y) {
        TransformCords cords = strategy.transform(new TransformCords(x, y));
        this.driver.operateTo(cords.x, cords.y);
    }

    @Override
    public String toString() {
        return "Transformer Driver";
    }

    @Override
    public void accept(DriverVisitor visitor) {
        visitor.visit(this);
    }
}