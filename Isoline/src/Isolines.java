import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

public class Isolines{
    private JFrame mainFrame;
    private JPanel mapPanel;
    private JPanel legendPanel;
    private JPanel valuePanel;
    private JPanel mainPanel;
    private BufferedImage map;
    private BufferedImage legend;
    private JLabel statusBar;

    private MainFunction mainFunction;
    private LegendFunction legendFunction;
    private Constants constants;
    private Paint paint;

    private Boolean netMode = false;
    private Boolean interpolationMode = false;
    private Boolean isolineMode = false;
    private Boolean clickMode = false;
    private Boolean dynamicMode = false;
    private Boolean jopa = true;
    private Boolean circleMode = false;

    private int startXSize;
    private int startYSize;

    private void start(){
        mainFrame = new JFrame("Isolines");
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setSize(new Dimension(850,700));
        mainFrame.setMinimumSize(new Dimension(850,700));
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.add(Toolbar(),BorderLayout.NORTH);
        mainFrame.add(Panel(),BorderLayout.CENTER);
        mainFrame.add(StatusBar(),BorderLayout.SOUTH);

        mainFrame.setVisible(true);
    }

    private JToolBar Toolbar(){
        JToolBar jToolBar = new JToolBar();
        jToolBar.setFloatable(false);
        /*-------------------------------------------------------------*/
        JButton jButtonNew = new JButton(new ImageIcon("src/icons/new24.png"));
        JButton jButtonSetting = new JButton(new ImageIcon("src/icons/settings24.png"));
        JButton jButtonInterpolation = new JButton(new ImageIcon("src/icons/brush24.png"));
        JButton jButtonNet = new JButton(new ImageIcon("src/icons/net24.png"));
        JButton jButtonIsoline = new JButton(new ImageIcon("src/icons/lines24.png"));
        JButton jButtonClick = new JButton(new ImageIcon("src/icons/click24.png"));
        JButton jButtonDynamic = new JButton(new ImageIcon("src/icons/move24.png"));
        JButton jButtonCircle = new JButton(new ImageIcon("src/icons/circle24.png"));
        /*-------------------------------------------------------------*/
        jToolBar.add(jButtonNew);
        jToolBar.add(jButtonSetting);
        jToolBar.add(new JToolBar.Separator());
        jToolBar.add(jButtonInterpolation);
        jToolBar.add(new JToolBar.Separator());
        jToolBar.add(jButtonNet);
        jToolBar.add(jButtonIsoline);
        jToolBar.add(jButtonClick);
        jToolBar.add(jButtonDynamic);
        jToolBar.add(jButtonCircle);
        /*-------------------------------------------------------------*/
        jButtonNet.addActionListener(e -> {
            if(netMode){
                netMode = false;
                drawMap();
                if(isolineMode){
                    drawIsoline();
                }
            } else {
                netMode = true;
                drawGrid();
            }
        });
        jButtonSetting.addActionListener(e -> settingsDialog());
        jButtonNew.addActionListener(e -> {
            this.constants = new Constants();
            this.constants.readFile(mainFrame);
            this.mainFunction = new MainFunction(constants.A,constants.B,constants.C,constants.D,constants.gridWidth,constants.gridHeight);
            this.legendFunction = new LegendFunction();
            this.constants.makeSegments(mainFunction,legendFunction);
            this.paint = new Paint(this.constants);

            this.startXSize = mapPanel.getWidth();
            this.startYSize = mapPanel.getHeight();
            System.out.println(startXSize + " | " + startYSize);

            drawMap();
            drawLegend();
            drawValue();
        });
        jButtonInterpolation.addActionListener(e -> {
            if(interpolationMode){
                interpolationMode = false;
                drawMap();
                drawLegend();
                jButtonInterpolation.setIcon(new ImageIcon("src/icons/brush24.png"));
                if(netMode){
                    drawGrid();
                }
            } else {
                interpolationMode = true;
                makeInterpolation();
                jButtonInterpolation.setIcon(new ImageIcon("src/icons/map24.png"));
                if(netMode){
                    drawGrid();
                }
            }
        });
        jButtonIsoline.addActionListener(e -> {
            if(isolineMode){
                isolineMode = false;
                drawMap();
                if(netMode){
                    drawGrid();
                }
            } else {
                if(jopa){
                    drawIsoline();
                    jopa = false;
                }
                isolineMode = true;

                double xCoeff = (double)mapPanel.getWidth() / startXSize;
                double yCoeff = (double)mapPanel.getHeight() / startYSize;

                map = paint.drawBufferedIsolines(map,xCoeff,yCoeff);
                mapPanel.repaint();

                if(netMode){
                    drawGrid();
                }
            }
        });
        jButtonClick.addActionListener(e -> {
            if(clickMode){
                clickMode = false;
                jButtonClick.setIcon(new ImageIcon("src/icons/click24.png"));
            } else {
                clickMode = true;
                jButtonClick.setIcon(new ImageIcon("src/icons/cross24.png"));
            }
        });
        jButtonDynamic.addActionListener(e -> {
            if(dynamicMode){
                dynamicMode = false;
                jButtonDynamic.setIcon(new ImageIcon("src/icons/move24.png"));
            } else {
                dynamicMode = true;
                jButtonDynamic.setIcon(new ImageIcon("src/icons/cross24.png"));
            }
        });
        jButtonCircle.addActionListener(e -> {
            if(circleMode){
                circleMode = false;
                drawMap();
                if(isolineMode){
                    drawIsoline();
                }
            } else {
                circleMode = true;
                drawCircle();
            }
        });
        /*-------------------------------------------------------------*/
        return jToolBar;
    }

