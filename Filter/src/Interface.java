import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;

class Interface {
    private JFrame mainFrame;
    private JPanel mainPanel;
//    private JPanel zoneA;
    private JLayeredPane zoneA;
    private JPanel zoneB;
    private JPanel zoneC;
    private JPanel zoneASelect;
    private BufferedImage bImage;
    private Boolean switchMode = false;

    Interface(){}

    JFrame getMain(){
        mainFrame = new JFrame("Filter");
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setMinimumSize(new Dimension(1100,440));
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setResizable(false);
        return  mainFrame;
    }

    JToolBar addToolBar(){
        JToolBar jToolBar = new JToolBar();
        jToolBar.setFloatable(false);
        /*-------------------------------------------------------------*/
        JButton jButtonNew = new JButton(new ImageIcon("src/icons/open32.png"));
        jButtonNew.setToolTipText("New Document");
        /*-------------------------------------------------------------*/
        JButton jButtonSelect = new JButton(new ImageIcon("src/icons/select32.png"));
        jButtonSelect.setToolTipText("Select Zone");
        /*-------------------------------------------------------------*/
        JButton jButtonSwitch = new JButton(new ImageIcon("src/icons/switch32.png"));
        jButtonSwitch.setToolTipText("Switch");
        /*-------------------------------------------------------------*/
        jToolBar.add(jButtonNew);
        jToolBar.add(jButtonSelect);
        jToolBar.add(jButtonSwitch);
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

                bImage = new BufferedImage(image.getWidth(null),image.getHeight(null),BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = bImage.createGraphics();
                g2.drawImage(image,0,0,null);
                g2.dispose();

//                JLabel tmp = new JLabel(new ImageIcon(image));
                JLabel tmp = new JLabel(new ImageIcon(bImage));
                tmp.setBounds(1,1,image.getWidth(null), image.getHeight(null));
                zoneA.add(tmp,JLayeredPane.DEFAULT_LAYER);
                zoneA.repaint();
            }
        });
        jButtonSelect.addActionListener(e ->{
//            JPanel selectPanel = new JPanel();
//            selectPanel.setBounds(50,50,50,50);
//            selectPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK,1));
//            selectPanel.setOpaque(false);
//            zoneA.add(selectPanel,JLayeredPane.DRAG_LAYER);
//            zoneA.repaint();
            if(bImage != null && !switchMode){
                switchMode = true;
                zoneASelect = new JPanel();
                zoneASelect.setBounds(1,1,zoneA.getWidth(),zoneA.getHeight());
                zoneASelect.setOpaque(false);

                BufferedImage testImage = new BufferedImage(zoneASelect.getWidth()-50,zoneASelect.getHeight()-50,BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = testImage.createGraphics();
                g2.setColor(Color.RED);
                g2.drawRect(50,50,100,100);
                zoneASelect.add(new JLabel(new ImageIcon(testImage)));

                zoneA.add(zoneASelect,JLayeredPane.DRAG_LAYER);
                zoneA.repaint();
            }
        });
        jButtonSwitch.addActionListener(e ->{
            if(switchMode){
                zoneASelect.setVisible(false);
                switchMode = false;
                System.out.println("OFF");
            } else {
                zoneASelect.setVisible(true);
                switchMode = true;
                System.out.println("ON");
            }
        });
        /*-------------------------------------------------------------*/
        return jToolBar;
    }

    JPanel mainJPanel(){
        mainPanel = new JPanel();
        Container pane = new Container();
        pane.setLayout(new BoxLayout(pane, BoxLayout.X_AXIS));

//        zoneA = new JPanel();
        zoneA = new JLayeredPane();
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

        CustomMouseListener cms = new CustomMouseListener();
        zoneA.addMouseListener(cms);
        zoneA.addMouseMotionListener(cms);

        mainPanel.add(zoneA,pane);
        mainPanel.add(zoneB,pane);
        mainPanel.add(zoneC,pane);

        return mainPanel;
    }

    private void drawSelect(MouseEvent e){
        int x = e.getX();
        int y = e.getY();

        if(x > 300){
            x = 300;
        } else if(x < 50){
            x = 50;
        }

        if(y > 300){
            y = 300;
        } else if(y < 50){
            y = 50;
        }

        if(switchMode){
            zoneASelect.removeAll();
            BufferedImage testImage = new BufferedImage(zoneASelect.getWidth(),zoneASelect.getHeight(),BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = testImage.createGraphics();
            g2.setColor(Color.RED);
            g2.drawRect(x-50,y-50,100,100);
            zoneASelect.add(new JLabel(new ImageIcon(testImage)));
            zoneASelect.revalidate();
        }
    }

    class CustomMouseListener extends MouseAdapter{
        @Override
        public void mouseClicked(MouseEvent e){
            drawSelect(e);
        }

        @Override
        public void mouseDragged(MouseEvent e){
            drawSelect(e);
        }
    }
}
