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
    private BufferedImage imageA;
    private BufferedImage imageB;
    private BufferedImage imageC;


    public Interface(){}

    public JFrame getMain(){
        mainFrame = new JFrame("Filter");
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setMinimumSize(new Dimension(1200,440));
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
            FileNameExtensionFilter jpegFilter = new FileNameExtensionFilter("JPEG(.jpeg)", "jpeg");
            fileChooser.setFileFilter(pngFilter);
            fileChooser.setFileFilter(jpgFilter);
            fileChooser.setFileFilter(jpegFilter);

            int res = fileChooser.showDialog(mainFrame,"Open");
            if (res == JFileChooser.APPROVE_OPTION) {
                zoneA.removeAll();
                File file = fileChooser.getSelectedFile();
                Image krot = new ImageIcon(file.getAbsolutePath()).getImage();
                if(krot.getWidth(null) > 350 || krot.getHeight(null) > 350){
                    krot = new ImageIcon(krot.getScaledInstance(348,348, BufferedImage.SCALE_DEFAULT)).getImage();
                }

                float dash1[] = {10.0f};
                BasicStroke basicStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f);

                imageA = new BufferedImage(350,350, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = imageA.createGraphics();
                g2.setColor(Color.WHITE);
                g2.setStroke(basicStroke);
                g2.drawRect(0,0,349,349);
                zoneA.add(new JLabel(new ImageIcon(imageA)));
                g2.drawImage(krot,1,1,null);
                g2.dispose();
                zoneA.revalidate();
            }
        });
        /*-------------------------------------------------------------*/
        return jToolBar;
    }

    public JPanel mainJPanel(){
        mainPanel = new JPanel(new GridLayout());

        float dash1[] = {10.0f};
        BasicStroke basicStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f);

        zoneA = new JPanel();
        zoneA.setBackground(Color.BLACK);
        imageA = new BufferedImage(350,350, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = imageA.createGraphics();
        g2.setColor(Color.WHITE);
        g2.setStroke(basicStroke);
        g2.drawRect(0,0,349,349);
        zoneA.add(new JLabel(new ImageIcon(imageA)));

        zoneB = new JPanel();
        zoneB.setBackground(Color.YELLOW);
        imageB = new BufferedImage(350,350, BufferedImage.TYPE_INT_ARGB);
        g2 = imageB.createGraphics();
        g2.setColor(Color.BLACK);
        g2.setStroke(basicStroke);
        g2.drawRect(0,0,349,349);
        zoneB.add(new JLabel(new ImageIcon(imageB)));

        zoneC = new JPanel();
        zoneC.setBackground(Color.RED);
        imageC = new BufferedImage(350,350, BufferedImage.TYPE_INT_ARGB);
        g2 = imageC.createGraphics();
        g2.setColor(Color.YELLOW);
        g2.setStroke(basicStroke);
        g2.drawRect(0,0,349,349);
        zoneC.add(new JLabel(new ImageIcon(imageC)));

        mainPanel.add(zoneA);
        mainPanel.add(zoneB);
        mainPanel.add(zoneC);

        return mainPanel;
    }
}
