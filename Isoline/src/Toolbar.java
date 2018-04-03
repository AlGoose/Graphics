import javax.swing.*;
import java.awt.*;

import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

class Toolbar {
    static Boolean netMode = false;

    static JToolBar Toolbar(){
        JToolBar jToolBar = new JToolBar();
        jToolBar.setFloatable(false);
        /*-------------------------------------------------------------*/
        JButton jButtonNew = new JButton(new ImageIcon("src/icons/new24.png"));
        jButtonNew.setToolTipText("New");
        /*-------------------------------------------------------------*/
        JButton jButtonSetting = new JButton(new ImageIcon("src/icons/settings24.png"));
        jButtonSetting.setToolTipText("Settings");
        /*-------------------------------------------------------------*/
        JButton jButtonMap = new JButton(new ImageIcon("src/icons/map24.png"));
        jButtonMap.setToolTipText("Map Mode");
        /*-------------------------------------------------------------*/
        JButton jButtonInterpolation = new JButton(new ImageIcon("src/icons/brush24.png"));
        jButtonInterpolation.setToolTipText("Interpolation Mode");
        /*-------------------------------------------------------------*/
        JButton jButtonNet = new JButton(new ImageIcon("src/icons/net24.png"));
        jButtonNet.setToolTipText("Net On/Off");
        /*-------------------------------------------------------------*/
        JButton jButtonIsoline = new JButton(new ImageIcon("src/icons/lines24.png"));
        jButtonIsoline.setToolTipText("Isolines On/Off");
        /*-------------------------------------------------------------*/
        JToolBar.Separator separator1 = new JToolBar.Separator(new Dimension(8,8));
        JToolBar.Separator separator2 = new JToolBar.Separator(new Dimension(8,8));
        /*-------------------------------------------------------------*/
        jToolBar.add(jButtonNew);
        jToolBar.add(jButtonSetting);
        jToolBar.add(separator1);
        jToolBar.add(jButtonMap);
        jToolBar.add(jButtonInterpolation);
        jToolBar.add(separator2);
        jToolBar.add(jButtonNet);
        jToolBar.add(jButtonIsoline);
        /*-------------------------------------------------------------*/
        jButtonNet.addActionListener(e -> {
            if(netMode){
                netMode = false;
                Panels.showGrid(false);
            } else {
                netMode = true;
                Panels.showGrid(true);
            }
        });
        jButtonSetting.addActionListener(e -> settingsDialog());
        /*-------------------------------------------------------------*/
        return jToolBar;
    }

    private static void settingsDialog(){
        JDialog dialog = new JDialog(MainWindow.mainFrame,"Settings",true);
        dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        dialog.setSize(200,300);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(MainWindow.mainFrame);
        /*-------------------------------------------------------------*/
        JPanel mainPanel = new JPanel(new GridLayout(1,2));
        JPanel panelOne = new JPanel(new GridLayout(7,1));
        JPanel panelTwo = new JPanel(new GridLayout(7,1));
        /*-------------------------------------------------------------*/
        JLabel labelOne = new JLabel("Ширина сетки", SwingConstants.CENTER);
        JTextField widthField = new JTextField();
        widthField.setText(String.valueOf(Constants.gridWidth));
        panelOne.add(labelOne);
        panelTwo.add(widthField);
        /*-------------------------------------------------------------*/
        JLabel labelTwo = new JLabel("Высота сетки", SwingConstants.CENTER);
        JTextField heightField = new JTextField();
        heightField.setText(String.valueOf(Constants.gridHeight));
        panelOne.add(labelTwo);
        panelTwo.add(heightField);
        /*-------------------------------------------------------------*/
        JLabel labelThree = new JLabel("A", SwingConstants.CENTER);
        JTextField AField = new JTextField();
        AField.setText(String.valueOf(Constants.A));
        panelOne.add(labelThree);
        panelTwo.add(AField);
        /*-------------------------------------------------------------*/
        JLabel labelFour = new JLabel("B", SwingConstants.CENTER);
        JTextField BField = new JTextField();
        BField.setText(String.valueOf(Constants.B));
        panelOne.add(labelFour);
        panelTwo.add(BField);
        /*-------------------------------------------------------------*/
        JLabel labelFive = new JLabel("C", SwingConstants.CENTER);
        JTextField CField = new JTextField();
        CField.setText(String.valueOf(Constants.C));
        panelOne.add(labelFive);
        panelTwo.add(CField);
        /*-------------------------------------------------------------*/
        JLabel labelSix = new JLabel("D", SwingConstants.CENTER);
        JTextField DField = new JTextField();
        DField.setText(String.valueOf(Constants.D));
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
                Panels.createGrid(width, height);
                Constants.gridWidth = width;
                Constants.gridHeight = height;

                Double A = Double.parseDouble(AField.getText());
                Double B = Double.parseDouble(BField.getText());
                Double C = Double.parseDouble(CField.getText());
                Double D = Double.parseDouble(DField.getText());
                Constants.A = A;
                Constants.B = B;
                Constants.C = C;
                Constants.D = D;

                Panels.newFunction();

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
}
