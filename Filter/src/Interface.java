import filters.GaussBlur;
import filters.Negative;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;

class Interface {
    private final int NEGATIVE = 1;
    private final int GAUSSBLUR = 2;
    private JFrame mainFrame;
    private JPanel mainPanel;
//    private JPanel zoneA;
    private JLayeredPane zoneA;
    private JPanel zoneB;
    private JPanel zoneC;
    private JPanel zoneASelect;
    private BufferedImage bImage;
    private BufferedImage selectImage;
    private BufferedImage effectImage;
    private Boolean selectMode = false;
    private int cursorX;
    private int cursorY;
    private double selectXsize;
    private double selectYsize;
    private int totalSize = 100;

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
        JButton jButtonNew = new JButton(new ImageIcon("src/icons/new32.png"));
        jButtonNew.setToolTipText("New Document");
        /*-------------------------------------------------------------*/
        JButton jButtonSelect = new JButton(new ImageIcon("src/icons/select32.png"));
        jButtonSelect.setToolTipText("Select Zone");
        /*-------------------------------------------------------------*/
        JButton jButtonShow = new JButton(new ImageIcon("src/icons/show32.png"));
        jButtonShow.setToolTipText("Show");
        /*-------------------------------------------------------------*/
        JButton jButtonDraw = new JButton(new ImageIcon("src/icons/draw32.png"));
        jButtonDraw.setToolTipText("Draw");
        /*-------------------------------------------------------------*/
        JButton jButtonNegative = new JButton(new ImageIcon("src/icons/negative32.png"));
        jButtonNegative.setToolTipText("Negative");
        /*-------------------------------------------------------------*/
        JButton jButtonGaussBlur = new JButton(new ImageIcon("src/icons/gaussBlur32.png"));
        jButtonGaussBlur.setToolTipText("Gauss Blur");
        /*-------------------------------------------------------------*/
        JToolBar.Separator separator = new JToolBar.Separator(new Dimension(14,14));
        jToolBar.add(jButtonNew);
        jToolBar.add(jButtonSelect);
        jToolBar.add(jButtonShow);
        jToolBar.add(jButtonDraw);
        jToolBar.add(separator);
        jToolBar.add(jButtonNegative);
        jToolBar.add(jButtonGaussBlur);
        /*-------------------------------------------------------------*/
        jButtonNew.addActionListener(e ->loadImage());
        jButtonSelect.addActionListener(e ->{
//            JPanel selectPanel = new JPanel();
//            selectPanel.setBounds(50,50,50,50);
//            selectPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK,1));
//            selectPanel.setOpaque(false);
//            zoneA.add(selectPanel,JLayeredPane.DRAG_LAYER);
//            zoneA.repaint();
            if(bImage != null && !selectMode){
                cursorX = 100;
                cursorY = 100;

                selectMode = true;
                zoneASelect = new JPanel();
                zoneASelect.setPreferredSize(new Dimension(zoneA.getWidth(),zoneA.getHeight()));
                zoneASelect.setBounds(0,0,zoneA.getWidth(),zoneA.getHeight());
                zoneASelect.setOpaque(false);
//                zoneASelect.setBorder(BorderFactory.createLineBorder(Color.YELLOW,1));

                BufferedImage testImage = new BufferedImage(zoneASelect.getWidth(),zoneASelect.getHeight(),BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = testImage.createGraphics();
                g2.setColor(Color.RED);
                g2.drawRect(50,50,100,100);
                zoneASelect.add(new JLabel(new ImageIcon(testImage)));

                zoneA.add(zoneASelect,JLayeredPane.DRAG_LAYER);
                zoneA.repaint();

                drawSelect();
            }
        });
        jButtonShow.addActionListener(e ->{
            if(selectMode){
                zoneASelect.setVisible(false);
                selectMode = false;
                System.out.println("OFF");
            } else {
                zoneASelect.setVisible(true);
                selectMode = true;
                System.out.println("ON");
            }
        });
        jButtonDraw.addActionListener(e -> drawSelect());
        jButtonNegative.addActionListener(e -> makeEffect(NEGATIVE));
        jButtonGaussBlur.addActionListener(e -> makeEffect(GAUSSBLUR));
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

//        Image image = new ImageIcon("src/icons/krot.png").getImage();
//        image = new ImageIcon(image.getScaledInstance(348,348, BufferedImage.SCALE_DEFAULT)).getImage();
//        BufferedImage test = new BufferedImage(image.getWidth(null),image.getHeight(null),BufferedImage.TYPE_INT_ARGB);
//        Graphics2D g2 = test.createGraphics();
//        g2.drawImage(image,0,0,null);
//        g2.dispose();
//        JLabel tmp = new JLabel(new ImageIcon(test));
//        tmp.setBounds(1,1,image.getWidth(null), image.getHeight(null));
//        zoneB.add(tmp);
//        zoneB.repaint();

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

    private void loadImage(){
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
                if(image.getWidth(null) > 350){
                    selectXsize = image.getWidth(null)/350d;
                }
                if(image.getHeight(null) > 350){
                    selectYsize = image.getHeight(null)/350d;
                }
                if (image.getWidth(null) > image.getHeight(null)){
                    int ySize = (int)(image.getHeight(null)/selectXsize) > 348 ? 348 : (int)(image.getHeight(null)/selectXsize);
                    image = new ImageIcon(image.getScaledInstance(348,ySize, BufferedImage.SCALE_DEFAULT)).getImage();
                } else {
                    int xSize = (int)(image.getWidth(null)/selectYsize) > 348 ? 348 : (int)(image.getWidth(null)/selectYsize);
                    image = new ImageIcon(image.getScaledInstance(xSize,348, BufferedImage.SCALE_DEFAULT)).getImage();
                }
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
    }

    private void drawSelect(MouseEvent e){
        int x = e.getX();
        int y = e.getY();
        System.out.println("X = " + x + " Y = " + y);
        int xSize = (int)(350/selectXsize);
        int ySize = (int)(350/selectYsize);
        totalSize = xSize > ySize ? xSize : ySize;
        System.out.println("Total " + totalSize);
        System.out.println(bImage.getHeight());

        if(x > bImage.getWidth()-totalSize/2 - 2){
            x = bImage.getWidth()-totalSize/2 - 2;
        } else if(x < totalSize/2){
            x = totalSize/2;
        }

        if(y > bImage.getHeight()-totalSize/2){
            y = bImage.getHeight()-totalSize/2;
        } else if(y < totalSize/2){
            y = totalSize/2 -5;
        }

        cursorX = x;
        cursorY = y;

        if(selectMode){
            zoneASelect.removeAll();
            BufferedImage testImage = new BufferedImage(zoneASelect.getWidth(),zoneASelect.getHeight(),BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = testImage.createGraphics();
            g2.setColor(Color.RED);
            g2.drawRect(x-totalSize/2,y-totalSize/2,totalSize,totalSize);
            zoneASelect.add(new JLabel(new ImageIcon(testImage)));
            zoneASelect.revalidate();
        }
    }

    private void drawSelect(){
        if(selectImage != null){
            zoneB.removeAll();
        }

        selectImage = new BufferedImage(totalSize,totalSize,BufferedImage.TYPE_INT_ARGB);

        int xStart = cursorX - totalSize/2;
        int yStart = cursorY - totalSize/2;
        for(int y=0; y<selectImage.getHeight(); y++, yStart++){
//            System.out.println("Y:" + y);
            for(int x=0; x<selectImage.getWidth(); x++, xStart++){
                int color = bImage.getRGB(xStart,yStart);
                selectImage.setRGB(x,y,color);
//                System.out.println("X:" + x);
            }
            xStart = cursorX - totalSize/2;
        }

        Image image = new ImageIcon(selectImage).getImage();
        image = new ImageIcon(image.getScaledInstance(348,348, BufferedImage.SCALE_DEFAULT)).getImage();
        selectImage = new BufferedImage(image.getWidth(null),image.getHeight(null),BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = selectImage.createGraphics();
        g2.drawImage(image,0,0,null);
        g2.dispose();
        JLabel tmp = new JLabel(new ImageIcon(selectImage));
        tmp.setBounds(1,1,image.getWidth(null), image.getHeight(null));
        zoneB.add(tmp);
        zoneB.repaint();
    }

    private void makeEffect(int TYPE){
        if(selectImage != null){
            if(effectImage != null){
                zoneC.removeAll();
            }

            switch (TYPE){
                case NEGATIVE:
                    Negative negative = new Negative();
                    effectImage = negative.makeNegative(selectImage);
                    break;

                case GAUSSBLUR:
                    GaussBlur gaussBlur = new GaussBlur();
                    effectImage = gaussBlur.makeGaussBlur(selectImage);
                    break;
            }

            Image image = new ImageIcon(effectImage).getImage();
            image = new ImageIcon(image.getScaledInstance(348,348, BufferedImage.SCALE_DEFAULT)).getImage();
            effectImage = new BufferedImage(image.getWidth(null),image.getHeight(null),BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = effectImage.createGraphics();
            g2.drawImage(image,0,0,null);
            g2.dispose();
            JLabel tmp = new JLabel(new ImageIcon(effectImage));
            tmp.setBounds(1,1,image.getWidth(null), image.getHeight(null));
            zoneC.add(tmp);
            zoneC.repaint();
        }
    }

    class CustomMouseListener extends MouseAdapter{
        @Override
        public void mouseClicked(MouseEvent e){
            drawSelect(e);
            if(selectMode){
                drawSelect();
            }
        }

        @Override
        public void mouseDragged(MouseEvent e){
            drawSelect(e);
            if(selectMode){
                drawSelect();
            }
        }
    }
}
