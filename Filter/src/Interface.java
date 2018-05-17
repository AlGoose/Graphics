import filters.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

class Interface {
    private final int NEGATIVE = 1;
    private final int GAUSSBLUR = 2;
    private final int GREYSHADES = 3;
    private final int ROBERTCROSS = 4;
    private final int DOUBLESIZE = 5;
    private final int SHARPNESS = 6;
    private final int GAMMA = 7;
    private final int AQUA = 8;
    private final int METAL = 9;
    private final int FLOYD = 10;
    private final int ORDER = 11;
    private final int ROTATE = 12;
    private final int SOBEL = 13;
    private JFrame mainFrame;
    private JPanel mainPanel;
    private JLayeredPane zoneA;
    private JPanel zoneB;
    private JPanel zoneC;
    private JPanel zoneASelect;
    private Image damnImage;
    private Image picture;
    private BufferedImage bImage;
    private BufferedImage selectImage;
    private BufferedImage effectImage;
    private Boolean selectMode = false;
    private int cursorX;
    private int cursorY;
    private double selectXsize;
    private double selectYsize;
    private int totalSize = 100;
    private double gammaParameter = 1.0;
    private int redParameter = 2;
    private int greenParameter = 2;
    private int blueParameter = 2;
    private int rotateParameter = 0;
    private int crossParameter = 10;
    private int sobelParameter = 10;

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
        JButton jButtonSave = new JButton(new ImageIcon("src/icons/save32.png"));
        jButtonSave.setToolTipText("Save");
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
        JButton jButtonSobel = new JButton(new ImageIcon("src/icons/sobel32.png"));
        jButtonSobel.setToolTipText("Sobel");
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
        JButton jButtonAqua = new JButton(new ImageIcon("src/icons/aqua32.png"));
        jButtonAqua.setToolTipText("Aqua");
        /*-------------------------------------------------------------*/
        JButton jButtonMetal = new JButton(new ImageIcon("src/icons/metal32.png"));
        jButtonMetal.setToolTipText("Metal");
        /*-------------------------------------------------------------*/
        JButton jButtonFloyd = new JButton(new ImageIcon("src/icons/floyd32.png"));
        jButtonFloyd.setToolTipText("Floyd-Steinberg");
        /*-------------------------------------------------------------*/
        JButton jButtonOrder = new JButton(new ImageIcon("src/icons/order32.png"));
        jButtonOrder.setToolTipText("Order Dithering");
        /*-------------------------------------------------------------*/
        JButton jButtonRotate = new JButton(new ImageIcon("src/icons/rotate32.png"));
        jButtonRotate.setToolTipText("Rotate");
        /*-------------------------------------------------------------*/
        JToolBar.Separator separator = new JToolBar.Separator(new Dimension(14, 14));
        jToolBar.add(jButtonNew);
        jToolBar.add(jButtonClear);
        jToolBar.add(jButtonSelect);
        jToolBar.add(jButtonCopy);
        jToolBar.add(jButtonSave);
        jToolBar.add(separator);
        jToolBar.add(jButtonNegative);
        jToolBar.add(jButtonGaussBlur);
        jToolBar.add(jButtonGreyShades);
        jToolBar.add(jButtonRobertCross);
        jToolBar.add(jButtonSobel);
        jToolBar.add(jButtonDoubleSize);
        jToolBar.add(jButtonRotate);
        jToolBar.add(jButtonSharpness);
        jToolBar.add(jButtonGamma);
        jToolBar.add(jButtonAqua);
        jToolBar.add(jButtonMetal);
        jToolBar.add(jButtonFloyd);
        jToolBar.add(jButtonOrder);
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
//                JLabel tmp = new JLabel(new ImageIcon(damnImage));
//                tmp.setBounds(1, 1, damnImage.getWidth(null), damnImage.getHeight(null));
//                zoneB.add(tmp);
//                zoneB.repaint();

                selectImage = new BufferedImage(damnImage.getWidth(null),damnImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);
                Graphics g = selectImage.getGraphics();
                g.drawImage(damnImage,0,0,null);
                g.dispose();

