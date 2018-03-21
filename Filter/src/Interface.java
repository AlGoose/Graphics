import filters.*;

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
    private final int GREYSHADES = 3;
    private final int ROBERTCROSS = 4;
    private final int DOUBLESIZE = 5;
    private final int SHARPNESS = 6;
    private final int GAMMA = 7;
    private JFrame mainFrame;
    private JPanel mainPanel;
    private JLayeredPane zoneA;
    private JPanel zoneB;
    private JPanel zoneC;
    private JPanel zoneASelect;
    private Image damnImage;
    private BufferedImage bImage;
    private BufferedImage selectImage;
    private BufferedImage effectImage;
    private Boolean selectMode = false;
    private int cursorX;
    private int cursorY;
    private double selectXsize;
    private double selectYsize;
    private int totalSize = 100;

    Interface() {
    }

    JFrame getMain() {
        mainFrame = new JFrame("Filter");
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setMinimumSize(new Dimension(1100, 440));
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setResizable(false);
        return mainFrame;
    }

    JToolBar addToolBar() {
        JToolBar jToolBar = new JToolBar();
        jToolBar.setFloatable(false);
        /*-------------------------------------------------------------*/
        JButton jButtonNew = new JButton(new ImageIcon("src/icons/new32.png"));
        jButtonNew.setToolTipText("New Document");
        /*-------------------------------------------------------------*/
        JButton jButtonClear = new JButton(new ImageIcon("src/icons/clear32.png"));
        jButtonClear.setToolTipText("Clear");
        /*-------------------------------------------------------------*/
        JButton jButtonSelect = new JButton(new ImageIcon("src/icons/select32.png"));
        jButtonSelect.setToolTipText("Select Zone");
        /*-------------------------------------------------------------*/
        JButton jButtonCopy = new JButton(new ImageIcon("src/icons/copy32.png"));
        jButtonCopy.setToolTipText("Copy C to B");
        /*-------------------------------------------------------------*/
        JButton jButtonNegative = new JButton(new ImageIcon("src/icons/negative32.png"));
        jButtonNegative.setToolTipText("Negative");
        /*-------------------------------------------------------------*/
        JButton jButtonGaussBlur = new JButton(new ImageIcon("src/icons/gaussBlur32.png"));
        jButtonGaussBlur.setToolTipText("Gauss Blur");
        /*-------------------------------------------------------------*/
        JButton jButtonGreyShades = new JButton(new ImageIcon("src/icons/greyShades32.png"));
        jButtonGreyShades.setToolTipText("Grey");
        /*-------------------------------------------------------------*/
        JButton jButtonRobertCross = new JButton(new ImageIcon("src/icons/robertCross32.png"));
        jButtonRobertCross.setToolTipText("Robert Cross");
        /*-------------------------------------------------------------*/
        JButton jButtonDoubleSize = new JButton(new ImageIcon("src/icons/doubleSize32.png"));
        jButtonDoubleSize.setToolTipText("Double Size");
        /*-------------------------------------------------------------*/
        JButton jButtonSharpness = new JButton(new ImageIcon("src/icons/sharpness32.png"));
        jButtonSharpness.setToolTipText("Sharpness");
        /*-------------------------------------------------------------*/
        JButton jButtonGamma = new JButton(new ImageIcon("src/icons/gamma32.png"));
        jButtonGamma.setToolTipText("Gamma");
        /*-------------------------------------------------------------*/
        JToolBar.Separator separator = new JToolBar.Separator(new Dimension(14, 14));
        jToolBar.add(jButtonNew);
        jToolBar.add(jButtonClear);
        jToolBar.add(jButtonSelect);
        jToolBar.add(jButtonCopy);
        jToolBar.add(separator);
        jToolBar.add(jButtonNegative);
        jToolBar.add(jButtonGaussBlur);
        jToolBar.add(jButtonGreyShades);
        jToolBar.add(jButtonRobertCross);
        jToolBar.add(jButtonDoubleSize);
        jToolBar.add(jButtonSharpness);
        jToolBar.add(jButtonGamma);
        /*-------------------------------------------------------------*/
        jButtonNew.addActionListener(e -> loadImage());
        jButtonClear.addActionListener(e ->{
            if(selectImage != null){
                if(effectImage != null){
                    zoneC.removeAll();
                    zoneC.repaint();
                }
                zoneB.removeAll();
                zoneB.repaint();

                zoneA.remove(zoneASelect);
                zoneA.repaint();

                selectMode = false;
            }
        });
        jButtonSelect.addActionListener(e -> {
            if (bImage != null){
                if(!selectMode){
                    selectMode = true;
                    zoneASelect = new JPanel(new BorderLayout());
                    zoneASelect.setPreferredSize(new Dimension(zoneA.getWidth(), zoneA.getHeight()));
                    zoneASelect.setBounds(0, 0, zoneA.getWidth(), zoneA.getHeight());
                    zoneASelect.setOpaque(false);
                    zoneA.add(zoneASelect, JLayeredPane.DRAG_LAYER);
                } else {
                    selectMode = false;
                    zoneA.remove(zoneASelect);
                    zoneA.repaint();
                }
            }
        });
        jButtonCopy.addActionListener(e -> {
            if(effectImage != null){
                zoneB.removeAll();
                JLabel tmp = new JLabel(new ImageIcon(damnImage));
                tmp.setBounds(1, 1, damnImage.getWidth(null), damnImage.getHeight(null));
                zoneB.add(tmp);
                zoneB.repaint();
            }
        });
        jButtonNegative.addActionListener(e -> makeEffect(NEGATIVE));
        jButtonGaussBlur.addActionListener(e -> makeEffect(GAUSSBLUR));
        jButtonGreyShades.addActionListener(e -> makeEffect(GREYSHADES));
        jButtonRobertCross.addActionListener(e -> makeEffect(ROBERTCROSS));
        jButtonDoubleSize.addActionListener(e -> makeEffect(DOUBLESIZE));
        jButtonSharpness.addActionListener(e -> makeEffect(SHARPNESS));
        jButtonGamma.addActionListener(e -> makeEffect(GAMMA));
        /*-------------------------------------------------------------*/
        return jToolBar;
    }

    JPanel mainJPanel() {
        mainPanel = new JPanel();
        Container pane = new Container();
        pane.setLayout(new BoxLayout(pane, BoxLayout.X_AXIS));

//        zoneA = new JPanel();
        zoneA = new JLayeredPane();
        zoneA.setPreferredSize(new Dimension(350, 350));
        zoneA.setBorder(BorderFactory.createDashedBorder(Color.BLACK, 1, 6, 3, true));

        zoneB = new JPanel();
        zoneB.setLayout(null);
        zoneB.setPreferredSize(new Dimension(350, 350));
        zoneB.setBorder(BorderFactory.createDashedBorder(Color.BLACK, 1, 6, 3, true));

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
        zoneC.setPreferredSize(new Dimension(350, 350));
        zoneC.setBorder(BorderFactory.createDashedBorder(Color.BLACK, 1, 6, 3, true));

        CustomMouseListener cms = new CustomMouseListener();
        zoneA.addMouseListener(cms);
        zoneA.addMouseMotionListener(cms);

        mainPanel.add(zoneA, pane);
        mainPanel.add(zoneB, pane);
        mainPanel.add(zoneC, pane);

        return mainPanel;
    }

    private void loadImage() {
        selectMode = false;
        JFileChooser fileChooser = new JFileChooser("src/icons");
        FileNameExtensionFilter pngFilter = new FileNameExtensionFilter("PNG(.png)", "png");
        FileNameExtensionFilter jpgFilter = new FileNameExtensionFilter("JPG(.jpg)", "jpg");
        fileChooser.setFileFilter(pngFilter);
        fileChooser.setFileFilter(jpgFilter);

        int res = fileChooser.showDialog(mainFrame, "Open");
        if (res == JFileChooser.APPROVE_OPTION) {
            zoneA.removeAll();
            File file = fileChooser.getSelectedFile();
            Image image = new ImageIcon(file.getAbsolutePath()).getImage();

            if (image.getWidth(null) > 350 || image.getHeight(null) > 350) {
                if (image.getWidth(null) > 350) {
                    selectXsize = image.getWidth(null) / 350d;
                }
                if (image.getHeight(null) > 350) {
                    selectYsize = image.getHeight(null) / 350d;
                }
                if (image.getWidth(null) > image.getHeight(null)) {
                    int ySize = (int) (image.getHeight(null) / selectXsize) > 348 ? 348 : (int) (image.getHeight(null) / selectXsize);
                    image = new ImageIcon(image.getScaledInstance(348, ySize, BufferedImage.SCALE_DEFAULT)).getImage();
                } else {
                    int xSize = (int) (image.getWidth(null) / selectYsize) > 348 ? 348 : (int) (image.getWidth(null) / selectYsize);
                    image = new ImageIcon(image.getScaledInstance(xSize, 348, BufferedImage.SCALE_DEFAULT)).getImage();
                }
            }

            bImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = bImage.createGraphics();
            g2.drawImage(image, 0, 0, null);
            g2.dispose();

            JLabel tmp = new JLabel(new ImageIcon(bImage));
            tmp.setBounds(1, 1, image.getWidth(null), image.getHeight(null));
            zoneA.add(tmp, JLayeredPane.DEFAULT_LAYER);
            zoneA.repaint();
        }
    }

    private void drawSelect(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        int xSize = (int) (350 / selectXsize);
        int ySize = (int) (350 / selectYsize);
        totalSize = xSize > ySize ? xSize : ySize;

        int x0 = x - totalSize/2;
        int y0 = y - totalSize/2;
        if(x0 < 0){
            x0 = 1;
        }
        if(y0 < 0){
            y0 = 1;
        }
        if(x0 + totalSize >= bImage.getWidth()){
            x0 = bImage.getWidth() - totalSize;
        }
        if(y0 + totalSize >= bImage.getHeight()){
            y0 = bImage.getHeight() - totalSize;
        }

        cursorX = x;
        cursorY = y;
        if (selectMode) {
            zoneASelect.removeAll();
            BufferedImage testImage = new BufferedImage(zoneASelect.getWidth(), zoneASelect.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = testImage.createGraphics();
            g2.setColor(Color.RED);
            g2.drawRect(x0,y0, totalSize, totalSize);
            zoneASelect.add(new JLabel(new ImageIcon(testImage)));
            zoneASelect.revalidate();
        }
    }

    private void drawSelect() {
        if (selectImage != null) {
            zoneB.removeAll();
        }

        selectImage = new BufferedImage(totalSize, totalSize, BufferedImage.TYPE_INT_ARGB);

        int x0 = cursorX - totalSize / 2;
        int y0 = cursorY - totalSize / 2;

        if(x0 < 0){
            x0 = 1;
        }
        if(y0 < 0){
            y0 = 1;
        }
        if(x0 + totalSize >= bImage.getWidth()){
            x0 = bImage.getWidth() - totalSize;
        }
        if(y0 + totalSize >= bImage.getHeight()){
            y0 = bImage.getHeight() - totalSize;
        }
        int temp = x0;
        for (int y = 0; y < selectImage.getHeight(); y++, y0++) {
            for (int x = 0; x < selectImage.getWidth(); x++, x0++) {
                int color = bImage.getRGB(x0,y0);
                selectImage.setRGB(x, y, color);
            }
            x0 = temp;
        }

        Image image = new ImageIcon(selectImage).getImage();
        image = new ImageIcon(image.getScaledInstance(348, 348, BufferedImage.SCALE_DEFAULT)).getImage();
        selectImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = selectImage.createGraphics();
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
        JLabel tmp = new JLabel(new ImageIcon(selectImage));
        tmp.setBounds(1, 1, image.getWidth(null), image.getHeight(null));
        zoneB.add(tmp);
        zoneB.repaint();
    }

    private void makeEffect(int TYPE) {
        if (selectImage != null) {
            if (effectImage != null) {
                zoneC.removeAll();
                zoneC.repaint();
            }

            switch (TYPE) {
                case NEGATIVE:
                    Negative negative = new Negative();
                    effectImage = negative.makeNegative(selectImage);
                    break;

                case GAUSSBLUR:
                    GaussBlur gaussBlur = new GaussBlur();
                    effectImage = gaussBlur.makeGaussBlur(selectImage);
                    break;

                case GREYSHADES:
                    GreyShades greyShades = new GreyShades();
                    effectImage = greyShades.makeGreyShades(selectImage);
                    break;

                case ROBERTCROSS:
                    RobertCross robertCross = new RobertCross();
                    effectImage = robertCross.makeRobertCross(selectImage);
                    break;

                case DOUBLESIZE:
                    DoubleSize doubleSize = new DoubleSize();
                    effectImage = doubleSize.makeDoubleSize(selectImage);
                    break;

                case SHARPNESS:
                    Sharpness sharpness = new Sharpness();
                    effectImage = sharpness.makeSharpness(selectImage);
                    break;

                case GAMMA:
                    Gamma gamma = new Gamma();
                    effectImage = gamma.makeGamma(selectImage);
                    break;
            }

            damnImage = new ImageIcon(effectImage).getImage();
            damnImage = new ImageIcon(damnImage.getScaledInstance(348, 348, BufferedImage.SCALE_DEFAULT)).getImage();
//            effectImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
//            Graphics2D g2 = effectImage.createGraphics();
//            g2.drawImage(image, 0, 0, null);
//            g2.dispose();
            JLabel tmp = new JLabel(new ImageIcon(damnImage));
            tmp.setBounds(1, 1, damnImage.getWidth(null), damnImage.getHeight(null));
            zoneC.add(tmp);
            zoneC.repaint();
        }
    }

    class CustomMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            if(bImage != null){
                drawSelect(e);
                if (selectMode){
                    drawSelect();
                }
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if(bImage != null){
                drawSelect(e);
                if (selectMode){
                    drawSelect();
                }
            }
        }
    }
}
