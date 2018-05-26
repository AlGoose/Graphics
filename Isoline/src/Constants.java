import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.Scanner;

import static java.lang.System.exit;

class Constants {
    public int segment[];
    public double legendSegment[];
    public Color[] colors;
    public int number;
    public int gridWidth;
    public int gridHeight;
    public double A = 0.;
    public double B = 10.;
    public double C = 0.;
    public double D = 10.;

    public Constants(){}

    public void makeSegments(MainFunction mainFunction, LegendFunction legendFunction){
        segment = new int[number + 1];
        legendSegment = new double[number + 1];

        int deltaMain = (int)((mainFunction.getMax() - mainFunction.getMin()) / number);
        double deltaLegend = legendFunction.getMax() - legendFunction.getMin() / number;

        double mainRes = mainFunction.getMin();
        double legendRes = legendFunction.getMin();

        segment[0] = (int)mainRes;
        legendSegment[0] = legendRes;
        for(int i = 1; i<number; i++){
            legendRes = legendRes + deltaLegend;
            mainRes = mainRes + deltaMain;
            segment[i] = (int)mainRes;
            legendSegment[i] = legendRes;
        }
        segment[number] = (int)mainFunction.getMax();
        legendSegment[number] = legendFunction.getMax();
    }

    public void readFile(JFrame mainFrame) {
        JFileChooser fileChooser = new JFileChooser("src/configs");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Txt file(.txt)", "txt");
        fileChooser.setFileFilter(filter);
        int res = fileChooser.showDialog(mainFrame,"File Open");

        if (res == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            Scanner scanner;
            try {
                scanner = new Scanner(file);
                int k = scanner.nextInt();
                gridWidth = k;

                int m = scanner.nextInt();
                gridHeight = m;

                int n = scanner.nextInt();
                number = n;

                colors = new Color[n];
                for(int i=0; i<n; i++){
                    int red = scanner.nextInt();
                    int green = scanner.nextInt();
                    int blue = scanner.nextInt();
                    Color color = new Color(red,green,blue);
                    colors[i] = color;
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        } else {
            exit(0);
        }
    }
}
