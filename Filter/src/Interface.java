import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Interface {
    private JFrame mainFrame;
    private JPanel mainPanel;
    private JPanel zoneA;
    private JPanel zoneB;
    private JPanel zoneC;


    public Interface(){}

    public JFrame getMain(){
        mainFrame = new JFrame("Filter");
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setMinimumSize(new Dimension(1100,440));
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setResizable(false);
        return  mainFrame;
    }

    public JToolBar addToolBar(){
        JToolBar jToolBar = new JToolBar();
        jToolBar.setFloatable(false);
        /*-------------------------------------------------------------*/
        JButton jButtonNew = new JButton(new ImageIcon("src/icons/new32.png"));
        jButtonNew.setToolTipText("New Document");
        /*-------------------------------------------------------------*/
        JButton jButtonSelect = new JButton(new ImageIcon("src/icons/select32.png"));
        jButtonSelect.setToolTipText("Select Document");
        /*-------------------------------------------------------------*/
        jToolBar.add(jButtonNew);
        jToolBar.add(jButtonSelect);
        /*-------------------------------------------------------------*/
        jButtonNew.addActionListener(e ->{
            JFileChooser fileChooser = new JFileChooser("src/icons");
            FileNameExtensionFilter pngFilter = new FileNameExtensionFilter("PNG(.png)", "png");
            FileNameExtensionFilter jpgFilter = new FileNameExtensionFilter("JPG(.jpg)", "jpg");
            fileChooser.setFileFilter(pngFilter);
            fileChooser.setFileFilter(jpgFilter);

            int res = fileChooser.showDialog(mainFrame,"Open");
            if (res == JFileChooser.APPROVE_OPTION) {
                zoneA.removeAll();
                File file = fileChooser.getSelectedFile();
                Image image = new ImageIcon(file.getAbsolutePath()).getImage();

                if(image.getWidth(null) > 350 || image.getHeight(null) > 350){
                    image = new ImageIcon(image.getScaledInstance(348,348, BufferedImage.SCALE_DEFAULT)).getImage();
                }

                JLabel tmp = new JLabel(new ImageIcon(image));
                tmp.setBounds(1,1,image.getWidth(null), image.getHeight(null));
                zoneA.add(tmp);
                zoneA.repaint();
            }
        });
        /*-------------------------------------------------------------*/
        return jToolBar;
    }

    public JPanel mainJPanel(){
        mainPanel = new JPanel();
        Container pane = new Container();
        pane.setLayout(new BoxLayout(pane, BoxLayout.X_AXIS));

        zoneA = new JPanel();
        zoneA.setLayout(null);
        zoneA.setPreferredSize(new Dimension(350,350));
        zoneA.setBorder(BorderFactory.createDashedBorder(Color.BLACK,1,6,3,true));

        zoneB = new JPanel();
        zoneB.setLayout(null);
        zoneB.setPreferredSize(new Dimension(350,350));
        zoneB.setBorder(BorderFactory.createDashedBorder(Color.BLACK,1,6,3,true));

        zoneC = new JPanel();
        zoneC.setLayout(null);
        zoneC.setPreferredSize(new Dimension(350,350));
        zoneC.setBorder(BorderFactory.createDashedBorder(Color.BLACK,1,6,3,true));

        mainPanel.add(zoneA,pane);
        mainPanel.add(zoneB,pane);
        mainPanel.add(zoneC,pane);

        return mainPanel;
    }
}
