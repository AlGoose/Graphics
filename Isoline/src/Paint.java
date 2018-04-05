import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

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
                for(int i=0; i<Constants.number; i++){
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
                for(int i=0; i<Constants.number; i++){
                    if(Constants.legendSegment[i] <= y && y <= Constants.legendSegment[i+1]){
                        image.setRGB(x,y,Constants.colors[(Constants.number - 1) - i].getRGB());
                    }
                }
            }
        }
        return  image;
    }

    BufferedImage paintIsolines(MainFunction function, BufferedImage image){
        for (int k = 1; k < Constants.segment.length - 1; k++) {
            paintIsoline(Constants.segment[k], function, image);
            System.out.println(Constants.segment[k]);
        }
        return image;
    }

    private BufferedImage paintIsoline(double levelValue, MainFunction function, BufferedImage image){
//        BufferedImage image = image2;
        Graphics g = image.getGraphics();
        Color isolineColor = Color.BLACK;
        g.setColor(isolineColor);

        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        int gridWidth = Constants.gridWidth - 1;
        int gridHeight = Constants.gridHeight - 1;

        double deltaX = imageWidth / gridWidth;
        double deltaY = imageHeight / gridHeight;

        boolean [] isCrossedBorder = new boolean[4];
        int [] crossingPoints = new int[4];
        int count;

        for (int y = 0; y < imageHeight; y += deltaY) {
            if (imageHeight - y < deltaY) {
                break;
            }

            for (int x = 0; x < imageWidth; x += deltaX) {
                if (imageWidth - x < deltaX) {
                    break;
                }

                count = countCrossedBorders(function, image, isCrossedBorder, crossingPoints, levelValue, x, y);

                switch (count) {
                    case 0:
                        break;
                    case 2:
                        int[] pointsX = new int[2];
                        int[] pointsY = new int[2];
                        int index = 0;

                        if (isCrossedBorder[0]) {
                            pointsX[index] = crossingPoints[0];
                            pointsY[index] = y;

                            index++;
                        }

                        if (isCrossedBorder[1]) {
                            pointsX[index] = crossingPoints[1];
                            pointsY[index] = (int) Math.round(y + deltaY);

                            index++;
                        }

                        if (isCrossedBorder[2]) {
                            pointsX[index] = x;
                            pointsY[index] = crossingPoints[2];

                            index++;
                        }

                        if (isCrossedBorder[3]) {
                            pointsX[index] = (int) Math.round(x + deltaX);
                            pointsY[index] = crossingPoints[3];
                        }

                        for (int i = 0; i < pointsX.length; i++) {
                            if (pointsX[i] >= imageWidth) {
                                pointsX[i] = imageWidth - 1;
                            }

                            if (pointsY[i] >= imageHeight) {
                                pointsY[i] = imageHeight - 1;
                            }
                        }
                        g.drawLine(pointsX[0], pointsY[0], pointsX[1], pointsY[1]);
                        break;

                    case 3:
                        boolean stop = false;

                        for (int c = 0; c < 4; c++) {
                            if (crossingPoints[c] == -1) {
                                stop = true;
                            }
                        }

                        if (stop)
                            break;

                        boolean [] epsBorder = new boolean[4];
                        int[] epsPoints = new int[4];
                        double eps = 0.002;

                        countCrossedBorders(function, image, epsBorder, epsPoints, levelValue - eps, x, y);

                        pointsX = new int[3];
                        pointsY = new int[3];

                        index = 0;

                        if (isCrossedBorder[0]) {
                            if (!epsBorder[0]) {
                                pointsX[2] = crossingPoints[0];
                                pointsY[2] = y;
                            }
                            else {
                                pointsX[index] = crossingPoints[0];
                                pointsY[index] = y;
                                index++;
                            }
                        }

                        if (isCrossedBorder[1]) {
                            if (!epsBorder[1]) {
                                pointsX[2] = crossingPoints[1];
                                pointsY[2] = (int) Math.round(y + deltaY);
                            }
                            else {
                                pointsX[index] = crossingPoints[1];
                                pointsY[index] = (int) Math.round(y + deltaY);
                                index++;
                            }
                        }

                        if (isCrossedBorder[2]) {
                            if (!epsBorder[2]) {
                                pointsX[2] = x;
                                pointsY[2] = crossingPoints[2];
                            }
                            else {
                                pointsX[index] = x;
                                pointsY[index] = crossingPoints[2];
                                index++;
                            }
                        }

                        if (isCrossedBorder[3]) {
                            if (!epsBorder[3]) {
                                pointsX[2] = (int) Math.round(x + deltaX);
                                pointsY[2] = crossingPoints[3];
                            }
                            else {
                                pointsX[index] = (int) Math.round(x + deltaX);
                                pointsY[index] = crossingPoints[3];
                            }
                        }

                        g.drawLine(pointsX[0], pointsY[0], pointsX[2], pointsY[2]);
                        g.drawLine(pointsX[2], pointsY[2], pointsX[1], pointsY[1]);

                    case 4:
                        boolean isCenterBigger = (function.findValue(x + deltaX / 2,y + deltaY / 2) > levelValue);

                        if (isCenterBigger) {
                            g.drawLine(crossingPoints[0], y, (int) Math.round(x + deltaX), crossingPoints[3]);
                            g.drawLine(crossingPoints[1], (int) Math.round(y + deltaY), x, crossingPoints[2]);
                        } else {
                            g.drawLine(crossingPoints[0], y, x, crossingPoints[2]);
                            g.drawLine(crossingPoints[1], (int) Math.round(y + deltaY), (int) Math.round(x + deltaX), crossingPoints[3]);
                        }
                        break;
                    default:
                        break;
                }

            }
        }
        return image;
    }

    private int countCrossedBorders(MainFunction function, BufferedImage image, boolean [] isCrossedBorder, int[] crossingPoints, double levelValue, int x, int y){
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        int gridWidth = Constants.gridWidth - 1;
        int gridHeight = Constants.gridHeight - 1;

        double deltaX = imageWidth / gridWidth;
        double deltaY = imageHeight / gridHeight;

        int count = 0;

        double domainX = (function.getB() - function.getA()) / (imageWidth);
        double domainY = (function.getD() - function.getC()) / (imageHeight);

        double valueNW;
        double valueNE;
        double valueSW;
        double valueSE;

        Arrays.fill(isCrossedBorder, true);
        Arrays.fill(crossingPoints, -1);

        valueNW = function.findValue(x * domainX, y * domainY);
        valueNE = function.findValue((x + deltaX) * domainX, y * domainY);
        valueSW = function.findValue(x * domainX, (y + deltaY) * domainY);
        valueSE = function.findValue((x + deltaX) * domainX, (y + deltaY) * domainY);

        if ((levelValue < valueNW && levelValue < valueNE) || (levelValue > valueNW && levelValue > valueNE)) {
            isCrossedBorder[0] = false;
        } else {
            double domain1 = levelValue - valueNW;
            double domain2 = valueNE - valueNW;

            crossingPoints[0] = (int) (x + deltaX * domain1 / domain2);
        }
        if ((levelValue < valueSW && levelValue < valueSE) || (levelValue > valueSW && levelValue > valueSE)) {
            isCrossedBorder[1] = false;
        } else {
            double domain1 = levelValue - valueSW;
            double domain2 = valueSE - valueSW;

            crossingPoints[1] = (int) (x + deltaX * domain1 / domain2);
        }

        if ((levelValue < valueNW && levelValue < valueSW) || (levelValue > valueNW && levelValue > valueSW)) {
            isCrossedBorder[2] = false;
        } else {
            double domain1 = levelValue - valueSW;
            double domain2 = valueNW - valueSW;

            crossingPoints[2] = (int) (y + deltaY - deltaY * domain1 / domain2);
        }
        if ((levelValue < valueNE && levelValue < valueSE) || (levelValue > valueNE && levelValue > valueSE)) {
            isCrossedBorder[3] = false;
        } else {
            double domain1 = levelValue - valueSE;
            double domain2 = valueNE - valueSE;

            crossingPoints[3] = (int) (y + deltaY - deltaY * domain1 / domain2);
        }

        for (boolean value : isCrossedBorder) {
            if (value) {
                count++;
            }
        }
        return count;
    }
}
