package filters;

import java.awt.*;
import java.awt.image.BufferedImage;

public class DoubleSize {
    public BufferedImage makeDoubleSize(BufferedImage image){
        BufferedImage filteredImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        Graphics graphics = filteredImage.createGraphics();
        graphics.drawImage(image,0,0,null);
        graphics.dispose();

        int height = filteredImage.getHeight();
        int width = filteredImage.getWidth();

        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                int r;
                int g;
                int b;

                if ((i - 1) % 2 == 0 && (j - 1) % 2 == 0){
                    int i1 = width / 4 + (i - 1) / 2;
                    int j1 = height / 4 + (j - 1) / 2;

                    Color color = new Color(image.getRGB(j1,i1));
                    r = color.getRed();
                    g = color.getGreen();
                    b = color.getBlue();
                } else if ((i - 1) % 2 == 0){
                    int i1 = width / 4 + (i - 1) / 2;
                    int j1 = height / 4 + (j - 2) / 2;

                    Color color1 = new Color(image.getRGB(j1,i1));
                    int r1 = color1.getRed();
                    int g1 = color1.getGreen();
                    int b1 = color1.getBlue();
                    Color color2 = new Color(image.getRGB(j1+1,i1));
                    int r2 = color2.getRed();
                    int g2 = color2.getGreen();
                    int b2 = color2.getBlue();

                    r = (r1 + r2) / 2;
                    g = (g1 + g2) / 2;
                    b = (b1 + b2) / 2;
                } else if((j - 1) % 2 == 0){
                    int i1 = width / 4 + (i - 2) / 2;
                    int j1 = height / 4 + (j - 1) / 2;

                    Color color1 = new Color(image.getRGB(j1,i1));
                    int r1 = color1.getRed();
                    int g1 = color1.getGreen();
                    int b1 = color1.getBlue();
                    Color color2 = new Color(image.getRGB(j1,i1+1));
                    int r2 = color2.getRed();
                    int g2 = color2.getGreen();
                    int b2 = color2.getBlue();

                    r = (r1 + r2) / 2;
                    g = (g1 + g2) / 2;
                    b = (b1 + b2) / 2;
                } else {
                    int i1 = width / 4 + (i - 2) / 2;
                    int j1 = height / 4 + (j - 2) / 2;

                    Color color1 = new Color(image.getRGB(j1,i1));
                    int r1 = color1.getRed();
                    int g1 = color1.getGreen();
                    int b1 = color1.getBlue();
                    Color color2 = new Color(image.getRGB(j1+1,i1));
                    int r2 = color2.getRed();
                    int g2 = color2.getGreen();
                    int b2 = color2.getBlue();
                    Color color3 = new Color(image.getRGB(j1,i1+1));
                    int r3 = color3.getRed();
                    int g3 = color3.getGreen();
                    int b3 = color3.getBlue();
                    Color color4 = new Color(image.getRGB(j1+1,i1+1));
                    int r4 = color4.getRed();
                    int g4 = color4.getGreen();
                    int b4 = color4.getBlue();

                    r = (r1 + r2 + r3 + r4) / 4;
                    g = (g1 + g2 + g3 + g4) / 4;
                    b = (b1 + b2 + b3 + b4) / 4;
                }
                Color color = new Color(r,g,b);
                filteredImage.setRGB(j,i,color.getRGB());
            }
        }
        return filteredImage;
    }
}