    private JPanel Panel(){
        int width = mainFrame.getWidth();
        int height = mainFrame.getHeight();
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        mainPanel.setPreferredSize(new Dimension(width, height));

        mapPanel = new JPanel(new BorderLayout());
        mapPanel.setPreferredSize(new Dimension((int)(width*0.8),height));

        valuePanel = new JPanel(new BorderLayout());
        valuePanel.setPreferredSize(new Dimension((int)(width*0.1),height));

        legendPanel = new JPanel(new BorderLayout());
        legendPanel.setPreferredSize(new Dimension((int)(width*0.1),height));


        mainPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent evt) {
                Component c = (Component)evt.getSource();
                Dimension newSize = c.getSize();
                mapPanel.setPreferredSize(new Dimension((int)(newSize.width*0.8),newSize.height));
                valuePanel.setPreferredSize(new Dimension((int)(newSize.width*0.1),newSize.height));
                legendPanel.setPreferredSize(new Dimension((int)(newSize.width*0.1),newSize.height));
                if(mainFunction != null){
                    drawMap();
                    drawValue();
                    drawLegend();
                    if(netMode){
                        drawGrid();
                    }
                    if(isolineMode){
                        double xCoeff = (double)mapPanel.getWidth() / startXSize;
                        double yCoeff = (double)mapPanel.getHeight() / startYSize;

                        map = paint.drawBufferedIsolines(map,xCoeff,yCoeff);
                        mapPanel.repaint();
//                        drawIsoline();
                    }
                }
            }
        });

        mapPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(clickMode && map != null){
                    Point point = e.getPoint();

                    double deltaX = (mainFunction.getB() - mainFunction.getA())/map.getWidth();
                    double deltaY = (mainFunction.getD() - mainFunction.getC())/map.getHeight();

                    double valueX = deltaX * point.getX();
                    double valueY = deltaY * point.getY();
                    double res = mainFunction.findValue(valueX,valueY);

                    constants.xCoeff = (double)mapPanel.getWidth() / startXSize;
                    constants.yCoeff = (double)mapPanel.getHeight() / startYSize;
                    map = paint.drawIso(mainFunction, map, res);
                    mapPanel.repaint();
                }
            }
        });

        mapPanel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if(dynamicMode && map != null){
                    Point point = e.getPoint();

                    double deltaX = (mainFunction.getB() - mainFunction.getA())/map.getWidth();
                    double deltaY = (mainFunction.getD() - mainFunction.getC())/map.getHeight();

                    double valueX = deltaX * point.getX();
                    double valueY = deltaY * point.getY();
                    double res = mainFunction.findValue(valueX,valueY);

                    map = paint.drawMap(mainFunction,map);
//                    map = paint.drawD;
                    constants.xCoeff = (double)mapPanel.getWidth() / startXSize;
                    constants.yCoeff = (double)mapPanel.getHeight() / startYSize;
                    map = paint.drawDynamicIsoline(mainFunction, map, res);

                    if(isolineMode){
                        double xCoeff = (double)mapPanel.getWidth() / startXSize;
                        double yCoeff = (double)mapPanel.getHeight() / startYSize;

                        map = paint.drawBufferedIsolines(map,xCoeff,yCoeff);
                    }
                    mapPanel.repaint();

                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                if(map != null){
                    Point point = e.getPoint();

                    double deltaX = (mainFunction.getB() - mainFunction.getA())/map.getWidth();
                    double deltaY = (mainFunction.getD() - mainFunction.getC())/map.getHeight();

                    double valueX = deltaX * point.getX();
                    String formattedX = String.format("%.2f", valueX);
                    double valueY = deltaY * point.getY();
                    String formattedY = String.format("%.2f", valueY);
                    double res = mainFunction.findValue(valueX,valueY);
                    String formattedRes = String.format("%.2f", res);

                    statusBar.setText("X: " + formattedX + " | Y: " + formattedY + " | F: " + formattedRes);
                }
            }
        });

        mainPanel.add(mapPanel);
        mainPanel.add(valuePanel);
        mainPanel.add(legendPanel);
        return mainPanel;
    }

    private JLabel StatusBar(){
        statusBar = new JLabel("Ready");
        statusBar.setPreferredSize(new Dimension(mainFrame.getWidth(), 20));
        statusBar.setBorder(new CompoundBorder(new LineBorder(Color.DARK_GRAY),
                new EmptyBorder(4, 4, 4, 4)));
        return statusBar;
    }

    private void drawMap(){
        if(mapPanel != null){
            mapPanel.removeAll();
        }

        this.map = new BufferedImage(mapPanel.getWidth(),mapPanel.getHeight(),BufferedImage.TYPE_INT_ARGB);
        this.map = paint.drawMap(mainFunction,map);
        JLabel test = new JLabel(new ImageIcon(map));
        test.setBounds(0,0,map.getWidth(), map.getHeight());
        mapPanel.add(test);
        mapPanel.repaint();
    }

    private void drawLegend(){
        if(legendPanel != null){
            legendPanel.removeAll();
        }

        legend = new BufferedImage(legendPanel.getWidth(),legendPanel.getHeight(),BufferedImage.TYPE_INT_ARGB);
        legend = paint.drawLegend(legendFunction, legend);
        JLabel test2 = new JLabel(new ImageIcon(legend));
        test2.setBounds(0,0,legend.getWidth(), legend.getHeight());
        legendPanel.add(test2, BorderLayout.CENTER);
        legendPanel.repaint();
    }

    private void drawGrid(){
        this.map = paint.drawGrid(map);
        this.mapPanel.repaint();
    }

    private void drawValue(){
        if(valuePanel != null){
            valuePanel.removeAll();
        }

        BufferedImage value = new BufferedImage(valuePanel.getWidth(),valuePanel.getHeight(),BufferedImage.TYPE_INT_ARGB);
        value = paint.drawValue(value);
        JLabel test2 = new JLabel(new ImageIcon(value));
        test2.setBounds(0,0,value.getWidth(), value.getHeight());
        valuePanel.add(test2, BorderLayout.CENTER);
        valuePanel.repaint();
    }

    private void drawIsoline(){
        this.map = paint.drawIsoline(mainFunction, map);
        this.mapPanel.repaint();
    }

    private void drawCircle(){
        double xCoeff = (double)mapPanel.getWidth() / startXSize;
        double yCoeff = (double)mapPanel.getHeight() / startYSize;
        this.map = paint.drawCircles(map,xCoeff,yCoeff);
        this.mapPanel.repaint();
    }

    private void makeInterpolation(){
        this.map = paint.makeInterpolationMap(mainFunction, map);
        this.legend = paint.makeInterpolationLegend(legendFunction, legend);
        this.mapPanel.repaint();
        this.legendPanel.repaint();
    }

    private void settingsDialog(){
        JDialog dialog = new JDialog(mainFrame,"Settings",true);
        dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        dialog.setSize(200,300);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(mainFrame);
        /*-------------------------------------------------------------*/
        JPanel mainPanel = new JPanel(new GridLayout(1,2));
        JPanel panelOne = new JPanel(new GridLayout(7,1));
        JPanel panelTwo = new JPanel(new GridLayout(7,1));
        /*-------------------------------------------------------------*/
        JLabel labelOne = new JLabel("Ширина сетки", SwingConstants.CENTER);
        JTextField widthField = new JTextField();
        widthField.setText(String.valueOf(constants.gridWidth));
        panelOne.add(labelOne);
        panelTwo.add(widthField);
        /*-------------------------------------------------------------*/
        JLabel labelTwo = new JLabel("Высота сетки", SwingConstants.CENTER);
        JTextField heightField = new JTextField();
        heightField.setText(String.valueOf(constants.gridHeight));
        panelOne.add(labelTwo);
        panelTwo.add(heightField);
        /*-------------------------------------------------------------*/
        JLabel labelThree = new JLabel("A", SwingConstants.CENTER);
        JTextField AField = new JTextField();
        AField.setText(String.valueOf(constants.A));
        panelOne.add(labelThree);
        panelTwo.add(AField);
        /*-------------------------------------------------------------*/
        JLabel labelFour = new JLabel("B", SwingConstants.CENTER);
        JTextField BField = new JTextField();
        BField.setText(String.valueOf(constants.B));
        panelOne.add(labelFour);
        panelTwo.add(BField);
        /*-------------------------------------------------------------*/
        JLabel labelFive = new JLabel("C", SwingConstants.CENTER);
        JTextField CField = new JTextField();
        CField.setText(String.valueOf(constants.C));
        panelOne.add(labelFive);
        panelTwo.add(CField);
        /*-------------------------------------------------------------*/
        JLabel labelSix = new JLabel("D", SwingConstants.CENTER);
        JTextField DField = new JTextField();
        DField.setText(String.valueOf(constants.D));
        panelOne.add(labelSix);
        panelTwo.add(DField);
        /*-------------------------------------------------------------*/
        JButton acceptButton = new JButton("Accept");
        panelOne.add(acceptButton);
        /*-------------------------------------------------------------*/
        JButton cancelButton = new JButton("Cancel");
        panelTwo.add(cancelButton);
        /*-------------------------------------------------------------*/
        cancelButton.addActionListener(e -> dialog.dispose());
        acceptButton.addActionListener(e -> {
            try{
                Integer width = Integer.parseInt(widthField.getText());
                Integer height = Integer.parseInt(heightField.getText());
                constants.gridWidth = width;
                constants.gridHeight = height;

                Double A = Double.parseDouble(AField.getText());
                Double B = Double.parseDouble(BField.getText());
                Double C = Double.parseDouble(CField.getText());
                Double D = Double.parseDouble(DField.getText());
                constants.A = A;
                constants.B = B;
                constants.C = C;
                constants.D = D;

                newSettings();

                dialog.dispose();
            } catch (NumberFormatException nfe){}
        });
        /*-------------------------------------------------------------*/
        mainPanel.add(panelOne);
        mainPanel.add(panelTwo);
        dialog.add(mainPanel);
        /*-------------------------------------------------------------*/
        dialog.setVisible(true);
    }

    private void newSettings(){
        this.mainFunction = new MainFunction(constants.A,constants.B,constants.C,constants.D,constants.gridWidth,constants.gridHeight);
        this.constants.makeSegments(mainFunction, legendFunction);
        this.paint = new Paint(this.constants);

        drawMap();
        drawLegend();
//        drawGrid();
        drawValue();
    }

    public static void main(String[] args) {
        Isolines main = new Isolines();
        main.start();
    }
}
