import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.Scanner;

import static java.lang.System.exit;

class Constants {
    static int segment[];
    static int legendSegment[];
    static Color[] colors;
    static int number;
    static int gridWidth;
    static int gridHeight;
    static double A = 0.;
    static double B = 10.;
    static double C = 0.;
    static double D = 10.;

    static void Constants(MainFunction function){
        segment = new int[number + 1];
        legendSegment = new int[number + 1];


        double min = function.getMin();
        double max = function.getMax();
        int delta = (int)((max - min) / number);
        double res = min;
        int legendRes = 0;

        segment[0] = (int)res;
        legendSegment[0] = 0;
        for(int i = 1; i<number; i++){
            legendRes = legendRes + 120;
            res = res + delta;
            segment[i] = (int)res;
            legendSegment[i] = legendRes;
        }
        segment[number] = (int)max;
        legendSegment[number] = 600;
    }

    static void readFile() {
        JFileChooser fileChooser = new JFileChooser("src");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Txt file(.txt)", "txt");
        fileChooser.setFileFilter(filter);
        int res = fileChooser.showDialog(MainWindow.mainFrame,"File Open");

        if (res == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            Scanner scanner = null;
            try {
                scanner = new Scanner(file);
                int k = scanner.nextInt();
                gridWidth = k;
//                System.out.println(k);
                int m = scanner.nextInt();
                gridHeight = m;
//                System.out.println(m);
                int n = scanner.nextInt();
                number = n;
//                System.out.println(n);
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
