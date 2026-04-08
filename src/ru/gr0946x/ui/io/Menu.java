package ru.gr0946x.ui.io;

import ru.gr0946x.Converter;
import ru.gr0946x.ui.MainWindow;
import ru.gr0946x.ui.fractals.Mandelbrot;
import ru.smak.math.Complex;


import javax.swing.*;

public class Menu {
    private final MainWindow window;
    private final FractalFileManager fileManager;
    private final FractalSerializer fracSerializer = new FracSerializer();

    public Menu(MainWindow window, Converter conv, Mandelbrot mandelbrot) {
        this.window = window;
        this.fileManager = new FractalFileManager(window, conv, mandelbrot);
        createMenu();
    }

    public void createMenu() {
        window.setJMenuBar(createMenuBar());
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createFileMenu());
        menuBar.add(createEditMenu());
        menuBar.add(createViewMenu());
        return menuBar;
    }

    private JMenu createFileMenu() {
        JMenu fileMenu = new JMenu("Файл");

        JMenu fileSave = new JMenu("Сохранить как...");
        JMenuItem saveAsPNG = new JMenuItem("Файл .png");
        JMenuItem saveAsJPG = new JMenuItem("Файл .jpg");

        JMenuItem saveAsFrac = new JMenuItem("Файл .frac");
        saveAsFrac.addActionListener(e -> fileManager.save(fracSerializer));

        JMenuItem openFile = new JMenuItem("Открыть...");
        openFile.addActionListener(e -> fileManager.open(fracSerializer, window::repaint));

        JMenuItem createAnimation = new JMenuItem("Создать анимацию...");

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
        editMenu.add(undo);
        return editMenu;
    }

    private JMenu createViewMenu() {
        JMenu viewMenu = new JMenu("Вид");
        JMenuItem juliaSet = new JMenuItem("Показать множество Жюлиа");

        JMenu setFractalFunc = new JMenu("Задать функцию построения фрактала");
        JMenuItem fractalFuncA = new JMenuItem("Функция 1");
        JMenuItem fractalFuncB = new JMenuItem("Функция 2");
        JMenuItem fractalFuncC = new JMenuItem("Функция 3");

        JMenu setColorScheme = new JMenu("Задать цветовую схему");
        JMenuItem colorSchemeA = new JMenuItem("Схема 1");
        JMenuItem colorSchemeB = new JMenuItem("Схема 2");
        JMenuItem colorSchemeC = new JMenuItem("Схема 3");

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

        fractalFuncA.addActionListener(e -> window.setCurrentFractal((x, y) -> {
            // z² + c — классический Мандельброт
            var c = new Complex(x, y);
            var z = new Complex();
            int i = 0;
            int maxIt = 100;
            while (z.getAbsoluteValue2() < 4 && ++i < maxIt) {
                z.timesAssign(z);
                z.plusAssign(c);
            }
            return (float) i / maxIt;
        }));
        fractalFuncB.addActionListener(e -> window.setCurrentFractal((x, y) -> {
            // z⁴ + c
            var c = new Complex(x, y);
            var z = new Complex();
            int i = 0;
            int maxIt = 100;
            while (z.getAbsoluteValue2() < 4 && ++i < maxIt) {
                z.timesAssign(z);
                z.timesAssign(z);
                z.plusAssign(c);
            }
            return (float) i / maxIt;
        }));
        return viewMenu;

    }

}