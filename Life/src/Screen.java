import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class Screen extends JFrame {
    private BufferedImage image;
    private Paint paint;
    private JLabel jLabel;

    public Screen() {
        super("LIFE");
        image = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_ARGB);
        paint = new Paint(image);
        image = paint.drawField(10,5,33);

        jLabel = new JLabel(new ImageIcon(image));
        add(jLabel);

        CustomListener listeners = new CustomListener();
        jLabel.addMouseListener(listeners);
        jLabel.addMouseMotionListener(listeners);

        setJMenuBar(addMenu());
        pack();
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
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
        JPanel panelOne = new JPanel(new GridLayout(4,1));
        JPanel panelTwo = new JPanel(new GridLayout(4,1));
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
        JButton button = new JButton("Accept");
        panelOne.add(button);

        mainPanel.add(panelOne);
        mainPanel.add(panelTwo);
        dialog.add(mainPanel);
        dialog.setVisible(true);

        button.addActionListener(e -> {
            int height = Integer.parseInt(heightField.getText());
            int width = Integer.parseInt(widthField.getText());
            int radius = Integer.parseInt(radiusField.getText());

            remove(jLabel);
            repaint();

            image = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_ARGB);
            paint = new Paint(image);
            image = paint.drawField(width,height,radius);

            jLabel = new JLabel(new ImageIcon(image));
            add(jLabel);

            CustomListener listeners = new CustomListener();
            jLabel.addMouseListener(listeners);
            jLabel.addMouseMotionListener(listeners);
            revalidate();
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

    public class CustomListener extends MouseAdapter{
        @Override
        public void mouseClicked(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            Color color = Color.RED;
            image =  paint.fillHexagon(x,y,color);
            repaint();
        }

        @Override
        public void mouseDragged(MouseEvent e){
            int x = e.getX();
            int y = e.getY();
            Color color = Color.RED;
            image = paint.fillHexagon(x,y,color);
            repaint();
        }
    }

    public static void main(String[] args) {
        new Screen();
    }
}
