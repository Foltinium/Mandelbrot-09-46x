import ru.gr0946x.ui.MainWindow;
import ru.gr0946x.ui.julia.JuliaSetController;

void main() {
    MainWindow mainWindow = new MainWindow();
    JuliaSetController.attachTo(mainWindow);
    mainWindow.setVisible(true);
}