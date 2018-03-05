import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Parser {
    private static final int NEED_LOAD = -1;
    private Integer WIDTH;
    private Integer HEIGHT;
    private Integer RADIUS;
    private Integer FAT;
    private ArrayList<Point> MASSIVE;
    private File file;

    public Parser(File file){
        MASSIVE = new ArrayList<>();
        this.file = file;
    }

    public void readFile(){
        ArrayList<Point> arrayList = new ArrayList<>();
        int width = -1;
        int height = -1;
        int radius = -1;
        int widthLine = -1;
        int sizeList = -1;
        try (FileReader reader = new FileReader(file);
             Scanner scanner = new Scanner(reader)) {
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                if (width == NEED_LOAD) {
                    Point size = parseTwo(line);
                    if(size != null) {
                        height = size.y;
                        width = size.x;
                    }
                } else {
                    if (widthLine == NEED_LOAD) {
                        widthLine = parseOne(line);
                    } else{
                        if(radius == NEED_LOAD){
                            radius = parseOne(line);
                        } else{
                            if(sizeList == NEED_LOAD) {
                                sizeList = parseOne(line);
                            } else{
                                Point point = parseTwo(line);
                                if(point != null) {
                                    arrayList.add(point);
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(width < 1 || height < 1 || widthLine < 1 || radius < 1){
            System.out.println("EXCEPTION");
            System.exit(0);
        }

        for(Point point: arrayList){
            if(point.x >= width || point.y >= height){
                System.out.println("EXCEPTION");
                System.exit(0);
            }
        }

        WIDTH = width;
        HEIGHT = height;
        FAT = widthLine;
        RADIUS = radius;
        MASSIVE = arrayList;
    }

    private Point parseTwo(String line){
        int index = line.indexOf("//");
        int len;
        if (index != -1) {
            len = index;
        } else {
            len = line.length();
        }
        boolean wasChar = false;
        StringBuilder stringBuilder = new StringBuilder();
        Point point = new Point();
        point.x = NEED_LOAD;
        point.y = NEED_LOAD;
        for (int i = 0; i < len; ++i) {
            if (Character.isDigit(line.charAt(i))) {
                stringBuilder.append(line.charAt(i));
            } else {
                if (line.charAt(i) != ' ') {
                    System.out.println("EXCEPTION");
                    System.exit(0);
                }

                if (stringBuilder.length() != 0) {
                    wasChar = true;
                    if (point.x == NEED_LOAD) {
                        point.x = Integer.parseInt(stringBuilder.toString());
                        if (point.x < 0) {
                            System.out.println("EXCEPTION");
                            System.exit(0);
                        }
                    } else {
                        if (point.y == NEED_LOAD) {
                            point.y = Integer.parseInt(stringBuilder.toString());
                            if (point.y < 0) {
                                System.out.println("EXCEPTION");
                                System.exit(0);
                            }
                        } else {
                            System.out.println("EXCEPTION");
                            System.exit(0);
                        }
                    }
                    stringBuilder = new StringBuilder();
                }
            }
        }

        if(point.y == NEED_LOAD && stringBuilder.length() != 0){
            point.y = Integer.parseInt(stringBuilder.toString());
            if (point.y < 0) {
                System.out.println("EXCEPTION");
                System.exit(0);
            }
        }
        if ((point.x == NEED_LOAD || point.y == NEED_LOAD) && wasChar) {
            System.out.println("EXCEPTION");
            System.exit(0);
            return null;
        } else {
            if (!wasChar){
                return null;
            } else{
                return point;
            }
        }
    }

    private int parseOne(String line){
        int index = line.indexOf("//");
        int len;
        if (index != -1) {
            len = index;
        } else {
            len = line.length();
        }
        boolean wasChar = false;
        int result = NEED_LOAD;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < len; ++i) {
            if (Character.isDigit(line.charAt(i))) {
                stringBuilder.append(line.charAt(i));
            } else {
                if (line.charAt(i) != ' '){
                    System.out.println("EXCEPTION");
                    System.exit(0);
                }
                if (stringBuilder.length() != 0) {
                    wasChar = true;
                    result = Integer.parseInt(stringBuilder.toString());
                    if (result < 0){
                        System.out.println("EXCEPTION");
                        System.exit(0);
                    }
                }
            }
        }

        if(result == NEED_LOAD && stringBuilder.length() != 0){
            result = Integer.parseInt(stringBuilder.toString());
            if (result < 0) {
                System.out.println("EXCEPTION");
                System.exit(0);
                return -1;
            }
        }

        if (result == NEED_LOAD && wasChar) {
            System.out.println("EXCEPTION");
            System.exit(0);
            return -1;
        } else{
            return result;
        }
    }

    public int getWidth(){ return WIDTH; }

    public int getHeight(){ return HEIGHT; }

    public int getRadius(){ return RADIUS; }

    public int getFat(){ return FAT; }

    public ArrayList<Point> getField(){ return MASSIVE; }
}
