package ru.gr0946x.ui.io;

import javax.swing.*;

public class MenuManager {
    private final JFrame window;
    private final MenuProvider provider;

    public MenuManager(JFrame window, MenuProvider provider) {
        this.window = window;
        this.provider = provider;
        createMenu();
    }

    private void createMenu() {
        window.setJMenuBar(createMenuBar());
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(provider.createFileMenu());
        menuBar.add(provider.createEditMenu());
        menuBar.add(provider.createViewMenu());
        return menuBar;
    }
}
