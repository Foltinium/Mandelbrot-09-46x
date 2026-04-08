package ru.gr0946x.ui;

import ru.gr0946x.Converter;
import ru.gr0946x.ui.fractals.FractalState;
import ru.gr0946x.ui.fractals.Mandelbrot;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;

public class FractalFileManager {

    private final Component parent;
    private final Converter conv;
    private final Mandelbrot mandelbrot;

    public FractalFileManager(Component parent, Converter conv, Mandelbrot mandelbrot) {
        this.parent = parent;
        this.conv = conv;
        this.mandelbrot = mandelbrot;
    }

    public void save() {
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

    public void open(Runnable onSuccess) {
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
}