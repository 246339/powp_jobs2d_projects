package edu.kis.powp.jobs2d.visitor;

import java.util.ArrayList;
import java.util.Iterator;

import edu.kis.powp.jobs2d.Job2dDriver;
import edu.kis.powp.jobs2d.drivers.AnimatedDriverDecorator;
import edu.kis.powp.jobs2d.drivers.DriverComposite;
import edu.kis.powp.jobs2d.drivers.LoggerDriver;
import edu.kis.powp.jobs2d.drivers.RecordingDriverDecorator;
import edu.kis.powp.jobs2d.drivers.adapter.LineDriverAdapter;
import edu.kis.powp.jobs2d.drivers.transformation.TransformerDriverDecorator;
import edu.kis.powp.jobs2d.drivers.UsageTrackingDriverDecorator;
import edu.kis.legacy.drawer.shape.ILine;

public class DriverDeepCopyVisitor implements DriverVisitor {

    private VisitableJob2dDriver copy;

    public VisitableJob2dDriver getCopy() {
        return copy;
    }

    @Override
    public void visit(AnimatedDriverDecorator animatedDriverDecorator) {
        animatedDriverDecorator.getTargetDriver().accept(this);
        VisitableJob2dDriver targetCopy = copy;
        copy = new AnimatedDriverDecorator(targetCopy, animatedDriverDecorator.getDelayMs());
    }

    @Override
    public void visit(DriverComposite driverComposite) {
        DriverComposite compositeCopy = new DriverComposite(new ArrayList<>());
        Iterator<VisitableJob2dDriver> it = driverComposite.iterator();
        while (it.hasNext()) {
            VisitableJob2dDriver child = it.next();
            child.accept(this);
            compositeCopy.add(copy);
        }
        copy = compositeCopy;
    }

    @Override
    public void visit(LoggerDriver loggerDriver) {
        copy = new LoggerDriver(loggerDriver.getLogger());
    }

    @Override
    public void visit(LineDriverAdapter lineDriverAdapter) {
        try {
            copy = new LineDriverAdapter(lineDriverAdapter.getDrawController(), (ILine) lineDriverAdapter.getLine().clone(), lineDriverAdapter.getName());
        } catch (CloneNotSupportedException e) {
            copy = new LineDriverAdapter(lineDriverAdapter.getDrawController(), lineDriverAdapter.getLine(), lineDriverAdapter.getName());
        }
    }

    @Override
    public void visit(TransformerDriverDecorator transformerDriverDecorator) {
        transformerDriverDecorator.getDriver().accept(this);
        VisitableJob2dDriver innerCopy = copy;
        copy = new TransformerDriverDecorator(innerCopy, transformerDriverDecorator.getStrategy());
    }

    @Override
    public void visit(UsageTrackingDriverDecorator usageTrackingDriverDecorator) {
        usageTrackingDriverDecorator.getDelegate().accept(this);
        VisitableJob2dDriver targetCopy = copy;
        copy = new UsageTrackingDriverDecorator(targetCopy, usageTrackingDriverDecorator.getLabel());
    }

    @Override
    public void visit(RecordingDriverDecorator recordingDriverDecorator) {
        recordingDriverDecorator.getDelegate().accept(this);
        VisitableJob2dDriver targetCopy = copy;
        copy = new RecordingDriverDecorator(targetCopy);
    }
}
