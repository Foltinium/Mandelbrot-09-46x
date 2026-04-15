package ru.gr0946x.ui.io;

import javax.swing.*;

public interface MenuProvider {
    JMenu createFileMenu();
    JMenu createEditMenu();
    JMenu createViewMenu();
}
