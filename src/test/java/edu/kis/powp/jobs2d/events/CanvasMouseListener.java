package edu.kis.powp.jobs2d.events;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import static java.awt.event.MouseEvent.BUTTON1;
import static java.awt.event.MouseEvent.BUTTON3;
import java.awt.event.MouseListener;

import edu.kis.powp.jobs2d.Job2dDriver;
import edu.kis.powp.jobs2d.features.DriverFeature;

public class CanvasMouseListener implements MouseListener {

    @Override
    public void mouseClicked(MouseEvent e) {

        switch (e.getButton()) {
            case BUTTON1:
                leftMouseClicked(e);
                break;
            case BUTTON3:
                rightMouseClicked(e);
                break;
            default:
                break;
        }
    }
    
    private Point calculateCanvasPosition(MouseEvent e) {
        Component canvas = (Component) e.getSource();
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        Point point = e.getPoint();
        return new Point(point.x - width / 2, point.y - height / 2);
    }

    private void rightMouseClicked(MouseEvent e) {
        Point point = calculateCanvasPosition(e);
        Job2dDriver driver = DriverFeature.getDriverManager().getCurrentDriver();

		driver.setPosition(point.x, point.y);
    }
    
	public void leftMouseClicked(MouseEvent e) {
        Point point = calculateCanvasPosition(e);
        Job2dDriver driver = DriverFeature.getDriverManager().getCurrentDriver();

		driver.operateTo(point.x, point.y);
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
