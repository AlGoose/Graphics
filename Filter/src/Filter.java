import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Filter {
    private JFrame mainFrame;
    private Interface anInterface;

    private Filter() throws IOException {
        anInterface = new Interface();
        mainFrame = anInterface.getMain();
        mainFrame.add(anInterface.addToolBar(), BorderLayout.NORTH);
        mainFrame.add(anInterface.mainJPanel());
        mainFrame.setVisible(true);
    }

    public static void main(String[] args) throws IOException {
        new Filter();
    }
}
