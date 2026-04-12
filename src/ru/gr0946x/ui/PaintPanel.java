package ru.gr0946x.ui;

import ru.gr0946x.ui.io.ImageSerializer;
import ru.gr0946x.ui.painting.Painter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class PaintPanel extends JPanel {

    private Painter painter;
    private ImageSerializer imageSerializer;

    public PaintPanel(Painter painter, ImageSerializer imageSerializer) {
        this.painter = painter;
        this.imageSerializer = imageSerializer;
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                painter.setWidth(getWidth());
                painter.setHeight(getHeight());
                repaint();
            }
        });
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        painter.paint(g);
        if (imageSerializer != null) {
            imageSerializer.drawImage(g, getWidth(), getHeight());
        }
    }
}