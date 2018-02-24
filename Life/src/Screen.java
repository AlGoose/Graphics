import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class Screen extends JFrame {
    private Logic logic;
    private BufferedImage image;
    private Paint paint;
    private JLabel jLabel;
    private JPanel jPanel;
    private Integer width = null;
    private Integer height = null;
    private Integer radius = null;
    private Integer fat = null;
    private Boolean xorMode = false;
    private Point currentPixel = new Point(-5,-5);

    public Screen() {
        super("LIFE");
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(800,600));
        setJMenuBar(addMenu());
        add(addToolBar(), BorderLayout.NORTH);

        jPanel = new JPanel();
        jPanel.setBackground(Color.WHITE);

        image = new BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB);
        colorBackground(Color.WHITE);
        jLabel = new JLabel(new ImageIcon(image));
        jPanel.add(jLabel);

        JScrollPane scrollPane = new JScrollPane(jPanel);
        add(scrollPane, BorderLayout.CENTER);
        pack();
        setResizable(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void colorBackground(Color c){
        for(int i=0;i<image.getHeight();i++){
            for(int j=0;j<image.getWidth();j++){
                image.setRGB(j,i,c.getRGB());
            }
        }
    }

    private JMenuBar addMenu(){
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        fileMenu.setIcon(new ImageIcon("src/Icons/file.png"));
        /*-------------------------------------------------------------*/
        JMenuItem newItem = new JMenuItem("New");
        newItem.setIcon(new ImageIcon("src/Icons/new.png"));
        fileMenu.add(newItem);

        JMenuItem settingsItem = new JMenuItem("Settings");
        settingsItem.setIcon(new ImageIcon("src/Icons/settings.png"));
        fileMenu.add(settingsItem);

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.setIcon(new ImageIcon("src/Icons/exit.png"));
        fileMenu.add(exitItem);

        menuBar.add(fileMenu);
        /*-------------------------------------------------------------*/
        JMenu infoMenu = new JMenu("Info");
        infoMenu.setIcon(new ImageIcon("src/Icons/info.png"));

        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.setIcon(new ImageIcon("src/Icons/about.png"));
        infoMenu.add(aboutItem);

        JMenuItem authorItem = new JMenuItem("Author");
        authorItem.setIcon(new ImageIcon("src/Icons/author.png"));
        infoMenu.add(authorItem);

        menuBar.add(infoMenu);
        /*-------------------------------------------------------------*/
        exitItem.addActionListener(e -> System.exit(0));
        newItem.addActionListener(e -> newDialog());
        authorItem.addActionListener(e -> authorDialog());
        settingsItem.addActionListener(e -> settingsDialog());
        return menuBar;
    }

    private void newDialog(){
        JFrame dialog = new JFrame("New");
        dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        dialog.setSize(200,180);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(this);
        /*-------------------------------------------------------------*/
        JPanel mainPanel = new JPanel(new GridLayout(1,2));
        JPanel panelOne = new JPanel(new GridLayout(5,1));
        JPanel panelTwo = new JPanel(new GridLayout(5,1));
        /*-------------------------------------------------------------*/
        JLabel labelOne = new JLabel("Ширина", SwingConstants.CENTER);
        JTextField widthField = new JTextField();
        panelOne.add(labelOne);
        panelTwo.add(widthField);
        /*-------------------------------------------------------------*/
        JLabel labelTwo = new JLabel("Высота",SwingConstants.CENTER);
        JTextField heightField = new JTextField();
        panelOne.add(labelTwo);
        panelTwo.add(heightField);
        /*-------------------------------------------------------------*/
        JLabel labelThree = new JLabel("Радиус", SwingConstants.CENTER);
        JTextField radiusField = new JTextField();
        panelOne.add(labelThree);
        panelTwo.add(radiusField);
        /*-------------------------------------------------------------*/
        JLabel labelFour = new JLabel("Толщина",SwingConstants.CENTER);
        JTextField fatField = new JTextField();
        panelOne.add(labelFour);
        panelTwo.add(fatField);
        /*-------------------------------------------------------------*/
        JButton button = new JButton("Accept");
        panelOne.add(button);

        mainPanel.add(panelOne);
        mainPanel.add(panelTwo);
        dialog.add(mainPanel);
        dialog.setVisible(true);

        button.addActionListener(e -> {
            height = Integer.parseInt(heightField.getText());
            width = Integer.parseInt(widthField.getText());
            radius = Integer.parseInt(radiusField.getText());
            fat = Integer.parseInt(fatField.getText());
            updateImage();
            if(logic == null){
                logic = new Logic(width,height,radius,fat);
            }
        });
    }

    private void authorDialog(){
        JFrame dialog = new JFrame("Author");
        dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        dialog.setSize(300,200);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(this);
        /*-------------------------------------------------------------*/
        JPanel jPanelOne = new JPanel(new GridLayout(1,2));
        JPanel jPanelTwo = new JPanel(new GridLayout(4,1));

        ImageIcon imageIcon = new ImageIcon("src/Icons/portret.jpg");
        Image image = imageIcon.getImage();
        Image newImage = image.getScaledInstance(100, 150,  java.awt.Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(newImage);

        JLabel jOne = new JLabel(imageIcon);
        JLabel jTwo = new JLabel("Author: Alexandr Gusev");
        JLabel jThree = new JLabel("University: NSU");
        JLabel jFour = new JLabel("Group: FIT 15203");
        JLabel jFive = new JLabel("Year: 2018");
        /*-------------------------------------------------------------*/
        jPanelOne.add(jOne);
        jPanelTwo.add(jTwo);
        jPanelTwo.add(jThree);
        jPanelTwo.add(jFour);
        jPanelTwo.add(jFive);
        jPanelOne.add(jPanelTwo);
        /*-------------------------------------------------------------*/
        dialog.add(jPanelOne);
        dialog.setVisible(true);
    }

    private JToolBar addToolBar(){
        JToolBar jToolBar = new JToolBar();
        jToolBar.setFloatable(false);
        /*-------------------------------------------------------------*/
        JButton jButtonNew = new JButton();
        jButtonNew.setIcon(new ImageIcon("src/Icons/new32.png"));
        jButtonNew.setToolTipText("New Field");
        /*-------------------------------------------------------------*/
        JButton jButtonClear = new JButton();
        jButtonClear.setIcon(new ImageIcon("src/Icons/clear32.png"));
        jButtonClear.setToolTipText("Clear Field");
         /*-------------------------------------------------------------*/
        JButton jButtonSettings = new JButton();
        jButtonSettings.setIcon(new ImageIcon("src/Icons/settings32.png"));
        jButtonSettings.setToolTipText("Settings");
        /*-------------------------------------------------------------*/
        JButton jButtonStart = new JButton();
        jButtonStart.setIcon(new ImageIcon("src/Icons/start32.png"));
        jButtonStart.setToolTipText("Start/Pause Game");
        jButtonStart.setActionCommand("PLAY");
        /*-------------------------------------------------------------*/
        JButton jButtonNext = new JButton();
        jButtonNext.setIcon(new ImageIcon("src/Icons/next32.png"));
        jButtonNext.setToolTipText("Next Step");
        /*-------------------------------------------------------------*/
        JButton jButtonExit = new JButton();
        jButtonExit.setIcon(new ImageIcon("src/Icons/exit32.png"));
        jButtonExit.setToolTipText("Exit");
        /*-------------------------------------------------------------*/
        JButton jButtonAuthor = new JButton();
        jButtonAuthor.setIcon(new ImageIcon("src/Icons/author32.png"));
        jButtonAuthor.setToolTipText("Author");
        /*-------------------------------------------------------------*/
        JButton jButtonSwitch = new JButton();
        jButtonSwitch.setIcon(new ImageIcon("src/Icons/switchoff32.png"));
        jButtonSwitch.setToolTipText("Change XOR/Replace Mode");
        /*-------------------------------------------------------------*/
        JButton jButtonImpact = new JButton();
        jButtonImpact.setIcon(new ImageIcon("src/Icons/impact32.png"));
        jButtonImpact.setToolTipText("On/Off Impact");
        /*-------------------------------------------------------------*/
        jToolBar.add(jButtonNew);
        jToolBar.add(jButtonClear);
        jToolBar.add(jButtonStart);
        jToolBar.add(jButtonNext);
        jToolBar.add(jButtonSwitch);
        jToolBar.add(jButtonImpact);
        jToolBar.add(jButtonSettings);
        jToolBar.add(jButtonAuthor);
        jToolBar.add(jButtonExit);
        /*-------------------------------------------------------------*/
        jButtonExit.addActionListener(e -> System.exit(0));
        jButtonNew.addActionListener(e -> newDialog());
        jButtonSettings.addActionListener(e -> settingsDialog());
        jButtonClear.addActionListener(e -> updateImage());
        jButtonAuthor.addActionListener(e -> authorDialog());
        jButtonSwitch.addActionListener(e -> {
            if(!xorMode){
                xorMode = true;
                jButtonSwitch.setIcon(new ImageIcon("src/Icons/switchon32.png"));
            } else {
                xorMode = false;
                jButtonSwitch.setIcon(new ImageIcon("src/Icons/switchoff32.png"));
            }
        });
        jButtonStart.addActionListener(e -> {
           if(jButtonStart.getActionCommand().equals("PLAY")){
               jButtonStart.setIcon(new ImageIcon("src/Icons/stop32.png"));
               jButtonStart.setActionCommand("STOP");
               jButtonNext.setEnabled(false);
           } else {
               jButtonStart.setIcon(new ImageIcon("src/Icons/start32.png"));
               jButtonStart.setActionCommand("PLAY");
               jButtonNext.setEnabled(true);
           }
        });
        /*-------------------------------------------------------------*/
        return jToolBar;
    }

    private void settingsDialog(){
        JFrame dialog = new JFrame("Settings");
        dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        dialog.setSize(300,200);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(this);
        /*-------------------------------------------------------------*/
        JPanel mainPanel = new JPanel(new GridLayout(1,3));
        JPanel panelOne = new JPanel(new GridLayout(5,1));
        JPanel panelTwo = new JPanel(new GridLayout(5,1));
        JPanel panelThree = new JPanel(new GridLayout(5,1));
        /*-------------------------------------------------------------*/
        JLabel labelOne = new JLabel("Ширина", SwingConstants.CENTER);
        JTextField widthField = new JTextField();
        JSlider jSliderWidth = new JSlider(JSlider.HORIZONTAL,1,20,1);
        if(width != null){
            widthField.setText(width.toString());
            jSliderWidth.setValue(width);
        }
        panelOne.add(labelOne);
        panelTwo.add(jSliderWidth);
        panelThree.add(widthField);
        /*-------------------------------------------------------------*/
        JLabel labelTwo = new JLabel("Высота", SwingConstants.CENTER);
        JTextField heightField = new JTextField();
        JSlider jSliderHeight = new JSlider(JSlider.HORIZONTAL,1,20,1);
        if(height != null){
            heightField.setText(height.toString());
            jSliderHeight.setValue(height);
        }
        panelOne.add(labelTwo);
        panelTwo.add(jSliderHeight);
        panelThree.add(heightField);
        /*-------------------------------------------------------------*/
        JLabel labelThree = new JLabel("Радиус", SwingConstants.CENTER);
        JTextField radiusField = new JTextField();
        JSlider jSliderRadius = new JSlider(JSlider.HORIZONTAL,1,40,10);
        if(radius != null){
            radiusField.setText(radius.toString());
            jSliderRadius.setValue(radius);
        }
        panelOne.add(labelThree);
        panelTwo.add(jSliderRadius);
        panelThree.add(radiusField);
        /*-------------------------------------------------------------*/
        JLabel labelFour = new JLabel("Толщина", SwingConstants.CENTER);
        JTextField fatField = new JTextField();
        JSlider jSliderFat = new JSlider(JSlider.HORIZONTAL,1,10,1);
        if(fat != null){
            fatField.setText(fat.toString());
            jSliderFat.setValue(fat);
        }
        panelOne.add(labelFour);
        panelTwo.add(jSliderFat);
        panelThree.add(fatField);
        /*-------------------------------------------------------------*/
        JRadioButton xorButton = new JRadioButton("XOR");
        JRadioButton replaceButton = new JRadioButton("Replace");
        if(!xorMode){
            xorButton.setSelected(false);
            replaceButton.setSelected(true);
        } else {
            xorButton.setSelected(true);
            replaceButton.setSelected(false);
        }
        JLabel modeLabel = new JLabel("Replace Mode", SwingConstants.CENTER);

        panelOne.add(xorButton);
        panelTwo.add(modeLabel);
        panelThree.add(replaceButton);
        /*-------------------------------------------------------------*/
        mainPanel.add(panelOne);
        mainPanel.add(panelTwo);
        mainPanel.add(panelThree);
        dialog.add(mainPanel);
        /*-------------------------------------------------------------*/
        jSliderWidth.addChangeListener(e -> {
            if(width != null && height != null && radius != null && fat != null){
                widthField.setText(((Integer)((JSlider)e.getSource()).getValue()).toString());
                width = Integer.parseInt(widthField.getText());
                updateImage();
            }
        });
        jSliderHeight.addChangeListener(e -> {
            if(width != null && height != null && radius != null && fat != null){
                heightField.setText(((Integer)((JSlider)e.getSource()).getValue()).toString());
                height = Integer.parseInt(heightField.getText());
                updateImage();
            }
        });
        jSliderRadius.addChangeListener(e -> {
            if(width != null && height != null && radius != null && fat != null){
                radiusField.setText(((Integer)((JSlider)e.getSource()).getValue()).toString());
                radius = Integer.parseInt(radiusField.getText());
                updateImage();
            }
        });
        jSliderFat.addChangeListener(e -> {
            if(width != null && height != null && radius != null && fat != null){
                fatField.setText(((Integer)((JSlider)e.getSource()).getValue()).toString());
                fat = Integer.parseInt(fatField.getText());
                updateImage();
            }
        });
        /*-------------------------------------------------------------*/
        widthField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                jSliderWidth.setValue(Integer.parseInt(widthField.getText()));
            }
        });

        heightField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                jSliderHeight.setValue(Integer.parseInt(heightField.getText()));
            }
        });

        radiusField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                jSliderRadius.setValue(Integer.parseInt(radiusField.getText()));
            }
        });

        fatField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                jSliderFat.setValue(Integer.parseInt(fatField.getText()));
            }
        });
        /*-------------------------------------------------------------*/
        xorButton.addActionListener(e -> {
            xorButton.setSelected(true); ;
            replaceButton.setSelected(false);
            modeLabel.setText("XOR Mode");
            xorMode = true;
        });
        replaceButton.addActionListener(e -> {
            replaceButton.setSelected(true);
            xorButton.setSelected(false);
            modeLabel.setText("Replace Mode");
            xorMode = false;
        });
        /*-------------------------------------------------------------*/
        dialog.setVisible(true);
    }

    private void updateImage(){
        jPanel.removeAll();
        jPanel.revalidate();
        repaint();

        Point point = getDelta(radius,fat);
        int pixelWidth = width * point.x + point.x/2 + 5;
        int pixelHeight = (height+1)*point.y;

        image = new BufferedImage(pixelWidth, pixelHeight, BufferedImage.TYPE_INT_ARGB);
        colorBackground(Color.WHITE);
        paint = new Paint(image);
        image = paint.drawField(width,height,radius,fat);

        jLabel = new JLabel(new ImageIcon(image));
        jPanel.add(jLabel);

        CustomListener listeners = new CustomListener();
        jLabel.addMouseListener(listeners);
        jLabel.addMouseMotionListener(listeners);
        revalidate();
    }

    public Point getDelta(int radius, int fat){
        radius+=fat/2;
        int deltaX = (int)Math.ceil(Math.sqrt(3)*radius);
        int deltaX2 = deltaX / 2;

        while(deltaX != deltaX2*2){
            radius++;
            deltaX = (int)Math.ceil(Math.sqrt(3)*radius);
            deltaX2 = deltaX/2;
        }

        int deltaY = (int)Math.ceil(3*radius/2);
        return new Point(deltaX,deltaY);
    }

    public class CustomListener extends MouseAdapter{
        @Override
        public void mouseClicked(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            Color color;
            if(xorMode){
                color = image.getRGB(x,y) == Color.RED.getRGB() ? Color.WHITE : Color.RED;
            } else {
                color = Color.RED;
            }

            try{
                image =  paint.fillHexagon(x,y,color);
                repaint();
            }catch (ArrayIndexOutOfBoundsException exception){}
            Point point = logic.whatHex(x,y);
            System.out.println(point.x + " : " + point.y);
        }

        @Override
        public void mouseDragged(MouseEvent e){
            int x = e.getX();
            int y = e.getY();
            Color color;
            Point point;
            if(xorMode){
                Color tmp = new Color(image.getRGB(x,y));
                if(tmp.getRGB() == Color.RED.getRGB() || tmp.getRGB() == Color.WHITE.getRGB()){
                    point = logic.whatHex(x,y);
                    if(currentPixel.x == point.x && currentPixel.y == point.y) {
                        color = new Color(image.getRGB(x,y));
                    } else {
                        color = image.getRGB(x,y) == Color.RED.getRGB() ? Color.WHITE : Color.RED;
                    }
                    currentPixel = point;
                } else {
                    color = Color.BLACK;
                }
            } else {
                color = Color.RED;
            }
            try{
                image =  paint.fillHexagon(x,y,color);
                repaint();
            }catch (ArrayIndexOutOfBoundsException exception){}
        }
    }

    public static void main(String[] args) {
        new Screen();
    }
}
