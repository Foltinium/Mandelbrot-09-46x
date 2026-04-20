package ru.gr0946x.ui.io;

import ru.gr0946x.ui.fractals.ColorFunction;
import ru.gr0946x.ui.fractals.Fractal;

public interface FractalMenuCallback {
    void saveFractal();
    void openFile();
    void openTourWindow();
    void triggerUndo();
    boolean canUndo();
    void setAdaptiveIterationsEnabled(boolean enabled);
    void setCurrentFractal(Fractal fractal);
    void setCurrentColorFunction(ColorFunction colorFunction);
}
