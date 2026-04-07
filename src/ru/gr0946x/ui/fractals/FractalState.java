package ru.gr0946x.ui.fractals;
import java.io.Serializable;

public record FractalState(
        double xMin,
        double xMax,
        double yMin,
        double yMax,
        int maxIterations
) implements Serializable {

}