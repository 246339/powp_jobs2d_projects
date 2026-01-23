package edu.kis.powp.jobs2d.features;

import javax.swing.JPanel;
import javax.swing.OverlayLayout;

import edu.kis.legacy.drawer.panel.DrawPanelController;
import edu.kis.powp.appbase.Application;
import edu.kis.powp.jobs2d.canvas.CanvasManager;
import edu.kis.powp.jobs2d.canvas.ICanvas;
import edu.kis.powp.jobs2d.events.SelectCanvasOptionListener;

public class CanvasFeature implements IFeature {
    @Override
    public void setup(Application app) {
        setupCanvasPlugin(app);
    }

    @Override
    public String getName() {
        return "Canvas";
    }

    private static CanvasManager canvasManager = new CanvasManager();
    private static CanvasLayerPanel canvasOverlay;
    private static Application app;

    public static CanvasManager getCanvasManager() {
        return canvasManager;
    }

    public static DrawPanelController getCanvasDrawer() {
        return canvasOverlay.getController();
    }

    private static void setupCanvasPlugin(Application app) {
        CanvasFeature.app = app;
        JPanel panel = app.getFreePanel();
        // OverlayLayout is used to add the canvas overlay on top of the drawing panel
        // so changing the canvas will not affect the drawing panel
        panel.setLayout(new OverlayLayout(panel));

        canvasOverlay = new CanvasLayerPanel();
        canvasOverlay.setCanvas(canvasManager.getCurrentCanvas());

        canvasManager.getChangePublisher().addSubscriber(() -> {
            canvasOverlay.setCanvas(canvasManager.getCurrentCanvas());
        });

        panel.add(canvasOverlay);

        app.addComponentMenu(CanvasFeature.class, "Canvas");
    }

    public static void addCanvas(ICanvas canvas) {
        app.addComponentMenuElement(CanvasFeature.class, canvas.getName(), new SelectCanvasOptionListener(canvas));
    }

    public static void addCanvas(String name, ICanvas canvas) {
        app.addComponentMenuElement(CanvasFeature.class, name, new SelectCanvasOptionListener(canvas));
    }
}
