import java.awt.*;
import java.awt.image.BufferedImage;

class Paint {
    BufferedImage drawMap(MainFunction function, BufferedImage map){
        int width = map.getWidth();
        int height = map.getHeight();

        double deltaX = (function.getB() - function.getA())/width;
        double deltaY = (function.getD() - function.getC())/height;
        double startX;
        double startY = function.getC();
        Constants.Constants(function);

        for (int y = 0; y < map.getHeight(); y++){
            startX = function.getA();
            for(int x = 0; x < map.getWidth(); x++){
                double z = function.findValue(startX,startY);
                for(int i=0; i<5; i++){
                    if(Constants.segment[i] <= z && z <= Constants.segment[i+1]){
                        map.setRGB(x,y,Constants.colors[i].getRGB());
                    }
                }
                startX += deltaX;
            }
            startY += deltaY;
        }
        return  map;
    }

    BufferedImage drawGrid(int width, int height, BufferedImage image){
        Graphics g = image.getGraphics();
        g.setColor(Color.BLACK);

        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        int gridWidth = width;
        int gridHeight = height;

        double deltaX = imageWidth / (gridWidth);
        double deltaY = imageHeight / (gridHeight);

        for (int i = 0; i < imageWidth; i += deltaX) {
            if (i > imageWidth - deltaX) {
                g.drawLine(imageWidth - 1, 0, imageWidth - 1, imageHeight - 1);
            } else {
                g.drawLine(i, 0, i, imageHeight - 1);
            }
        }

        for (int j = 0; j < imageHeight; j += deltaY) {
            if (j > imageHeight - deltaY) {
                g.drawLine(0, imageHeight - 1, imageWidth - 1, imageHeight - 1);
            } else {
                g.drawLine(0, j, imageWidth - 1, j);
            }
        }

        return image;
    }

    BufferedImage drawLegend(BufferedImage image){
        for (int y = 0; y < image.getHeight(); y++){
            for(int x = 0; x < image.getWidth(); x++){
                for(int i=0; i<5; i++){
                    if(Constants.legendSegment[i] <= y && y <= Constants.legendSegment[i+1]){
                        image.setRGB(x,y,Constants.colors[4 - i].getRGB());
                    }
                }
            }
        }
        return  image;
    }
}
