package ru.gr0946x.ui;

import ru.gr0946x.Converter;
import ru.gr0946x.ui.fractals.Mandelbrot;
import ru.gr0946x.ui.io.FracSerializer;
import ru.gr0946x.ui.io.FractalFileManager;
import ru.gr0946x.ui.io.ImageSerializer;
import ru.gr0946x.ui.io.Menu;
import ru.gr0946x.ui.painting.FractalPainter;
import ru.gr0946x.ui.painting.Painter;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

import static java.lang.Math.*;

public class MainWindow extends JFrame {
    private final SelectablePanel mainPanel;
    private final Painter painter;
    private final Mandelbrot mandelbrot;
    private final Converter conv;
    private final FracSerializer fracSerializer;
    private final FractalFileManager fileManager;
    private final ImageSerializer imageSerializer;
    private boolean adaptiveIterationsEnabled = true;

    public MainWindow() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(800, 650));

        mandelbrot = new Mandelbrot();
        conv = new Converter(-2.0, 1.0, -1.0, 1.0);
        fracSerializer = new FracSerializer();
        fileManager = new FractalFileManager(this, conv, mandelbrot);
        imageSerializer = new ImageSerializer();

        painter = new FractalPainter(mandelbrot, conv, (value) -> {
            if (value == 1.0) return Color.BLACK;
            var r = (float)abs(sin(5 * value));
            var g = (float)abs(cos(8 * value) * sin (3 * value));
            var b = (float)abs((sin(7 * value) + cos(15 * value)) / 2f);
            return new Color(r, g, b);
        });

        mainPanel = new SelectablePanel(painter);
        mainPanel.setBackground(Color.WHITE);

        mainPanel.addSelectListener((r) -> {
            if (r.width <= 2 || r.height <= 2) {
                return;
            }
            var xMin = conv.xScr2Crt(r.x);
            var xMax = conv.xScr2Crt(r.x + r.width);
            var yMin = conv.yScr2Crt(r.y + r.height);
            var yMax = conv.yScr2Crt(r.y);
            conv.setXShape(xMin, xMax);
            conv.setYShape(yMin, yMax);

            if (adaptiveIterationsEnabled) {
                double zoomFactor = 3.0 / (xMax - xMin);
                mandelbrot.setMaxIterations(Math.max(100, (int)(100 * (1 + Math.log10(zoomFactor)))));
            }

            mainPanel.repaint();
        });

        new Menu(this, fracSerializer, fileManager, imageSerializer);

        setContent();
    }

    private void setContent() {
        var gl = new GroupLayout(getContentPane());
        setLayout(gl);

        gl.setVerticalGroup(gl.createSequentialGroup()
                .addGap(8)
                .addComponent(mainPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                .addGap(8)
        );

        gl.setHorizontalGroup(gl.createSequentialGroup()
                .addGap(8)
                .addComponent(mainPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                .addGap(8)
        );
    }

    public void saveFractal() {
        fracSerializer.saveWithFormatChoice(this, conv, mandelbrot, mainPanel);
    }

    public void saveAsPNG() {
        imageSerializer.saveAsPNG(this, conv, mainPanel);
    }

    public void saveAsJPEG() {
        imageSerializer.saveAsJPEG(this, conv, mainPanel);
    }

    public void openImage() {
        imageSerializer.openImage(this, mainPanel);
    }

    public void setAdaptiveIterationsEnabled(boolean enabled) {
        this.adaptiveIterationsEnabled = enabled;
    }

    public void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Открыть файл");

        FileNameExtensionFilter fracFilter = new FileNameExtensionFilter("Файлы фракталов (*.frac)", "frac");
        FileNameExtensionFilter pngFilter = new FileNameExtensionFilter("PNG изображения (*.png)", "png");
        FileNameExtensionFilter jpgFilter = new FileNameExtensionFilter("JPEG изображения (*.jpg)", "jpg");

        fileChooser.addChoosableFileFilter(fracFilter);
        fileChooser.addChoosableFileFilter(pngFilter);
        fileChooser.addChoosableFileFilter(jpgFilter);
        fileChooser.setAcceptAllFileFilterUsed(true);
        fileChooser.setFileFilter(fileChooser.getAcceptAllFileFilter());

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String extension = "";
            String name = file.getName().toLowerCase();
            if (name.endsWith(".frac")) extension = "frac";
            else if (name.endsWith(".png")) extension = "png";
            else if (name.endsWith(".jpg") || name.endsWith(".jpeg")) extension = "jpg";

            if (extension.equals("frac")) {
                fileManager.open(fracSerializer, this::repaint);
            } else if (extension.equals("png") || extension.equals("jpg")) {
                imageSerializer.openImage(this, mainPanel);
            } else {
                JOptionPane.showMessageDialog(this, "Неподдерживаемый формат файла", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}