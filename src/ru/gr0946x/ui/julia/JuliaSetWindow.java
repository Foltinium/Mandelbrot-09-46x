package ru.gr0946x.ui.julia;

import ru.gr0946x.Converter;
import ru.gr0946x.ui.painting.FractalPainter;
import ru.gr0946x.ui.painting.Painter;
import ru.smak.math.Complex;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static java.lang.Math.abs;
import static java.lang.Math.sin;
import static java.lang.Math.cos;

public class JuliaSetWindow extends JFrame {
    private final JuliaFractal juliaFractal;
    private final Converter converter;
    private final JPanel fractalPanel;

    public JuliaSetWindow(Complex point) {
        setTitle("Множество Жюлия: " + formatComplex(point));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 600);
        setMinimumSize(new Dimension(400, 400));

        converter = new Converter(-2.0, 2.0, -2.0, 2.0);
        juliaFractal = new JuliaFractal(point);

        Painter painter = new FractalPainter(juliaFractal, converter, (value) -> {
            if (value == 1.0) return Color.BLACK;
            float r = (float) abs(sin(5 * value));
            float g = (float) abs(cos(8 * value) * sin(3 * value));
            float b = (float) abs((sin(7 * value) + cos(15 * value)) / 2f);
            return new Color(r, g, b);
        });

        fractalPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getWidth() > 0 && getHeight() > 0) {
                    painter.setWidth(getWidth());
                    painter.setHeight(getHeight());
                    painter.paint(g);
                }
            }
        };

        fractalPanel.setBackground(Color.BLACK);

        // Клик для смены константы
        fractalPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                double x = converter.xScr2Crt(e.getX());
                double y = converter.yScr2Crt(e.getY());
                Complex newPoint = new Complex(x, y);
                juliaFractal.setConstant(newPoint);
                setTitle("Множество Жюлия: " + formatComplex(newPoint));
                fractalPanel.repaint();
            }
        });

        // Зум колёсиком
        fractalPanel.addMouseWheelListener(e -> {
            double zoomFactor = e.getWheelRotation() < 0 ? 0.9 : 1.1;
            double mouseX = converter.xScr2Crt(e.getX());
            double mouseY = converter.yScr2Crt(e.getY());

            double newWidth = (converter.getXMax() - converter.getXMin()) * zoomFactor;
            double newHeight = (converter.getYMax() - converter.getYMin()) * zoomFactor;

            double newXMin = mouseX - newWidth * (mouseX - converter.getXMin()) / (converter.getXMax() - converter.getXMin());
            double newXMax = newXMin + newWidth;
            double newYMin = mouseY - newHeight * (mouseY - converter.getYMin()) / (converter.getYMax() - converter.getYMin());
            double newYMax = newYMin + newHeight;

            converter.setXShape(newXMin, newXMax);
            converter.setYShape(newYMin, newYMax);
            fractalPanel.repaint();
        });

        // Кнопка сброса
        JButton resetButton = new JButton("Сбросить вид");
        resetButton.addActionListener(_ -> {
            converter.setXShape(-2.0, 2.0);
            converter.setYShape(-2.0, 2.0);
            fractalPanel.repaint();
        });

        setLayout(new BorderLayout());
        add(fractalPanel, BorderLayout.CENTER);
        add(resetButton, BorderLayout.NORTH);
    }

    private String formatComplex(Complex c) {
        return String.format("%.4f %+.4fi", c.getReal(), c.getImaginary());
    }
}