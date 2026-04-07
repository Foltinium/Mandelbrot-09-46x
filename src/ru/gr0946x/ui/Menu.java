package ru.gr0946x.ui;

import javax.swing.*;

public class Menu {
    private final MainWindow window;
//    поля ваших объектов

    public Menu(MainWindow window /*ваши объекты-параметры*/) {
        this.window = window;
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
//        saveAsFrac.addActionListener(e -> /*поле.метод()*/);

        JMenuItem openFile = new JMenuItem("Открыть...");
//        openFile.addActionListener(e -> /*поле.метод()*/);

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
}
