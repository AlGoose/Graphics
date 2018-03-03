import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Parser {
    private Integer width;
    private Integer height;
    private Integer radius;
    private Integer fat;
    private ArrayList<Integer> xArr;
    private ArrayList<Integer> yArr;
    private HashMap<ArrayList<Integer>, ArrayList<Integer>> map;
    Scanner scanner;

    public Parser(File file) throws FileNotFoundException {
        map = new HashMap<>();
        xArr = new ArrayList<>();
        yArr = new ArrayList<>();
        scanner = new Scanner(file);
    }

    public void readFile(){
        int fromIndex = 0;
        int index;
        String number;
        try {
            String str = scanner.nextLine();
            for (int i = 0; i < 2; i++) {
                index = str.indexOf(" ", fromIndex);
                if (index == -1) {
                    number = str.substring(fromIndex);
                    height = Integer.parseInt(number);
//                    System.out.println(height);
                } else {
                    if(i == 1){
                        number = str.substring(fromIndex, index);
                        height = Integer.parseInt(number);
                    }
                    number = str.substring(fromIndex, index);
                    width = Integer.parseInt(number);
//                    System.out.println(width);
                    fromIndex = index + 1;
                }
            }

            number = nextNumber();
            fat = Integer.parseInt(number);
//            System.out.println(fat);

            number = nextNumber();
            radius = Integer.parseInt(number);
//            System.out.println(radius);

            number = nextNumber();
            int count = Integer.parseInt(number);
//            System.out.println(count);

            for (int i = 0; i < count; i++) {
                fromIndex = 0;
                int x, y;
                str = scanner.nextLine();
//                System.out.println(">>>" + str);
                for (int j = 0; j < 2; j++) {
                    index = str.indexOf(" ", fromIndex);
                    if (index == -1) {
                        number = str.substring(fromIndex);
                        y = Integer.parseInt(number);
                        yArr.add(y);
                        System.out.println(y);
                    } else {
                        if(j == 1){
                            number = str.substring(fromIndex, index);
                            y = Integer.parseInt(number);
                            yArr.add(y);
                            System.out.println(y);
                        }
                        number = str.substring(fromIndex, index);
                        x = Integer.parseInt(number);
                        xArr.add(x);
                        System.out.println(x);
                        fromIndex = index + 1;
                    }
                }
            }
            map.put(xArr, yArr);
        } catch (NumberFormatException nfe){
            JOptionPane.showMessageDialog(null, "Wrong file data!");
        }
    }

    private String nextNumber(){
        String str = scanner.nextLine();
        int fromIndex = 0;
        int index;
        String number;

        index = str.indexOf(" ", fromIndex);
        if(index == -1){
            number = str.substring(fromIndex);
        } else {
            number = str.substring(fromIndex,index);
        }
        return number;
    }

    public int getWidth(){ return width; }

    public int getHeight(){ return height; }

    public int getRadius(){ return radius; }

    public int getFat(){ return fat; }

    public HashMap<ArrayList<Integer>, ArrayList<Integer>> getField(){ return map; }
}
