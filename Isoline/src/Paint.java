import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

class Paint {
    private Constants constants;
    private MainFunction mainFunction;
    private BufferedImage image;
    private ArrayList<int []> isolines;
    private ArrayList<int []> dynamicIsolines;
    private Boolean mode = true;

    Paint(Constants constants){
        this.constants = constants;
        this.isolines  = new ArrayList<>();
        this.dynamicIsolines = new ArrayList<>();
    }

    BufferedImage drawMap(MainFunction function, BufferedImage map){
        int width = map.getWidth();
        int height = map.getHeight();

        double deltaX = (function.getB() - function.getA())/width;
        double deltaY = (function.getD() - function.getC())/height;

        double startX;
        double startY = function.getC();

        for (int y = 0; y < map.getHeight(); y++){
            startX = function.getA();
            for(int x = 0; x < map.getWidth(); x++){
                double z = function.findValue(startX,startY);
                for(int i=0; i<constants.number; i++){
                    if(constants.segment[i] <= z && z <= constants.segment[i+1]){
                        map.setRGB(x,y,constants.colors[i].getRGB());
                    }
                }
                startX += deltaX;
            }
            startY += deltaY;
        }
        return  map;
    }

    BufferedImage drawGrid(BufferedImage image){
        Graphics g = image.getGraphics();
        g.setColor(Color.BLACK);

        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        int gridWidth = constants.gridWidth - 1;
        int gridHeight = constants.gridHeight - 1;

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

    BufferedImage drawLegend(LegendFunction function, BufferedImage legend){
        int height = legend.getHeight();

        double deltaY = (function.getD() - function.getC())/height;
        double startY = function.getC();

        for (int y = 0; y < legend.getHeight(); y++){
            for(int x = 0; x < legend.getWidth(); x++){
                double z = function.findValue(0,startY);
                for(int i=0; i<constants.number; i++){
                    if(constants.legendSegment[i] <= z && z <= constants.legendSegment[i+1]){
                        legend.setRGB(x,y,constants.colors[i].getRGB());
                    }
                }
            }
            startY += deltaY;
        }
        return legend;
    }

    BufferedImage drawValue(BufferedImage image){
        Graphics2D g = image.createGraphics();
        g.setColor(Color.BLACK);
        g.setFont(new Font("TimesRoman", Font.PLAIN,20));
        int deltaValue = image.getHeight() / constants.number;

        for(int i=1, y = deltaValue - 5; i<constants.number; i++, y+= deltaValue){
//            String value = String.valueOf(constants.segment[constants.number - i]);
            String value = String.format("%.2f", constants.segment[constants.number - i]);
            Rectangle bounds = getStringBounds(g, value, 0, 0);
            int a = bounds.width;
            int b = bounds.height;
            g.drawString(value,image.getWidth()/2 - (a+1)/2,y + b);
        }
        return image;
    }

    private Rectangle getStringBounds(Graphics2D g2, String str, float x, float y){
        FontRenderContext frc = g2.getFontRenderContext();
        GlyphVector gv = g2.getFont().createGlyphVector(frc, str);
        return gv.getPixelBounds(null, x, y);
    }

    BufferedImage makeInterpolationMap(MainFunction function, BufferedImage image){
        int width = image.getWidth();
        int height = image.getHeight();

        double deltaX = (function.getB() - function.getA())/width;
        double deltaY = (function.getD() - function.getC())/height;
        double startX;
        double startY = function.getC();

        int index = 0;

        for (int y = 0; y < image.getHeight(); y++){
            startX = function.getA();
            for(int x = 0; x < image.getWidth(); x++){
                double value = function.findValue(startX,startY);

                if (value > constants.segment[constants.segment.length - 1]) {
                    index = constants.segment.length - 1;
                }
                else {
                    for (int i = 1; i < constants.segment.length; i++) {
                        if (value < constants.segment[i]) {
                            index = i;
                            break;
                        }
                    }
                }

                Color colorPrev = (index != 0) ? (constants.colors[index - 1]) : (constants.colors[constants.colors.length - 1]);
                Color colorNext = (index != constants.segment.length - 1) ? (constants.colors[index]) : (constants.colors[index - 1]);

                double valuePrev = (index != 0) ? (constants.segment[index - 1]) : (constants.segment[constants.segment.length - 1]);
                double valueNext = constants.segment[index];

                int red = (int)(colorPrev.getRed() * (valueNext - value)/(valueNext - valuePrev) + colorNext.getRed() * (value - valuePrev)/(valueNext - valuePrev));
                int green = (int)(colorPrev.getGreen() * (valueNext - value)/(valueNext - valuePrev) + colorNext.getGreen() * (value - valuePrev)/(valueNext - valuePrev));
                int blue = (int)(colorPrev.getBlue() * (valueNext - value)/(valueNext - valuePrev) + colorNext.getBlue() * (value - valuePrev)/(valueNext - valuePrev));

                image.setRGB(x,y, new Color(red,green,blue).getRGB());

                startX += deltaX;
            }
            startY += deltaY;
        }
        return image;
    }

    BufferedImage makeInterpolationLegend(LegendFunction function, BufferedImage image){
        int width = image.getWidth();
        int height = image.getHeight();

        double deltaX = (function.getB() - function.getA())/width;
        double deltaY = (function.getD() - function.getC())/height;
        double startX;
        double startY = function.getC();

        int index = 0;

        for (int y = 0; y < image.getHeight(); y++){
            startX = function.getA();
            for(int x = 0; x < image.getWidth(); x++){
                double value = function.findValue(startX,startY);

                if (value > constants.legendSegment[constants.legendSegment.length - 1]) {
                    index = constants.legendSegment.length - 1;
                }
                else {
                    for (int i = 1; i < constants.legendSegment.length; i++) {
                        if (value < constants.legendSegment[i]) {
                            index = i;
                            break;
                        }
                    }
                }

                Color colorPrev = (index != 0) ? (constants.colors[index - 1]) : (constants.colors[constants.colors.length - 1]);
                Color colorNext = (index != constants.legendSegment.length - 1) ? (constants.colors[index]) : (constants.colors[index - 1]);

                double valuePrev = (index != 0) ? (constants.legendSegment[index - 1]) : (constants.legendSegment[constants.legendSegment.length - 1]);
                double valueNext = constants.legendSegment[index];

                int red = (int)(colorPrev.getRed() * (valueNext - value)/(valueNext - valuePrev) + colorNext.getRed() * (value - valuePrev)/(valueNext - valuePrev));
                int green = (int)(colorPrev.getGreen() * (valueNext - value)/(valueNext - valuePrev) + colorNext.getGreen() * (value - valuePrev)/(valueNext - valuePrev));
                int blue = (int)(colorPrev.getBlue() * (valueNext - value)/(valueNext - valuePrev) + colorNext.getBlue() * (value - valuePrev)/(valueNext - valuePrev));

                image.setRGB(x,y, new Color(red,green,blue).getRGB());

                startX += deltaX;
            }
            startY += deltaY;
        }
        return image;
    }

    BufferedImage drawIsoline(MainFunction function, BufferedImage image){
        this.mainFunction = function;
        this.image = image;

        for (int k = 1; k < constants.segment.length - 1; k++) {
            paintIsoline(constants.segment[k]);
        }
        return this.image;
    }

    BufferedImage drawIso(MainFunction function, BufferedImage image, double levelValue){
        this.mainFunction = function;
        this.image = image;

        mode = false;
        paintIsoline(levelValue);
        mode = true;

        return image;
    }

    private void paintIsoline(double levelValue){
        Graphics g = image.getGraphics();

        Color isolineColor = Color.black;
        g.setColor(isolineColor);

        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        int gridWidth = constants.gridWidth - 1;
        int gridHeight = constants.gridHeight - 1;

        double deltaX = imageWidth / gridWidth;
        double deltaY = imageHeight / gridHeight;

        // 0 - up, 1 - down, 2 - left, 3 - right
        boolean [] isCrossedBorder = new boolean[4];
        int [] crossingPoints = new int[4];
        int count;

        for (int y = 0; y < imageHeight; y += deltaY){
            if (imageHeight - y < deltaY){
                break;
            }

            for (int x = 0; x < imageWidth; x += deltaX){
                if (imageWidth - x < deltaX){
                    break;
                }

                count = countCrossedBorders(isCrossedBorder, crossingPoints, levelValue, x, y);

                switch (count){
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

                        if (isCrossedBorder[1]){
                            pointsX[index] = crossingPoints[1];
                            pointsY[index] = (int) Math.round(y + deltaY);

                            index++;
                        }

                        if (isCrossedBorder[2]){
                            pointsX[index] = x;
                            pointsY[index] = crossingPoints[2];

                            index++;
                        }

                        if (isCrossedBorder[3]){
                            pointsX[index] = (int) Math.round(x + deltaX);
                            pointsY[index] = crossingPoints[3];
                        }

                        for (int i = 0; i < pointsX.length; i++){
                            if (pointsX[i] >= imageWidth) {
                                pointsX[i] = imageWidth - 1;
                            }

                            if (pointsY[i] >= imageHeight) {
                                pointsY[i] = imageHeight - 1;
                            }
                        }

                        if(mode){
                            int[] point = new int[4];

                            point[0] = pointsX[0];
                            point[1] = pointsY[0];

                            point[2] = pointsX[1];
                            point[3] = pointsY[1];

                            this.isolines.add(point);
                        } else {
                            int[] point = new int[4];

                            point[0] = (int)(pointsX[0] / constants.xCoeff);
                            point[1] = (int)(pointsY[0] / constants.yCoeff);

                            point[2] = (int)(pointsX[1] / constants.xCoeff);
                            point[3] = (int)(pointsY[1] / constants.yCoeff);

                            this.isolines.add(point);
                        }

                        g.drawLine(pointsX[0], pointsY[0], pointsX[1], pointsY[1]);
                        break;

                    case 3:
                        boolean stop = false;

                        for (int c = 0; c < 4; c++){
                            if (crossingPoints[c] == -1){
                                stop = true;
                            }
                        }

                        if (stop)
                            break;

                        boolean [] epsBorder = new boolean[4];
                        int[] epsPoints = new int[4];
                        double eps = 0.002;

                        countCrossedBorders(epsBorder, epsPoints, levelValue - eps, x, y);

                        pointsX = new int[3];
                        pointsY = new int[3];

                        index = 0;

                        if (isCrossedBorder[0]){
                            if (!epsBorder[0]){
                                pointsX[2] = crossingPoints[0];
                                pointsY[2] = y;
                            }
                            else{
                                pointsX[index] = crossingPoints[0];
                                pointsY[index] = y;
                                index++;
                            }
                        }

                        if (isCrossedBorder[1]){
                            if (!epsBorder[1]){
                                pointsX[2] = crossingPoints[1];
                                pointsY[2] = (int) Math.round(y + deltaY);
                            }
                            else {
                                pointsX[index] = crossingPoints[1];
                                pointsY[index] = (int) Math.round(y + deltaY);
                                index++;
                            }
                        }

                        if (isCrossedBorder[2]){
                            if (!epsBorder[2]){
                                pointsX[2] = x;
                                pointsY[2] = crossingPoints[2];
                            }
                            else {
                                pointsX[index] = x;
                                pointsY[index] = crossingPoints[2];
                                index++;
                            }
                        }

                        if (isCrossedBorder[3]){
                            if (!epsBorder[3]){
                                pointsX[2] = (int) Math.round(x + deltaX);
                                pointsY[2] = crossingPoints[3];
                            }
                            else {
                                pointsX[index] = (int) Math.round(x + deltaX);
                                pointsY[index] = crossingPoints[3];
                            }
                        }

                       if(mode){
                           int[] point = new int[4];
                           point[0] = pointsX[0];
                           point[1] = pointsY[0];

                           point[2] = pointsX[2];
                           point[3] = pointsY[2];

                           this.isolines.add(point);

                           point[0] = pointsX[1];
                           point[1] = pointsY[1];

                           this.isolines.add(point);
                       } else {
                           int[] point = new int[4];
                           point[0] = (int)(pointsX[0] * constants.xCoeff);
                           point[1] = (int)(pointsY[0] * constants.yCoeff);

                           point[2] = (int)(pointsX[2] * constants.xCoeff);
                           point[3] = (int)(pointsY[2] * constants.yCoeff);

                           this.isolines.add(point);

                           point[0] = (int)(pointsX[1] * constants.xCoeff);
                           point[1] = (int)(pointsY[1] * constants.yCoeff);

                           this.isolines.add(point);
                       }

                        g.drawLine(pointsX[0], pointsY[0], pointsX[2], pointsY[2]);
                        g.drawLine(pointsX[2], pointsY[2], pointsX[1], pointsY[1]);

                    case 4:
                        boolean isCenterBigger = (mainFunction.findValue(x + deltaX / 2,y + deltaY / 2) > levelValue);

                        if (isCenterBigger){
                            if(mode){
                                int[] point = new int[4];

                                point[0] = crossingPoints[0];
                                point[1] = y;

                                point[2] = (int) Math.round(x + deltaX);
                                point[3] = crossingPoints[3];

                                this.isolines.add(point);

                                point[0] = crossingPoints[1];
                                point[1] = (int) Math.round(y + deltaY);

                                point[2] = x;
                                point[3] = crossingPoints[2];

                                this.isolines.add(point);
                            } else {
                                int[] point = new int[4];

                                point[0] = (int)(crossingPoints[0] * constants.xCoeff);
                                point[1] = (int)(y * constants.yCoeff);

                                point[2] = (int)((int) Math.round(x + deltaX) * constants.xCoeff);
                                point[3] = (int)(crossingPoints[3] * constants.yCoeff);

                                this.isolines.add(point);

                                point[0] = (int)(crossingPoints[1] * constants.xCoeff);
                                point[1] = (int)((int) Math.round(y + deltaY) * constants.yCoeff);

                                point[2] = (int)(x * constants.xCoeff);
                                point[3] = (int)(crossingPoints[2] * constants.yCoeff);

                                this.isolines.add(point);
                            }

                            g.drawLine(crossingPoints[0], y, (int) Math.round(x + deltaX), crossingPoints[3]);
                            g.drawLine(crossingPoints[1], (int) Math.round(y + deltaY), x, crossingPoints[2]);
                        } else{
                            if(mode){
                                int[] point = new int[4];
                                point[0] = crossingPoints[0];
                                point[1] = y;

                                point[2] = x;
                                point[3] = crossingPoints[2];

                                this.isolines.add(point);

                                point[0] = crossingPoints[1];
                                point[1] = (int) Math.round(y + deltaY);

                                point[2] = (int) Math.round(x + deltaX);
                                point[3] = crossingPoints[3];

                                this.isolines.add(point);
                            } else {
                                int[] point = new int[4];
                                point[0] = (int)(crossingPoints[0] * constants.xCoeff);
                                point[1] = (int)(y * constants.yCoeff);

                                point[2] = (int)(x * constants.xCoeff);
                                point[3] = (int)(crossingPoints[2] * constants.yCoeff);

                                this.isolines.add(point);

                                point[0] = (int)(crossingPoints[1] * constants.xCoeff);
                                point[1] = (int)((int) Math.round(y + deltaY) * constants.yCoeff);

                                point[2] = (int)((int) Math.round(x + deltaX) * constants.xCoeff);
                                point[3] = (int)(crossingPoints[3] * constants.yCoeff);

                                this.isolines.add(point);
                            }

                            g.drawLine(crossingPoints[0], y, x, crossingPoints[2]);
                            g.drawLine(crossingPoints[1], (int) Math.round(y + deltaY), (int) Math.round(x + deltaX), crossingPoints[3]);
                        }
                        break;

                    default:
                        break;
                }
            }
        }
    }

    private int countCrossedBorders(boolean [] isCrossedBorder, int[] crossingPoints, double levelValue, int x, int y){
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        int gridWidth = constants.gridWidth - 1;
        int gridHeight = constants.gridHeight - 1;

        double deltaX = imageWidth / gridWidth;
        double deltaY = imageHeight / gridHeight;

        int count = 0;

        double domainX = (mainFunction.getB() - mainFunction.getA()) / (imageWidth);
        double domainY = (mainFunction.getD() - mainFunction.getC()) / (imageHeight);

        double valueNW;
        double valueNE;
        double valueSW;
        double valueSE;

        Arrays.fill(isCrossedBorder, true);
        Arrays.fill(crossingPoints, -1);

        valueNW = mainFunction.findValue(x * domainX, y * domainY);
        valueNE = mainFunction.findValue((x + deltaX) * domainX, y * domainY);
        valueSW = mainFunction.findValue(x * domainX, (y + deltaY) * domainY);
        valueSE = mainFunction.findValue((x + deltaX) * domainX, (y + deltaY) * domainY);


        if ((levelValue < valueNW && levelValue < valueNE) || (levelValue > valueNW && levelValue > valueNE)){
            isCrossedBorder[0] = false;
        } else {
            double domain1 = levelValue - valueNW;
            double domain2 = valueNE - valueNW;

            crossingPoints[0] = (int) (x + deltaX * domain1 / domain2);
        }
        if ((levelValue < valueSW && levelValue < valueSE) || (levelValue > valueSW && levelValue > valueSE)){
            isCrossedBorder[1] = false;
        } else {
            double domain1 = levelValue - valueSW;
            double domain2 = valueSE - valueSW;

            crossingPoints[1] = (int) (x + deltaX * domain1 / domain2);
        }

        if ((levelValue < valueNW && levelValue < valueSW) || (levelValue > valueNW && levelValue > valueSW)){
            isCrossedBorder[2] = false;
        } else {
            double domain1 = levelValue - valueSW;
            double domain2 = valueNW - valueSW;

            crossingPoints[2] = (int) (y + deltaY - deltaY * domain1 / domain2);
        }

        if ((levelValue < valueNE && levelValue < valueSE) || (levelValue > valueNE && levelValue > valueSE)){
            isCrossedBorder[3] = false;
        } else {
            double domain1 = levelValue - valueSE;
            double domain2 = valueNE - valueSE;

            crossingPoints[3] = (int) (y + deltaY - deltaY * domain1 / domain2);
        }

        for (boolean value : isCrossedBorder){
            if (value) {
                count++;
            }
        }
        return count;
    }

    BufferedImage drawBufferedIsolines(BufferedImage image, double xCoeff, double yCoeff){
        this.image = image;
        Graphics g = image.getGraphics();
        g.setColor(Color.BLACK);

        for (int i = 0; i < this.isolines.size(); i++ ){
            int[] point = this.isolines.get(i);
            g.drawLine((int)(point[0]*xCoeff),(int)(point[1]*yCoeff),(int)(point[2]*xCoeff),(int)(point[3]*yCoeff));
        }

        if(dynamicIsolines != null){
            for (int i = 0; i < this.dynamicIsolines.size(); i++ ){
                int[] point = this.dynamicIsolines.get(i);
                g.drawLine((int)(point[0]*xCoeff),(int)(point[1]*yCoeff),(int)(point[2]*xCoeff),(int)(point[3]*yCoeff));
            }
        }
        return this.image;
    }

    BufferedImage drawDynamicIsoline(MainFunction function, BufferedImage image, double levelValue){
        this.mainFunction = function;
        this.image = image;

        paintDynamicIsoline(levelValue);

        return image;
    }

    private void paintDynamicIsoline(double levelValue){
        this.dynamicIsolines.clear();
        Graphics g = image.getGraphics();

        Color isolineColor = Color.black;
        g.setColor(isolineColor);

        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        int gridWidth = constants.gridWidth - 1;
        int gridHeight = constants.gridHeight - 1;

        double deltaX = imageWidth / gridWidth;
        double deltaY = imageHeight / gridHeight;

        // 0 - up, 1 - down, 2 - left, 3 - right
        boolean [] isCrossedBorder = new boolean[4];
        int [] crossingPoints = new int[4];
        int count;

        for (int y = 0; y < imageHeight; y += deltaY){
            if (imageHeight - y < deltaY){
                break;
            }

            for (int x = 0; x < imageWidth; x += deltaX){
                if (imageWidth - x < deltaX){
                    break;
                }

                count = countCrossedBorders(isCrossedBorder, crossingPoints, levelValue, x, y);

                switch (count){
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

                        if (isCrossedBorder[1]){
                            pointsX[index] = crossingPoints[1];
                            pointsY[index] = (int) Math.round(y + deltaY);

                            index++;
                        }

                        if (isCrossedBorder[2]){
                            pointsX[index] = x;
                            pointsY[index] = crossingPoints[2];

                            index++;
                        }

                        if (isCrossedBorder[3]){
                            pointsX[index] = (int) Math.round(x + deltaX);
                            pointsY[index] = crossingPoints[3];
                        }

                        for (int i = 0; i < pointsX.length; i++){
                            if (pointsX[i] >= imageWidth) {
                                pointsX[i] = imageWidth - 1;
                            }

                            if (pointsY[i] >= imageHeight) {
                                pointsY[i] = imageHeight - 1;
                            }
                        }

                        int[] point = new int[4];

                        point[0] = (int)(pointsX[0] / constants.xCoeff);
                        point[1] = (int)(pointsY[0] / constants.yCoeff);

                        point[2] = (int)(pointsX[1] / constants.xCoeff);
                        point[3] = (int)(pointsY[1] / constants.yCoeff);

//                        System.out.println("X1: " + point[0]);
//                        System.out.println("Y1: " + point[1]);
//                        System.out.println("X2: " + point[2]);
//                        System.out.println("Y2: " + point[3]);

                        this.dynamicIsolines.add(point);

                        g.drawLine(pointsX[0], pointsY[0], pointsX[1], pointsY[1]);
                        break;

                    case 3:
                        boolean stop = false;

                        for (int c = 0; c < 4; c++){
                            if (crossingPoints[c] == -1){
                                stop = true;
                            }
                        }

                        if (stop)
                            break;

                        boolean [] epsBorder = new boolean[4];
                        int[] epsPoints = new int[4];
                        double eps = 0.002;

                        countCrossedBorders(epsBorder, epsPoints, levelValue - eps, x, y);

                        pointsX = new int[3];
                        pointsY = new int[3];

                        index = 0;

                        if (isCrossedBorder[0]){
                            if (!epsBorder[0]){
                                pointsX[2] = crossingPoints[0];
                                pointsY[2] = y;
                            }
                            else{
                                pointsX[index] = crossingPoints[0];
                                pointsY[index] = y;
                                index++;
                            }
                        }

                        if (isCrossedBorder[1]){
                            if (!epsBorder[1]){
                                pointsX[2] = crossingPoints[1];
                                pointsY[2] = (int) Math.round(y + deltaY);
                            }
                            else {
                                pointsX[index] = crossingPoints[1];
                                pointsY[index] = (int) Math.round(y + deltaY);
                                index++;
                            }
                        }

                        if (isCrossedBorder[2]){
                            if (!epsBorder[2]){
                                pointsX[2] = x;
                                pointsY[2] = crossingPoints[2];
                            }
                            else {
                                pointsX[index] = x;
                                pointsY[index] = crossingPoints[2];
                                index++;
                            }
                        }

                        if (isCrossedBorder[3]){
                            if (!epsBorder[3]){
                                pointsX[2] = (int) Math.round(x + deltaX);
                                pointsY[2] = crossingPoints[3];
                            }
                            else {
                                pointsX[index] = (int) Math.round(x + deltaX);
                                pointsY[index] = crossingPoints[3];
                            }
                        }


                        point = new int[4];
                        point[0] = (int)(pointsX[0] / constants.xCoeff);
                        point[1] = (int)(pointsY[0] / constants.yCoeff);

                        point[2] = (int)(pointsX[2] / constants.xCoeff);
                        point[3] = (int)(pointsY[2] / constants.yCoeff);

                        this.dynamicIsolines.add(point);

                        point[0] = (int)(pointsX[1] / constants.xCoeff);
                        point[1] = (int)(pointsY[1] / constants.yCoeff);

                        this.dynamicIsolines.add(point);

                        g.drawLine(pointsX[0], pointsY[0], pointsX[2], pointsY[2]);
                        g.drawLine(pointsX[2], pointsY[2], pointsX[1], pointsY[1]);

                    case 4:
                        boolean isCenterBigger = (mainFunction.findValue(x + deltaX / 2,y + deltaY / 2) > levelValue);

                        if (isCenterBigger){
                            point = new int[4];

                            point[0] = (int)(crossingPoints[0] * constants.xCoeff);
                            point[1] = (int)(y * constants.yCoeff);

                            point[2] = (int)((int) Math.round(x + deltaX) * constants.xCoeff);
                            point[3] = (int)(crossingPoints[3] * constants.yCoeff) ;

                            this.dynamicIsolines.add(point);

                            point[0] = (int)(crossingPoints[1] * constants.xCoeff);
                            point[1] = (int)((int) Math.round(y + deltaY) * constants.yCoeff);

                            point[2] = (int)(x * constants.xCoeff);
                            point[3] = (int)(crossingPoints[2] * constants.yCoeff);

                            this.dynamicIsolines.add(point);

                            g.drawLine(crossingPoints[0], y, (int) Math.round(x + deltaX), crossingPoints[3]);
                            g.drawLine(crossingPoints[1], (int) Math.round(y + deltaY), x, crossingPoints[2]);
                        } else{
                            point = new int[4];
                            point[0] = (int)(crossingPoints[0] * constants.xCoeff);
                            point[1] = (int)(y * constants.yCoeff);

                            point[2] = (int)(x * constants.xCoeff);
                            point[3] = (int)(crossingPoints[2] * constants.yCoeff);

                            this.dynamicIsolines.add(point);

                            point[0] = (int)(crossingPoints[1] * constants.xCoeff);
                            point[1] = (int)((int) Math.round(y + deltaY) * constants.yCoeff);

                            point[2] = (int)((int) Math.round(x + deltaX) * constants.xCoeff);
                            point[3] = (int)(crossingPoints[3] * constants.yCoeff);

                            this.dynamicIsolines.add(point);

                            g.drawLine(crossingPoints[0], y, x, crossingPoints[2]);
                            g.drawLine(crossingPoints[1], (int) Math.round(y + deltaY), (int) Math.round(x + deltaX), crossingPoints[3]);
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public BufferedImage drawCircles(BufferedImage picture, double xCoeff, double yCoeff){
        Graphics g = picture.getGraphics();
        g.setColor(Color.BLACK);

        for (int i = 0; i < isolines.size(); i++ ) {
            int[] point = isolines.get(i);
            g.drawOval((int)(point[0]*xCoeff) - 2, (int)(point[1]*yCoeff) - 2, 4, 4);
            g.drawOval((int)(point[2]*xCoeff) - 2, (int)(point[3]*yCoeff) - 2, 4, 4);
        }
        return picture;
    }
}
