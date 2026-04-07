package ru.gr0946x.ui;

import ru.gr0946x.Converter;
import ru.gr0946x.ui.fractals.FractalState;
import ru.gr0946x.ui.fractals.Mandelbrot;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;

public class Menu {
    private final MainWindow window;
    private final Converter conv;
    private final Mandelbrot mandelbrot;
//    поля ваших объектов

    public Menu(MainWindow window, Converter conv, Mandelbrot mandelbrot /*ваши объекты-параметры*/) {
        this.window = window;
        this.conv = conv;
        this.mandelbrot = mandelbrot;
//        this.поле = параметр
        createMenu();
    }

    public void createMenu() {
        JMenuBar menuBar = createMenuBar();
        window.setJMenuBar(menuBar);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = createFileMenu();
        JMenu editMenu = createEditMenu();
        JMenu viewMenu = createViewMenu();

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(viewMenu);

        return menuBar;
    }

    private JMenu createFileMenu() {
        JMenu fileMenu = new JMenu("Файл");

        JMenu fileSave = new JMenu("Сохранить как...");
        JMenuItem saveAsPNG = new JMenuItem("Файл .png");
//        пример добавления функционала элементу меню:
//        saveAsPNG.addActionListener(e -> System.out.println("Сохранение успешно!"));

        JMenuItem saveAsJPG = new JMenuItem("Файл .jpg");
//        saveAsJPG.addActionListener(e -> /*поле.метод()*/);

        JMenuItem saveAsFrac = new JMenuItem("Файл .frac");
        saveAsFrac.addActionListener(e -> saveFractal());

        JMenuItem openFile = new JMenuItem("Открыть...");
        openFile.addActionListener(e -> openFractal());

        JMenuItem createAnimation = new JMenuItem("Создать анимацию...");
//        createAnimation.addActionListener(e -> /*поле.метод()*/);

        fileMenu.add(fileSave);
        fileSave.add(saveAsPNG);
        fileSave.add(saveAsJPG);
        fileSave.add(saveAsFrac);

        fileMenu.add(openFile);
        fileMenu.addSeparator();
        fileMenu.add(createAnimation);

        return fileMenu;
    }

    private JMenu createEditMenu() {
        JMenu editMenu = new JMenu("Правка");
        JMenuItem undo = new JMenuItem("Отменить");
        undo.setAccelerator(KeyStroke.getKeyStroke("control Z"));
//        undo.addActionListener(e -> /*поле.метод()*/);

        editMenu.add(undo);

        return editMenu;
    }

    private JMenu createViewMenu() {
        JMenu viewMenu  = new JMenu("Вид");
        JMenuItem juliaSet = new JMenuItem("Показать множество Жюлиа");
//        juliaSet.addActionListener(e -> /*поле.метод()*/);

        JMenu setFractalFunc = new JMenu("Задать функцию построения фрактала");
        JMenuItem fractalFuncA = new JMenuItem("Функция 1");
//        fractalFuncA.addActionListener(e -> /*поле.метод()*/);

        JMenuItem fractalFuncB = new JMenuItem("Функция 2");
//        fractalFuncB.addActionListener(e -> /*поле.метод()*/);

        JMenuItem fractalFuncC = new JMenuItem("Функция 3");
//        fractalFuncC.addActionListener(e -> /*поле.метод()*/);

        JMenu setColorScheme = new JMenu("Задать цветовую схему");
        JMenuItem colorSchemeA = new JMenuItem("Схема 1");
//        colorSchemeA.addActionListener(e -> /*поле.метод()*/);

        JMenuItem colorSchemeB = new JMenuItem("Схема 2");
//        colorSchemeB.addActionListener(e -> /*поле.метод()*/);

        JMenuItem colorSchemeC = new JMenuItem("Схема 3");
//        colorSchemeC.addActionListener(e -> /*поле.метод()*/);

        viewMenu.add(juliaSet);
        viewMenu.addSeparator();
        viewMenu.add(setFractalFunc);
        viewMenu.add(setColorScheme);

        setFractalFunc.add(fractalFuncA);
        setFractalFunc.add(fractalFuncB);
        setFractalFunc.add(fractalFuncC);

        setColorScheme.add(colorSchemeA);
        setColorScheme.add(colorSchemeB);
        setColorScheme.add(colorSchemeC);

        return viewMenu;
    }
    private void saveFractal() {
        JFileChooser fileChooser = new JFileChooser();
        // Устанавливаем фильтр для .frac
        fileChooser.setFileFilter(new FileNameExtensionFilter("Файлы фракталов (*.frac)", "frac"));
        fileChooser.setAcceptAllFileFilterUsed(false);

        if (fileChooser.showSaveDialog(window) == JFileChooser.APPROVE_OPTION) {
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
                JOptionPane.showMessageDialog(window, "Фрактал успешно сохранен!", "Успех", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(window, "Ошибка сохранения: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Метод открытия
    private void openFractal() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Файлы фракталов (*.frac)", "frac"));
        fileChooser.setAcceptAllFileFilterUsed(false);

        if (fileChooser.showOpenDialog(window) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                FractalState state = (FractalState) ois.readObject();

                conv.setXShape(state.xMin(), state.xMax());
                conv.setYShape(state.yMin(), state.yMax());
                mandelbrot.setMaxIterations(state.maxIterations());

                window.repaint();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(window, "Ошибка загрузки файла: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
