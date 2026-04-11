package ru.gr0946x.ui.io;

import ru.gr0946x.Converter;
import ru.gr0946x.ui.fractals.FractalState;
import ru.gr0946x.ui.fractals.Mandelbrot;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class FracSerializer implements FractalSerializer {

    @Override
    public void save(Component parent, Converter conv, Mandelbrot mandelbrot) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Файлы фракталов (*.frac)", "frac"));
        fileChooser.setAcceptAllFileFilterUsed(false);

        if (fileChooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            if (!file.getName().toLowerCase().endsWith(".frac")) {
                file = new File(file.getParentFile(), file.getName() + ".frac");
            }

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                FractalState state = new FractalState(
                        conv.getXMin(),
                        conv.getXMax(),
                        conv.getYMin(),
                        conv.getYMax(),
                        mandelbrot.getMaxIterations()
                );
                oos.writeObject(state);
                JOptionPane.showMessageDialog(parent, "Фрактал успешно сохранен!", "Успех", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(parent, "Ошибка сохранения: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public void open(Component parent, Converter conv, Mandelbrot mandelbrot, Runnable onSuccess) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Файлы фракталов (*.frac)", "frac"));
        fileChooser.setAcceptAllFileFilterUsed(false);

        if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                FractalState state = (FractalState) ois.readObject();

                conv.setXShape(state.xMin(), state.xMax());
                conv.setYShape(state.yMin(), state.yMax());
                mandelbrot.setMaxIterations(state.maxIterations());

                onSuccess.run();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(parent, "Ошибка загрузки файла: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void saveWithFormatChoice(Component parent, Converter conv, Mandelbrot mandelbrot, JPanel paintPanel) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Сохранить фрактал");

        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Файлы фракталов (*.frac)", "frac"));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("PNG изображения (*.png)", "png"));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("JPEG изображения (*.jpg)", "jpg"));
        fileChooser.setAcceptAllFileFilterUsed(true);
        fileChooser.setFileFilter(fileChooser.getAcceptAllFileFilter());

        if (fileChooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String extension = "frac";

            if (fileChooser.getFileFilter() instanceof FileNameExtensionFilter) {
                extension = ((FileNameExtensionFilter) fileChooser.getFileFilter()).getExtensions()[0];
            }

            if (extension.equals("frac")) {
                String path = file.getAbsolutePath();
                if (!path.toLowerCase().endsWith(".frac")) {
                    int dot = path.lastIndexOf(".");
                    path = (dot > 0 ? path.substring(0, dot) : path) + ".frac";
                    file = new File(path);
                }
                try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                    oos.writeObject(new FractalState(conv.getXMin(), conv.getXMax(), conv.getYMin(), conv.getYMax(), mandelbrot.getMaxIterations()));
                    JOptionPane.showMessageDialog(parent, "Фрактал сохранён!", "Успех", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(parent, "Ошибка: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            } else if (extension.equals("png")) {
                saveImage(parent, conv, paintPanel, file, "png");
            } else if (extension.equals("jpg")) {
                saveImage(parent, conv, paintPanel, file, "jpg");
            }
        }
    }

    private void saveImage(Component parent, Converter conv, JPanel paintPanel, File file, String format) {
        String path = file.getAbsolutePath();
        if (!path.toLowerCase().endsWith("." + format)) {
            int dot = path.lastIndexOf(".");
            path = (dot > 0 ? path.substring(0, dot) : path) + "." + format;
            file = new File(path);
        }

        try {
            BufferedImage image = new BufferedImage(paintPanel.getWidth(), paintPanel.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            paintPanel.paint(g);

            g.setFont(new Font("Monospaced", Font.BOLD, 14));
            String coords = String.format("Re: [%.5f, %.5f]  Im: [%.5f, %.5f]", conv.getXMin(), conv.getXMax(), conv.getYMin(), conv.getYMax());
            g.setColor(Color.BLACK);
            g.drawString(coords, 11, 21);
            g.setColor(Color.WHITE);
            g.drawString(coords, 10, 20);
            g.dispose();

            ImageIO.write(image, format.toUpperCase(), file);
            JOptionPane.showMessageDialog(parent, "Изображение сохранено!", "Успех", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(parent, "Ошибка: " + e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }
}
