package ru.gr0946x.ui.io;

import ru.gr0946x.Converter;
import ru.gr0946x.ui.MainWindow;
import ru.gr0946x.ui.fractals.Mandelbrot;


import javax.swing.*;

public class Menu {
    private final MainWindow mainWindow;
    private final FractalFileManager fileManager;
    private final FractalSerializer fracSerializer;

    public Menu(MainWindow mainWindow, FractalSerializer fracSerializer, FractalFileManager fileManager, ImageSerializer imageSerializer) {
        this.mainWindow = mainWindow;
        this.fracSerializer = fracSerializer;
        this.fileManager = fileManager;
        createMenu();
    }

    public void createMenu() {
        mainWindow.setJMenuBar(createMenuBar());
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
        fileMenu.setMnemonic('F');

        JMenuItem saveAsItem = new JMenuItem("Сохранить как...");
        saveAsItem.addActionListener(e -> mainWindow.saveFractal());
        saveAsItem.setAccelerator(KeyStroke.getKeyStroke("control S"));

        JMenuItem openItem = new JMenuItem("Открыть...");
        openItem.addActionListener(e -> mainWindow.openFile());
        openItem.setAccelerator(KeyStroke.getKeyStroke("control O"));

        JMenuItem createAnimationItem = new JMenuItem("Создать анимацию...");
        createAnimationItem.setAccelerator(KeyStroke.getKeyStroke("control N"));

        fileMenu.add(saveAsItem);
        fileMenu.addSeparator();
        fileMenu.add(openItem);
        fileMenu.addSeparator();
        fileMenu.add(createAnimationItem);

        return fileMenu;
    }

    private JMenu createEditMenu() {
        JMenu editMenu = new JMenu("Правка");
        editMenu.setMnemonic('E');

        JMenuItem undoItem = new JMenuItem("Отменить");
        undoItem.setAccelerator(KeyStroke.getKeyStroke("control Z"));

        undoItem.setEnabled(mainWindow.canUndo());
        undoItem.addActionListener(e -> mainWindow.triggerUndo());

        editMenu.addMenuListener(new javax.swing.event.MenuListener() {
            @Override
            public void menuSelected(javax.swing.event.MenuEvent e) {
                undoItem.setEnabled(mainWindow.canUndo());
            }
            @Override
            public void menuDeselected(javax.swing.event.MenuEvent e) {}
            @Override
            public void menuCanceled(javax.swing.event.MenuEvent e) {}
        });

        editMenu.add(undoItem);
        return editMenu;
    }

    private JMenu createViewMenu() {
        JMenu viewMenu = new JMenu("Вид");
        viewMenu.setMnemonic('V');

        JMenu setFractalFuncMenu = new JMenu("Задать функцию построения фрактала");
        ButtonGroup functionGroup = new ButtonGroup();
        JRadioButtonMenuItem fractalFunc1Item = new JRadioButtonMenuItem("Функция 1");
        fractalFunc1Item.setSelected(true);
        JRadioButtonMenuItem fractalFunc2Item = new JRadioButtonMenuItem("Функция 2");
        JRadioButtonMenuItem fractalFunc3Item = new JRadioButtonMenuItem("Функция 3");

        JMenu setColorSchemeMenu = new JMenu("Задать цветовую схему");
        ButtonGroup colorSchemeGroup = new ButtonGroup();
        JRadioButtonMenuItem colorScheme1Item = new JRadioButtonMenuItem("Схема 1");
        colorScheme1Item.setSelected(true);
        JRadioButtonMenuItem colorScheme2Item = new JRadioButtonMenuItem("Схема 2");
        JRadioButtonMenuItem colorScheme3Item = new JRadioButtonMenuItem("Схема 3");

        JCheckBoxMenuItem adaptiveIterationsItem = new JCheckBoxMenuItem("Адаптивное число итераций");
        adaptiveIterationsItem.addActionListener(e -> mainWindow.setAdaptiveIterationsEnabled(adaptiveIterationsItem.isSelected()));
        adaptiveIterationsItem.setSelected(true);
        adaptiveIterationsItem.setAccelerator(KeyStroke.getKeyStroke("control I"));

        functionGroup.add(fractalFunc1Item);
        functionGroup.add(fractalFunc2Item);
        functionGroup.add(fractalFunc3Item);

        setFractalFuncMenu.add(fractalFunc1Item);
        setFractalFuncMenu.add(fractalFunc2Item);
        setFractalFuncMenu.add(fractalFunc3Item);

        colorSchemeGroup.add(colorScheme1Item);
        colorSchemeGroup.add(colorScheme2Item);
        colorSchemeGroup.add(colorScheme3Item);

        setColorSchemeMenu.add(colorScheme1Item);
        setColorSchemeMenu.add(colorScheme2Item);
        setColorSchemeMenu.add(colorScheme3Item);

        viewMenu.add(setFractalFuncMenu);
        viewMenu.add(setColorSchemeMenu);
        viewMenu.addSeparator();
        viewMenu.add(adaptiveIterationsItem);

        return viewMenu;
    }
}