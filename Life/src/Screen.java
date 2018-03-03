import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;

public class Screen extends JFrame {
    private File file = null;
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
    private Boolean impactMode = false;
    private Point currentPixel = new Point(-5,-5);
    private boolean isPlay;
    java.util.Timer timer = new java.util.Timer();
    Schedule timerTask = new Schedule();

    private Double LIVE_BEGIN = 2.0;
    private Double BIRTH_BEGIN = 2.3;
    private Double BIRTH_END = 2.9;
    private Double LIVE_END = 3.3;
    private Double FST_IMPACT = 1.0;
    private Double SND_IMPACT = 0.3;
    private Integer SPEED = 1000;

    public Screen() {
        super("LIFE");
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(800,600));
        setJMenuBar(addMenu());
        add(addToolBar(), BorderLayout.NORTH);

        jPanel = new JPanel();
        jPanel.setBackground(Color.WHITE);

        image = new BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB);
        image = colorBackground(Color.WHITE, image);
        jLabel = new JLabel(new ImageIcon(image));
        jPanel.add(jLabel);

        MyWindowListener mwl = new MyWindowListener();
        addWindowListener(mwl);

        JScrollPane scrollPane = new JScrollPane(jPanel);
        add(scrollPane, BorderLayout.CENTER);
        pack();
        setResizable(true);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setVisible(true);

    }

    public BufferedImage colorBackground(Color c, BufferedImage image){
        for(int i=0;i<image.getHeight();i++){
            for(int j=0;j<image.getWidth();j++){
                image.setRGB(j,i,c.getRGB());
            }
        }
        return image;
    }

    private JMenuBar addMenu(){
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        fileMenu.setIcon(new ImageIcon("src/Icons/file.png"));
        /*-------------------------------------------------------------*/
        JMenuItem newItem = new JMenuItem("New");
        newItem.setIcon(new ImageIcon("src/Icons/new.png"));
        fileMenu.add(newItem);

        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.setIcon(new ImageIcon("src/Icons/save10.png"));
        fileMenu.add(saveItem);

        JMenuItem saveAsItem = new JMenuItem("Save As");
        saveAsItem.setIcon(new ImageIcon("src/Icons/saveas10.png"));
        fileMenu.add(saveAsItem);

        JMenuItem settingsItem = new JMenuItem("Settings");
        settingsItem.setIcon(new ImageIcon("src/Icons/settings.png"));
        fileMenu.add(settingsItem);

        JMenuItem lifeSettingsItem = new JMenuItem("Life Settings");
        lifeSettingsItem.setIcon(new ImageIcon("src/Icons/settings.png"));
        fileMenu.add(lifeSettingsItem);

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.setIcon(new ImageIcon("src/Icons/exit.png"));
        fileMenu.add(exitItem);

        menuBar.add(fileMenu);
        /*-------------------------------------------------------------*/
        JMenu gameMenu = new JMenu("Game");
        gameMenu.setIcon(new ImageIcon("src/Icons/game10.png"));

        JMenuItem playItem = new JMenuItem("Play");
        playItem.setIcon(new ImageIcon("src/Icons/play10.png"));
        gameMenu.add(playItem);

        JMenuItem stopItem = new JMenuItem("Stop");
        stopItem.setIcon(new ImageIcon("src/Icons/play10.png"));
        gameMenu.add(stopItem);

        JMenuItem nextItem = new JMenuItem("Next Step");
        nextItem.setIcon(new ImageIcon("src/Icons/next10.png"));
        gameMenu.add(nextItem);

        JMenuItem clearItem = new JMenuItem("Clear");
        clearItem.setIcon(new ImageIcon("src/Icons/clear10.png"));
        gameMenu.add(clearItem);

        JMenuItem impactItem = new JMenuItem("Show Impacts");
        impactItem.setIcon(new ImageIcon("src/Icons/impacts10.png"));
        gameMenu.add(impactItem);

        JMenuItem xorItem = new JMenuItem("XOR Mode");
        xorItem.setIcon(new ImageIcon("src/Icons/xor10.png"));
        gameMenu.add(xorItem);

        menuBar.add(gameMenu);
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
        lifeSettingsItem.addActionListener(e -> liveSettingDialog());
        impactItem.addActionListener(e -> {
            if(!impactMode){
                impactMode = true;
                showImpacts();
            } else {
                impactMode = false;
                closeImpacts();
            }
        });
        xorItem.addActionListener(e -> {
            if(!xorMode){
                xorMode = true;
            } else {
                xorMode = false;
            }
        });
        nextItem.addActionListener(e -> {
            if(!impactMode){
                image = logic.nextStep(image);
                repaint();
            } else {
                closeImpacts();
                image = logic.nextStep(image);
                showImpacts();
            }
        });
        clearItem.addActionListener(e -> clearField());
        playItem.addActionListener(e -> play());
        stopItem.addActionListener(e -> play());
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(this, "New - новое поле\n" +
                "Clear - очистить поле\n" +
                "Play - начать/остановить игру\n" +
                "Next Step - перейти на следующий шаг игры\n" +
                "XOR Mode - включить/выключить XOR режим\n" +
                "Show Impacts - включить/выключить отображение влияния\n" +
                "Settings - настройки поля\n" +
                "Life Settings - настройка игры\n" +
                "Author - об авторе\n" +
                "Exit - выход"));
        saveAsItem.addActionListener(e -> saveDialog());
        saveItem.addActionListener(e ->{
            if(file == null){
                saveDialog();
            } else {
                justSave();
            }
        });
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
            try{
                Integer a = Integer.parseInt(heightField.getText());
                Integer b = Integer.parseInt(widthField.getText());
                Integer c = Integer.parseInt(radiusField.getText());
                Integer d = Integer.parseInt(fatField.getText());

                if(a > 20 || b > 20 || c > 50 || d > 10){
                    JOptionPane.showMessageDialog(dialog,"Wrong input data!\n" +
                            "Height <= 20\n" +
                            "Width <= 20\n" +
                            "Radius <= 40\n" +
                            "Fat <= 10");
                } else {
                    height = Integer.parseInt(heightField.getText());
                    width = Integer.parseInt(widthField.getText());
                    radius = Integer.parseInt(radiusField.getText());
                    fat = Integer.parseInt(fatField.getText());
                    if(logic != null){
                        clearField();
                    }
                    updateImage();
                    dialog.dispose();
                }
            } catch (NumberFormatException nfe){
                JOptionPane.showMessageDialog(dialog,"Wrong input data!\n" +
                        "Try again!");
            }
        });
    }

    private void authorDialog(){
        JDialog dialog = new JDialog(this,"Author",true);
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

    private void saveDialog(){
        if(logic != null) {
            JFileChooser fileChooser = new JFileChooser("src");
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Txt file(.txt)", "txt");
            fileChooser.setFileFilter(filter);
            int res = fileChooser.showSaveDialog(null);
            if (res == JFileChooser.APPROVE_OPTION) {
                file = fileChooser.getSelectedFile();
                System.out.println(file.toString());
                String filePath;
                if (file.toString().contains(".txt")) {
                    filePath = file.toString();
                } else {
                    filePath = file.toString() + ".txt";
                }

                try (FileWriter writer = new FileWriter(filePath, false)) {
                    writer.write(width.toString() + " " + height.toString() + "\r\n");
                    writer.write(fat.toString() + "\r\n");
                    writer.write(radius.toString() + "\r\n");

                    Integer number = logic.liveNumber();
                    System.out.println(number);
                    writer.write(number.toString() + "\r\n");

                    for (int i = 0; i < height; i++) {
                        int tmp = i % 2 == 0 ? width : width - 1;
                        for (int j = 0; j < tmp; j++) {
                            if (logic.getAlive(i, j)) {
                                writer.write(i + " " + j + "\r\n");
                            }
                        }
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    private void justSave(){
        System.out.println(file.toString());
        String filePath;
        if (file.toString().contains(".txt")) {
            filePath = file.toString();
        } else {
            filePath = file.toString() + ".txt";
        }

        try (FileWriter writer = new FileWriter(filePath, false)) {
            writer.write(width.toString() + " " + height.toString() + "\r\n");
            writer.write(fat.toString() + "\r\n");
            writer.write(radius.toString() + "\r\n");

            Integer number = logic.liveNumber();
            System.out.println(number);
            writer.write(number.toString() + "\r\n");

            for (int i = 0; i < height; i++) {
                int tmp = i % 2 == 0 ? width : width - 1;
                for (int j = 0; j < tmp; j++) {
                    if (logic.getAlive(i, j)) {
                        writer.write(i + " " + j + "\r\n");
                    }
                }
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
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
        JButton jButtonLifeSetting = new JButton();
        jButtonLifeSetting.setIcon(new ImageIcon("src/Icons/life32.png"));
        jButtonLifeSetting.setToolTipText("Life Setting");
        /*-------------------------------------------------------------*/
        JButton jButtonOpen = new JButton();
        jButtonOpen.setIcon(new ImageIcon("src/Icons/newfile32.png"));
        jButtonOpen.setToolTipText("Open File");
        /*-------------------------------------------------------------*/
        JButton jButtonSave = new JButton();
        jButtonSave.setIcon(new ImageIcon("src/Icons/save32.png"));
        jButtonSave.setToolTipText("Save");
        /*-------------------------------------------------------------*/
        JButton jButtonSaveAs = new JButton();
        jButtonSaveAs.setIcon(new ImageIcon("src/Icons/saveas32.png"));
        jButtonSaveAs.setToolTipText("Save As");
        /*-------------------------------------------------------------*/
        jToolBar.add(jButtonNew);
        jToolBar.add(jButtonOpen);
        jToolBar.add(jButtonSave);
        jToolBar.add(jButtonSaveAs);
        jToolBar.add(jButtonClear);
        jToolBar.add(jButtonStart);
        jToolBar.add(jButtonNext);
        jToolBar.add(jButtonSwitch);
        jToolBar.add(jButtonImpact);
        jToolBar.add(jButtonSettings);
        jToolBar.add(jButtonLifeSetting);
        jToolBar.add(jButtonAuthor);
        jToolBar.add(jButtonExit);
        /*-------------------------------------------------------------*/
        jButtonExit.addActionListener(e -> System.exit(0));
        jButtonNew.addActionListener(e -> newDialog());
        jButtonSettings.addActionListener(e -> settingsDialog());
        jButtonClear.addActionListener(e -> {
            clearField();
            if(impactMode){
                closeImpacts();
                showImpacts();
            }
        });
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
               play();
           } else {
               jButtonStart.setIcon(new ImageIcon("src/Icons/start32.png"));
               jButtonStart.setActionCommand("PLAY");
               jButtonNext.setEnabled(true);
               play();
           }
        });
        jButtonNext.addActionListener(e -> {
            if(!impactMode){
                image = logic.nextStep(image);
                repaint();
            } else {
                closeImpacts();
                image = logic.nextStep(image);
                showImpacts();
            }
        });
        jButtonLifeSetting.addActionListener(e -> liveSettingDialog());
        jButtonImpact.addActionListener(e -> {
            if(!impactMode && radius >= 30){
                impactMode = true;
                showImpacts();
            } else {
                impactMode = false;
                closeImpacts();
            }
        });
        jButtonOpen.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser("src");
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Txt file(.txt)", "txt");
            fileChooser.setFileFilter(filter);
            int res = fileChooser.showDialog(null,"File Open");
            if (res == JFileChooser.APPROVE_OPTION) {
                file = fileChooser.getSelectedFile();
                try {
                    Parser parser = new Parser(file);
                    parser.readFile();

                    height = parser.getHeight();
                    width = parser.getWidth();
                    radius = parser.getRadius();
                    fat = parser.getFat();
                    updateImage();
                    clearField();

                    HashMap<ArrayList<Integer>, ArrayList<Integer>> map = parser.getField();
                    for (Map.Entry<ArrayList<Integer>, ArrayList<Integer>> entry : map.entrySet()){
                        ArrayList<Integer> x = entry.getKey();
                        ArrayList<Integer> y = entry.getValue();
                        for(int i=0; i<x.size(); i++){
                            Point point = logic.getCentre(x.get(i),y.get(i));
                            paint.fillHexagon(point.x, point.y, Color.RED);
                            logic.setAlive(x.get(i),y.get(i),true);
                            logic.countImpacts();
                        }
                    }
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }
            }
        });
        jButtonSave.addActionListener(e -> {
            if(file == null){
                saveDialog();
            } else {
                justSave();
            }
        });
        jButtonSaveAs.addActionListener(e -> saveDialog());
        /*-------------------------------------------------------------*/
        return jToolBar;
    }

    private void settingsDialog(){
        JDialog dialog = new JDialog(this,"Settings",true);
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
        JSlider jSliderRadius = new JSlider(JSlider.HORIZONTAL,1,50,10);
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

    private void liveSettingDialog(){
        JDialog dialog = new JDialog(this,"Live Settings",true);
        dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        dialog.setSize(250,200);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(this);
        /*-------------------------------------------------------------*/
        JPanel mainPanel = new JPanel(new GridLayout(1,2));
        JPanel panelOne = new JPanel(new GridLayout(7,1));
        JPanel panelTwo = new JPanel(new GridLayout(7,1));
        /*-------------------------------------------------------------*/
        JLabel labelOne = new JLabel("LIVE_BEGIN", SwingConstants.CENTER);
        JTextField liveBeginField = new JTextField();
        if(LIVE_BEGIN != null){
            liveBeginField.setText(LIVE_BEGIN.toString());
        }
        panelOne.add(labelOne);
        panelTwo.add(liveBeginField);
        /*-------------------------------------------------------------*/
        JLabel labelTwo = new JLabel("BIRTH_BEGIN", SwingConstants.CENTER);
        JTextField birthBeginField = new JTextField();
        if(BIRTH_BEGIN != null){
            birthBeginField.setText(BIRTH_BEGIN.toString());
        }
        panelOne.add(labelTwo);
        panelTwo.add(birthBeginField);
        /*-------------------------------------------------------------*/
        JLabel labelThree = new JLabel("BIRTH_END", SwingConstants.CENTER);
        JTextField birthEndField = new JTextField();
        if(LIVE_END != null){
            birthEndField.setText(BIRTH_END.toString());
        }
        panelOne.add(labelThree);
        panelTwo.add(birthEndField);
        /*-------------------------------------------------------------*/
        JLabel labelFour = new JLabel("LIVE_END", SwingConstants.CENTER);
        JTextField liveEndField = new JTextField();
        if(LIVE_END != null){
            liveEndField.setText(LIVE_END.toString());
        }
        panelOne.add(labelFour);
        panelTwo.add(liveEndField);
        /*-------------------------------------------------------------*/
        JLabel labelFive = new JLabel("FIRST_IMPACT", SwingConstants.CENTER);
        JTextField firstImpactField = new JTextField();
        if(FST_IMPACT != null){
            firstImpactField.setText(FST_IMPACT.toString());
        }
        panelOne.add(labelFive);
        panelTwo.add(firstImpactField);
        /*-------------------------------------------------------------*/
        JLabel labelSix = new JLabel("SECOND_IMPACT", SwingConstants.CENTER);
        JTextField secondImpactField = new JTextField();
        if(SND_IMPACT != null){
            secondImpactField.setText(SND_IMPACT.toString());
        }
        panelOne.add(labelSix);
        panelTwo.add(secondImpactField);
        /*-------------------------------------------------------------*/
        JButton accept = new JButton("Accept");
        panelOne.add(accept);
        /*-------------------------------------------------------------*/
        mainPanel.add(panelOne);
        mainPanel.add(panelTwo);
        dialog.add(mainPanel);
        /*-------------------------------------------------------------*/
        accept.addActionListener(e -> {
            try {
                Double live_begin = Double.parseDouble(liveBeginField.getText());
                Double live_end = Double.parseDouble(liveEndField.getText());
                Double birth_begin = Double.parseDouble(birthBeginField.getText());
                Double birth_end = Double.parseDouble(birthEndField.getText());
                Double fst_impact = Double.parseDouble(firstImpactField.getText());
                Double snd_impact = Double.parseDouble(secondImpactField.getText());

                if (live_begin <= birth_begin && birth_begin <= birth_end && birth_end <= live_end) {
                    LIVE_BEGIN = live_begin;
                    LIVE_END = live_end;
                    BIRTH_BEGIN = birth_begin;
                    BIRTH_END = birth_end;
                    FST_IMPACT = fst_impact;
                    SND_IMPACT = snd_impact;
                    logic.setOptions(LIVE_BEGIN, BIRTH_BEGIN, BIRTH_END, LIVE_END, FST_IMPACT, SND_IMPACT);
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Wrong options!\n" +
                            "Look at this:\n" +
                            "LIVE_BEGIN <= BIRTH_BEGIN <= BIRTH_END <= LIVE_END");
                }
            } catch (NumberFormatException nfe){
                JOptionPane.showMessageDialog(dialog, "Wrong options!\n" +
                        "Look at this:\n" +
                        "LIVE_BEGIN <= BIRTH_BEGIN <= BIRTH_END <= LIVE_END\n" +
                        "And it must be numbers!");
            }
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
        image = colorBackground(Color.WHITE, image);

        if(logic == null){
            logic = new Logic(width,height,radius,fat);
            logic.setOptions(2.0,2.3,2.9,3.3,1.0,0.3);
        } else {
            logic.newLogic(width,height,radius,fat);
        }

        paint = new Paint(image);
        image = paint.drawField(width,height,radius,fat);
        image = logic.resurrectImage(image);

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
                Point point = logic.whatHex(x,y);
                if(point.x != -1){
                    if(color == Color.RED){
                        logic.setAlive(point.x, point.y, true);
                    } else {
                        logic.setAlive(point.x, point.y, false);
                    }
                    if(impactMode){
                        closeImpacts();
                        image =  paint.fillHexagon(x,y,color);
                        logic.countImpacts();
                        showImpacts();
                    } else {
                        image =  paint.fillHexagon(x,y,color);
                        logic.countImpacts();
                        repaint();
                    }
                }
            }catch (ArrayIndexOutOfBoundsException exception){}
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

            point = logic.whatHex(x,y);
            if(point.x != -1){
                if(color == Color.RED){
                    logic.setAlive(point.x, point.y, true);
                } else if (color == Color.WHITE){
                    logic.setAlive(point.x, point.y, false);
                }
            }
        }
    }

    public class MyWindowListener implements WindowListener{
        public void windowActivated(WindowEvent e) {}

        public void windowClosed(WindowEvent e) {}

        public void windowClosing(WindowEvent e) {
            System.out.println("windowClosing()");
            int choice = JOptionPane.showOptionDialog(null,
                        "Do you want to save?",
                        "Quit?",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null, null, null);

                if(choice == JOptionPane.YES_OPTION){
                    saveDialog();
                    System.exit(0);
                } else {
                    System.exit(0);
                }
        }

        public void windowDeactivated(WindowEvent e) {}

        public void windowDeiconified(WindowEvent e) {}

        public void windowIconified(WindowEvent e) {}

        public void windowOpened(WindowEvent e) {}
    }

    public void play(){
        if (isPlay){
            stop();
        } else {
            if (logic.isLive()){
                isPlay = true;
                timer.schedule(timerTask, 0, SPEED);
            }
        }
    }

    public void stop(){
        if(isPlay) {
            timer.cancel();
            timer.purge();

            timer = new java.util.Timer();
            timerTask = new Schedule();

            isPlay = false;
        }
    }

    private class Schedule extends TimerTask{
        @Override
        public void run() {
            if(logic.isLive()) {
                if(!impactMode){
                    image = logic.nextStep(image);
                    repaint();
                } else {
                    closeImpacts();
                    image = logic.nextStep(image);
                    showImpacts();
                }
            } else {
                stop();
            }
        }
    }

    private void clearField(){
        Paint p = new Paint(image);

        for(int i=0; i<height; i++){
            int mc = i%2==0 ? width : width-1;
            for(int j=0; j<mc; j++){
                Point pt = logic.getCentre(i,j);
                p.fillHexagon(pt.x, pt.y, Color.WHITE);
            }
        }
        logic.clearField();
        repaint();
    }

    private void showImpacts(){
        Graphics2D g2 = image.createGraphics();
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("TimesRoman", Font.PLAIN, radius/2));

        for(int i=0; i<height; i++){
            int mc = i%2==0 ? width : width-1;
            for(int j=0; j<mc; j++){
                double imp = logic.getImpact(i,j);
                String formattedDouble;
                if(imp - (int)imp > 0){
                    formattedDouble = String.format("%.1f", imp);
                    Point p = logic.getCentre(i,j);
                    g2.drawString(formattedDouble, p.x - radius/8, p.y + radius/2);
                } else {
                    formattedDouble = String.format("%.0f", imp);
                    Point p = logic.getCentre(i,j);
                    g2.drawString(formattedDouble, p.x + radius/6, p.y + radius/2);
                }
            }
        }
        repaint();
    }

    private void closeImpacts(){
        updateImage();
    }

    public static void main(String[] args) {
        new Screen();
    }
}


