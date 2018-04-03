import javax.swing.*;
import java.awt.*;

class MainWindow {
    static JFrame mainFrame;
    MainWindow(){
        mainFrame = new JFrame("Isoline");
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setSize(new Dimension(840,680));
        mainFrame.setResizable(false);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        mainFrame.add(Toolbar.Toolbar(),BorderLayout.NORTH);
        mainFrame.add(Panels.Panel(),BorderLayout.CENTER);
        mainFrame.setVisible(true);
    }
}