                JLabel tmp = new JLabel(new ImageIcon(selectImage));
                tmp.setBounds(1, 1, selectImage.getWidth(null), selectImage.getHeight(null));
                zoneB.add(tmp, JLayeredPane.DEFAULT_LAYER);
                zoneB.repaint();
            }
        });
        jButtonNegative.addActionListener(e -> makeEffect(NEGATIVE));
        jButtonGaussBlur.addActionListener(e -> makeEffect(GAUSSBLUR));
        jButtonGreyShades.addActionListener(e -> makeEffect(GREYSHADES));
        jButtonRobertCross.addActionListener(e -> crossDialog());
        jButtonSobel.addActionListener(e -> sobelDialog());
        jButtonDoubleSize.addActionListener(e -> makeEffect(DOUBLESIZE));
        jButtonSharpness.addActionListener(e -> makeEffect(SHARPNESS));
        jButtonGamma.addActionListener(e -> {
            if(selectImage != null){
                makeEffect(GAMMA);
                gammaDialog();
            }
        });
        jButtonAqua.addActionListener(e -> makeEffect(AQUA));
        jButtonMetal.addActionListener(e -> makeEffect(METAL));
        jButtonFloyd.addActionListener(e -> {
            if(selectImage != null){
                makeEffect(FLOYD);
                floydDialog();
            }
        });
        jButtonOrder.addActionListener(e -> makeEffect(ORDER));
        jButtonRotate.addActionListener(e -> {
            if(selectImage != null){
                makeEffect(ROTATE);
                rotateDialog();
            }
        });
        jButtonSave.addActionListener(e -> saveDialog());
        /*-------------------------------------------------------------*/
        return jToolBar;
    }

    JPanel mainJPanel() {
        mainPanel = new JPanel();
        Container pane = new Container();
        pane.setLayout(new BoxLayout(pane, BoxLayout.X_AXIS));

        zoneA = new JLayeredPane();
        zoneA.setPreferredSize(new Dimension(350, 350));
        zoneA.setBorder(BorderFactory.createDashedBorder(Color.BLACK, 1, 6, 3, true));

        zoneB = new JPanel();
        zoneB.setLayout(null);
        zoneB.setPreferredSize(new Dimension(350, 350));
        zoneB.setBorder(BorderFactory.createDashedBorder(Color.BLACK, 1, 6, 3, true));

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

    private void gammaDialog(){
        JDialog dialog = new JDialog(mainFrame,"Gamma Parameter",true);
        dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        dialog.setSize(300,100);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(mainFrame);
        /*-------------------------------------------------------------*/
        JPanel mainPanel = new JPanel(new GridLayout(1,2));
        JPanel panelOne = new JPanel(new GridLayout(2,1));
        JPanel panelTwo = new JPanel(new GridLayout(2,1));
        /*-------------------------------------------------------------*/
        JLabel gammaField = new JLabel(Double.toString(gammaParameter),SwingConstants.CENTER);
        JSlider jSliderGamma = new JSlider(JSlider.HORIZONTAL,1,10,(int)gammaParameter);

        panelOne.add(gammaField);
        panelTwo.add(jSliderGamma);
        /*-------------------------------------------------------------*/
        JButton applyButton = new JButton("Apply");
        JButton cancelButton = new JButton("Cancel");
        panelOne.add(applyButton);
        panelTwo.add(cancelButton);
        /*-------------------------------------------------------------*/
        jSliderGamma.addChangeListener(e -> {
            gammaField.setText(((Integer)((JSlider)e.getSource()).getValue()).toString());
            gammaParameter = Double.parseDouble(gammaField.getText());
            makeEffect(GAMMA);
        });
        /*-------------------------------------------------------------*/
        applyButton.addActionListener(e -> dialog.dispose());
        cancelButton.addActionListener(e -> {
            gammaParameter = 1;
            makeEffect(GAMMA);
            dialog.dispose();
        });
        /*-------------------------------------------------------------*/
        mainPanel.add(panelOne);
        mainPanel.add(panelTwo);
        dialog.add(mainPanel);
        dialog.setVisible(true);
    }

    private void floydDialog(){
        JDialog dialog = new JDialog(mainFrame,"Floyd Steinberg Parameter",true);
        dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        dialog.setSize(300,300);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(mainFrame);
        /*-------------------------------------------------------------*/
        JPanel mainPanel = new JPanel(new GridLayout(1,2));
        JPanel panelOne = new JPanel(new GridLayout(4,1));
        JPanel panelTwo = new JPanel(new GridLayout(4,1));
        /*-------------------------------------------------------------*/
        JLabel redField = new JLabel(Integer.toString(redParameter),SwingConstants.CENTER);
        JSlider jSliderRed = new JSlider(JSlider.HORIZONTAL,2,255,redParameter);

        panelOne.add(redField);
        panelTwo.add(jSliderRed);
        /*-------------------------------------------------------------*/
        JLabel greenField = new JLabel(Integer.toString(greenParameter),SwingConstants.CENTER);
        JSlider jSliderGreen = new JSlider(JSlider.HORIZONTAL,2,255,greenParameter);

        panelOne.add(greenField);
        panelTwo.add(jSliderGreen);
        /*-------------------------------------------------------------*/
        JLabel blueField = new JLabel(Integer.toString(blueParameter),SwingConstants.CENTER);
        JSlider jSliderBlue = new JSlider(JSlider.HORIZONTAL,2,255,blueParameter);

        panelOne.add(blueField);
        panelTwo.add(jSliderBlue);
        /*-------------------------------------------------------------*/
        JButton applyButton = new JButton("Apply");
        JButton cancelButton = new JButton("Cancel");
        panelOne.add(applyButton);
        panelTwo.add(cancelButton);
        /*-------------------------------------------------------------*/
        jSliderRed.addChangeListener(e -> {
            redField.setText(((Integer)((JSlider)e.getSource()).getValue()).toString());
            redParameter = Integer.parseInt(redField.getText());
            makeEffect(FLOYD);
        });
        /*-------------------------------------------------------------*/
        jSliderGreen.addChangeListener(e -> {
            greenField.setText(((Integer)((JSlider)e.getSource()).getValue()).toString());
            greenParameter = Integer.parseInt(greenField.getText());
            makeEffect(FLOYD);
        });
        /*-------------------------------------------------------------*/
        jSliderBlue.addChangeListener(e -> {
            blueField.setText(((Integer)((JSlider)e.getSource()).getValue()).toString());
            blueParameter = Integer.parseInt(blueField.getText());
            makeEffect(FLOYD);
        });
        /*-------------------------------------------------------------*/
        applyButton.addActionListener(e -> dialog.dispose());
        cancelButton.addActionListener(e -> {
            redParameter = 2;
            greenParameter = 2;
            blueParameter = 2;
            makeEffect(FLOYD);
            dialog.dispose();
        });
        /*-------------------------------------------------------------*/
        mainPanel.add(panelOne);
        mainPanel.add(panelTwo);
        dialog.add(mainPanel);
        dialog.setVisible(true);
    }

    private void rotateDialog(){
        JDialog dialog = new JDialog(mainFrame,"Rotate",true);
        dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        dialog.setSize(300,100);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(mainFrame);
        /*-------------------------------------------------------------*/
        JPanel mainPanel = new JPanel(new GridLayout(1,2));
        JPanel panelOne = new JPanel(new GridLayout(2,1));
        JPanel panelTwo = new JPanel(new GridLayout(2,1));
        /*-------------------------------------------------------------*/
        JLabel rotateField = new JLabel(Integer.toString(rotateParameter),SwingConstants.CENTER);
        JSlider jSliderRotate = new JSlider(JSlider.HORIZONTAL,0,360,rotateParameter);

        panelOne.add(rotateField);
        panelTwo.add(jSliderRotate);
        /*-------------------------------------------------------------*/
        JButton applyButton = new JButton("Apply");
        JButton cancelButton = new JButton("Cancel");
        panelOne.add(applyButton);
        panelTwo.add(cancelButton);
        /*-------------------------------------------------------------*/
        jSliderRotate.addChangeListener(e -> {
            rotateField.setText(((Integer)((JSlider)e.getSource()).getValue()).toString());
            rotateParameter = Integer.parseInt(rotateField.getText());
            makeEffect(ROTATE);
        });
        /*-------------------------------------------------------------*/
        applyButton.addActionListener(e -> dialog.dispose());
        cancelButton.addActionListener(e -> {
            rotateParameter = 0;
            makeEffect(GAMMA);
            dialog.dispose();
        });
        /*-------------------------------------------------------------*/
        mainPanel.add(panelOne);
        mainPanel.add(panelTwo);
        dialog.add(mainPanel);
        dialog.setVisible(true);
    }

    private void crossDialog(){
        JDialog dialog = new JDialog(mainFrame,"Cross Parameter",true);
        dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        dialog.setSize(300,100);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(mainFrame);
        /*-------------------------------------------------------------*/
        JPanel mainPanel = new JPanel(new GridLayout(1,2));
        JPanel panelOne = new JPanel(new GridLayout(2,1));
        JPanel panelTwo = new JPanel(new GridLayout(2,1));
        /*-------------------------------------------------------------*/
        JLabel crossField = new JLabel(Integer.toString(crossParameter),SwingConstants.CENTER);
        JSlider jSliderCross = new JSlider(JSlider.HORIZONTAL,10,100,(int)crossParameter);

        panelOne.add(crossField);
        panelTwo.add(jSliderCross);
        /*-------------------------------------------------------------*/
        JButton applyButton = new JButton("Apply");
        JButton cancelButton = new JButton("Cancel");
        panelOne.add(applyButton);
        panelTwo.add(cancelButton);
        /*-------------------------------------------------------------*/
        jSliderCross.addChangeListener(e -> {
            crossField.setText(((Integer)((JSlider)e.getSource()).getValue()).toString());
            crossParameter = Integer.parseInt(crossField.getText());
            makeEffect(ROBERTCROSS);
        });
        /*-------------------------------------------------------------*/
        applyButton.addActionListener(e -> dialog.dispose());
        cancelButton.addActionListener(e -> {
            crossParameter = 10;
            makeEffect(ROBERTCROSS);
            dialog.dispose();
        });
        /*-------------------------------------------------------------*/
        mainPanel.add(panelOne);
        mainPanel.add(panelTwo);
        dialog.add(mainPanel);
        dialog.setVisible(true);
    }

    private void sobelDialog(){
        JDialog dialog = new JDialog(mainFrame,"Sobel Parameter",true);
        dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        dialog.setSize(300,100);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(mainFrame);
        /*-------------------------------------------------------------*/
        JPanel mainPanel = new JPanel(new GridLayout(1,2));
        JPanel panelOne = new JPanel(new GridLayout(2,1));
        JPanel panelTwo = new JPanel(new GridLayout(2,1));
        /*-------------------------------------------------------------*/
        JLabel sobelField = new JLabel(Integer.toString(sobelParameter),SwingConstants.CENTER);
        JSlider jSliderSobel = new JSlider(JSlider.HORIZONTAL,10,100,(int)sobelParameter);

        panelOne.add(sobelField);
        panelTwo.add(jSliderSobel);
        /*-------------------------------------------------------------*/
        JButton applyButton = new JButton("Apply");
        JButton cancelButton = new JButton("Cancel");
        panelOne.add(applyButton);
        panelTwo.add(cancelButton);
        /*-------------------------------------------------------------*/
        jSliderSobel.addChangeListener(e -> {
            sobelField.setText(((Integer)((JSlider)e.getSource()).getValue()).toString());
            sobelParameter = Integer.parseInt(sobelField.getText());
            makeEffect(SOBEL);
        });
        /*-------------------------------------------------------------*/
        applyButton.addActionListener(e -> dialog.dispose());
        cancelButton.addActionListener(e -> {
            sobelParameter = 10;
            makeEffect(SOBEL);
            dialog.dispose();
        });
        /*-------------------------------------------------------------*/
        mainPanel.add(panelOne);
        mainPanel.add(panelTwo);
        dialog.add(mainPanel);
        dialog.setVisible(true);
    }

    private void saveDialog(){
        if(effectImage != null) {
            JFileChooser fileChooser = new JFileChooser("src/data");
            int res = fileChooser.showSaveDialog(mainFrame);
            if (res == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                File newFile = new File(file.toString() + ".png");
                try {
                    ImageIO.write(effectImage,"png", newFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
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
            picture = new ImageIcon(file.getAbsolutePath()).getImage();

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
        totalSize = xSize < ySize ? xSize : ySize;

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
            zoneASelect.setSize(totalSize+2,totalSize+2);
            BufferedImage testImage = new BufferedImage(bImage.getWidth(), bImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
            zoneASelect.setBorder(new XorBorder(x0,y0,bImage));
            zoneASelect.setLocation(x0,y0);
//            BufferedImage testImage = new BufferedImage(zoneASelect.getWidth(), zoneASelect.getHeight(), BufferedImage.TYPE_INT_ARGB);
//            Graphics2D g2 = testImage.createGraphics();
//            g2.setColor(Color.RED);
//            g2.drawRect(x0,y0, totalSize, totalSize);
//            zoneASelect.add(new JLabel(new ImageIcon(testImage)));
            zoneASelect.revalidate();
        }
    }

    private void drawSelect() {
        if (selectImage != null) {
            zoneB.removeAll();
        }

//        selectImage = new BufferedImage(totalSize, totalSize, BufferedImage.TYPE_INT_ARGB);
        selectImage = new BufferedImage(350, 350, BufferedImage.TYPE_INT_ARGB);

        BufferedImage tmp2 = new BufferedImage(picture.getWidth(null), picture.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g22 = tmp2.createGraphics();
        g22.drawImage(picture, 0, 0, null);
        g22.dispose();

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
//        int temp = x0;
//        for (int y = 0; y < selectImage.getHeight(); y++, y0++) {
//            for (int x = 0; x < selectImage.getWidth(); x++, x0++) {
//                int color = bImage.getRGB(x0,y0);
//                selectImage.setRGB(x, y, color);
//            }
//            x0 = temp;
//        }
        int newX0 = (int)(x0 * selectXsize);
        int newY0 = (int)(y0 * selectXsize);
        int temp = newX0;
        for (int y = 0; y < 350 && y != picture.getHeight(null); y++, newY0++) {
            for (int x = 0; x < 350 && x != picture.getWidth(null); x++, newX0++) {
                int color = tmp2.getRGB(newX0,newY0);
                selectImage.setRGB(x, y, color);
            }
            newX0 = temp;
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
                    effectImage = robertCross.makeRobertCross(selectImage, crossParameter);
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
                    Gamma gamma = new Gamma(gammaParameter);
                    effectImage = gamma.makeGamma(selectImage);
                    break;

                case AQUA:
                    Median median = new Median();
                    Sharpness sharpnes = new Sharpness();
                    effectImage = median.makeMedian(selectImage);
                    effectImage = sharpnes.makeSharpness(effectImage);
                    break;

                case METAL:
                    Metal metal = new Metal();
                    effectImage = metal.makeMetal(selectImage);
                    break;

                case FLOYD:
                    FloydSteinberg floydSteinberg = new FloydSteinberg(redParameter,greenParameter,blueParameter);
                    effectImage = floydSteinberg.makeFloydSteinberg(selectImage);
                    break;

                case ORDER:
                    OrderDithering orderDithering = new OrderDithering();
                    effectImage = orderDithering.makeOrderDither(selectImage);
                    break;

                case ROTATE:
                    Rotate rotate = new Rotate(rotateParameter);
                    effectImage = rotate.makeRotate(selectImage);
                    break;

                case SOBEL:
                    Sobel sobel = new Sobel();
                    effectImage = sobel.makeSobel(selectImage, sobelParameter);
                    break;
            }

            damnImage = new ImageIcon(effectImage).getImage();
            damnImage = new ImageIcon(damnImage.getScaledInstance(348, 348, BufferedImage.SCALE_DEFAULT)).getImage();
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
