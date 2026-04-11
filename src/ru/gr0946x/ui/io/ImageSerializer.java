package ru.gr0946x.ui.io;

import ru.gr0946x.Converter;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImageSerializer {

    public void saveAsPNG(Component parent, Converter conv, JPanel paintPanel) {
        saveAsImage(parent, conv, paintPanel, "png");
    }

    public void saveAsJPEG(Component parent, Converter conv, JPanel paintPanel) {
        saveAsImage(parent, conv, paintPanel, "jpg");
    }

    public void openImage(Component parent, JPanel paintPanel) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Открыть изображение");

        FileNameExtensionFilter pngFilter = new FileNameExtensionFilter("PNG изображения (*.png)", "png");
        FileNameExtensionFilter jpgFilter = new FileNameExtensionFilter("JPEG изображения (*.jpg)", "jpg");

        fileChooser.addChoosableFileFilter(pngFilter);
        fileChooser.addChoosableFileFilter(jpgFilter);
        fileChooser.setAcceptAllFileFilterUsed(true);

        if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                BufferedImage image = ImageIO.read(file);
                if (image != null) {
                    Graphics g = paintPanel.getGraphics();
                    g.drawImage(image, 0, 0, paintPanel.getWidth(), paintPanel.getHeight(), null);
                    JOptionPane.showMessageDialog(parent, "Изображение загружено!", "Успех", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(parent, "Не удалось прочитать изображение", "Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(parent, "Ошибка открытия: " + e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveAsImage(Component parent, Converter conv, JPanel paintPanel, String format) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Сохранить как " + format.toUpperCase());
        fileChooser.setFileFilter(new FileNameExtensionFilter(
                format.toUpperCase() + " изображения (*." + format + ")", format));
        fileChooser.setAcceptAllFileFilterUsed(true);

        if (fileChooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String path = file.getAbsolutePath();

            String correctExtension = "." + format;
            if (format.equals("jpg")) {
                if (path.toLowerCase().endsWith(".jpg") || path.toLowerCase().endsWith(".jpeg")) {
                    correctExtension = null;
                }
            } else {
                if (path.toLowerCase().endsWith(correctExtension)) {
                    correctExtension = null;
                }
            }

            if (correctExtension != null) {
                int lastDot = path.lastIndexOf(".");
                if (lastDot > 0) {
                    path = path.substring(0, lastDot);
                }
                path = path + correctExtension;
                file = new File(path);
            }

            try {
                BufferedImage image = new BufferedImage(
                        paintPanel.getWidth(),
                        paintPanel.getHeight(),
                        BufferedImage.TYPE_INT_RGB
                );
                Graphics2D g2d = image.createGraphics();
                paintPanel.paint(g2d);

                g2d.setFont(new Font("Monospaced", Font.BOLD, 14));
                String coords = String.format("Re: [%.5f, %.5f]  Im: [%.5f, %.5f]",
                        conv.getXMin(), conv.getXMax(),
                        conv.getYMin(), conv.getYMax());

                g2d.setColor(Color.BLACK);
                g2d.drawString(coords, 11, 21);
                g2d.setColor(Color.WHITE);
                g2d.drawString(coords, 10, 20);

                g2d.dispose();

                ImageIO.write(image, format.toUpperCase(), file);

                JOptionPane.showMessageDialog(parent,
                        "Изображение сохранено в " + format.toUpperCase() + "!\n" + file.getName(),
                        "Успех", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(parent,
                        "Ошибка сохранения: " + e.getMessage(),
                        "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}