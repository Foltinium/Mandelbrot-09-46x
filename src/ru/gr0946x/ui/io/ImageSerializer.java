package ru.gr0946x.ui.io;

import ru.gr0946x.Converter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImageSerializer {
    private BufferedImage currentImage = null;
    private boolean isImageMode = false;

    public void openImage(Component parent, JPanel paintPanel, File file) {
        try {
            currentImage = ImageIO.read(file);
            if (currentImage != null) {
                isImageMode = true;
                paintPanel.repaint();
                JOptionPane.showMessageDialog(parent, "Изображение загружено!", "Успех", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(parent, "Не удалось прочитать изображение", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(parent, "Ошибка открытия: " + e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void drawImage(Graphics g, int width, int height) {
        if (currentImage != null) {
            g.drawImage(currentImage, 0, 0, width, height, null);
        }
    }

    public void clearImage() {
        currentImage = null;
        isImageMode = false;
    }

    public boolean isImageMode() {
        return isImageMode;
    }

    public void saveImage(Component parent, Converter conv, JPanel paintPanel, File file, String format) {
        // Проверка поддерживаемых форматов
        if (!format.equals("png") && !format.equals("jpg")) {
            JOptionPane.showMessageDialog(parent,
                    "Поддерживаются только форматы PNG и JPEG",
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String path = file.getAbsolutePath();
        String targetExt = "." + format.toLowerCase();

        // (file.png.png -> file.png)
        String lowerPath = path.toLowerCase();
        while (lowerPath.endsWith(targetExt)) {
            path = path.substring(0, path.length() - (format.length() + 1));
            lowerPath = path.toLowerCase();
        }

        boolean hasPng = lowerPath.endsWith(".png");
        boolean hasJpg = lowerPath.endsWith(".jpg") || lowerPath.endsWith(".jpeg");

        if (format.equals("png") && !hasPng) {
            path = path + ".png";
        } else if (format.equals("jpg") && !hasJpg) {
            path = path + ".jpg";
        }

        File finalFile = new File(path);

        // запрос на перезапись
        if (finalFile.exists()) {
            int result = JOptionPane.showConfirmDialog(parent,
                    "Файл '" + finalFile.getName() + "' уже существует.\nПерезаписать?",
                    "Подтверждение сохранения",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (result != JOptionPane.YES_OPTION) {
                return;
            }
        }

        try {
            BufferedImage image = new BufferedImage(paintPanel.getWidth(), paintPanel.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            paintPanel.paint(g);

            g.setFont(new Font("Monospaced", Font.BOLD, 14));
            String coords = String.format("Re: [%.5f, %.5f]  Im: [%.5f, %.5f]",
                    conv.getXMin(), conv.getXMax(), conv.getYMin(), conv.getYMax());

            g.setColor(Color.BLACK);
            g.drawString(coords, 11, 21);
            g.setColor(Color.WHITE);
            g.drawString(coords, 10, 20);
            g.dispose();

            ImageIO.write(image, format.toUpperCase(), finalFile);
            JOptionPane.showMessageDialog(parent,
                    "Изображение сохранено как: " + finalFile.getName(),
                    "Успех",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(parent,
                    "Ошибка сохранения: " + e.getMessage(),
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}