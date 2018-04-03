import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

class Panels {
    static JLayeredPane mapPanel;
    static JPanel legendPanel;
    static JPanel valuePanel;
    static JPanel gridPanel;
    static MainFunction function;
    static Paint paint;

    static JPanel Panel() {
        JPanel mainPanel = new JPanel();
        Container container = new Container();
        container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));

        Constants.readFile();
        function = new MainFunction(0,10,0,10,Constants.gridWidth,Constants.gridHeight);
        paint = new Paint();

        mapPanel = new JLayeredPane();
        mapPanel.setPreferredSize(new Dimension(600,600));
        drawMap();

        valuePanel = new JPanel();
        valuePanel.setLayout(null);
        valuePanel.setPreferredSize(new Dimension(60,600));
        drawValues();

        legendPanel = new JPanel();
        legendPanel.setLayout(new BorderLayout());
        legendPanel.setPreferredSize(new Dimension(100,600));
        drawLegend();

        mainPanel.add(mapPanel,container);
        mainPanel.add(valuePanel,container);
        mainPanel.add(legendPanel,container);

        createGrid(Constants.gridWidth,Constants.gridHeight);

        return mainPanel;
    }

    static void createGrid(int width, int height){
        if(gridPanel != null){
            gridPanel.removeAll();
            gridPanel.revalidate();
        }

        gridPanel = new JPanel(new BorderLayout());
        gridPanel.setPreferredSize(mapPanel.getSize());
        gridPanel.setOpaque(false);

        BufferedImage image = new BufferedImage(600,600,BufferedImage.TYPE_INT_ARGB);
        Paint paint = new Paint();
        image = paint.drawGrid(width, height, image);
        JLabel test = new JLabel(new ImageIcon(image));
        gridPanel.add(test);
        gridPanel.setBounds(0,0,600,600);
        mapPanel.add(gridPanel, JLayeredPane.DRAG_LAYER);
        gridPanel.setVisible(false);

        if(Toolbar.netMode){
            gridPanel.setVisible(true);
        }
    }

    static void showGrid(Boolean mode){
        if(mode){
            gridPanel.setVisible(true);
        } else {
            gridPanel.setVisible(false);
        }
    }

    static void drawValues(){
        if(valuePanel != null){
            valuePanel.removeAll();
        }

        for(int i=1, y = 115; i<5; i++, y+= 120){
            String value = String.valueOf(Constants.segment[5 - i]);
            JLabel tmp = new JLabel(value);
            tmp.setBounds(20,y,60,10);
            valuePanel.add(tmp);
        }
        valuePanel.repaint();
        valuePanel.revalidate();
    }

    static void drawMap(){
        if(mapPanel != null){
            mapPanel.removeAll();
        }

        BufferedImage map = new BufferedImage(600,600,BufferedImage.TYPE_INT_ARGB);
        map = paint.drawMap(function,map);
        JLabel test = new JLabel(new ImageIcon(map));
        test.setBounds(0,0,map.getWidth(), map.getHeight());
        mapPanel.add(test,JLayeredPane.DEFAULT_LAYER);
    }

    static void drawLegend(){
        BufferedImage legend = new BufferedImage(100,600,BufferedImage.TYPE_INT_ARGB);
        legend = paint.drawLegend(legend);
        JLabel test2 = new JLabel(new ImageIcon(legend));
        test2.setBounds(0,0,legend.getWidth(), legend.getHeight());
        legendPanel.add(test2);
    }

    static void newFunction(){
        function = new MainFunction(Constants.A,Constants.B,Constants.C,Constants.D,Constants.gridWidth,Constants.gridHeight);
        drawMap();
        drawValues();
    }
}
