package filters;

import java.awt.*;
import java.awt.image.BufferedImage;

public class FloydSteinberg{
    private int countRed;
    private int countGreen;
    private int countBlue;
    private int[] redPalette;
    private int[] greenPalette;
    private int[] bluePalette;

    public FloydSteinberg(int countRed, int countGreen, int countBlue){
        this.countRed = countRed;
        this.countGreen = countGreen;
        this.countBlue = countBlue;

        redPalette = new int[countRed];
        greenPalette = new int[countGreen];
        bluePalette = new int[countBlue];
    }

    public BufferedImage makeFloydSteinberg(BufferedImage image){
        BufferedImage filteredImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        for(int i = 0; i < filteredImage.getHeight(); i++){
            for(int j = 0; j < filteredImage.getWidth(); j++){
                filteredImage.setRGB(j,i,image.getRGB(j,i));
            }
        }

        for (int i = 0; i < countRed; ++i) {
            redPalette[i] = (256 / (countRed - 1)) * i;
        }
        redPalette[countRed - 1] = 256;

        for (int i = 0; i < countGreen; ++i) {
            greenPalette[i] = (256 / (countGreen - 1)) * i;
        }
        greenPalette[countGreen - 1] = 256;

        for (int i = 0; i < countBlue; ++i) {
            bluePalette[i] = (256 / (countBlue - 1)) * i;
        }
        bluePalette[countBlue - 1] = 256;

        for (int i = 0; i < filteredImage.getHeight(); i++){
            for (int j = 0; j < filteredImage.getWidth(); j++){
//                Color color0 = new Color(image.getRGB(j,i));
                Color color0 = new Color(filteredImage.getRGB(j,i));
                int red0 = color0.getRed();
                int green0 = color0.getGreen();
                int blue0 = color0.getBlue();

                Color newColor = findDef(red0,green0,blue0);
                int newRed = newColor.getRed();
                int newGreen = newColor.getGreen();
                int newBlue = newColor.getBlue();

                Color color = new Color(newRed,newGreen,newBlue);
                filteredImage.setRGB(j,i,color.getRGB());
//                image.setRGB(j,i,color.getRGB());

                int errRed = red0 - newRed;
                int errGreen = green0 - newGreen;
                int errBlue = blue0 - newBlue;

                if (j + 1 < filteredImage.getWidth()) {
//                    Color color1 = new Color(image.getRGB(j+1,i));
                    Color color1 = new Color(filteredImage.getRGB(j+1,i));
                    int red1 = color1.getRed();
                    int green1 = color1.getGreen();
                    int blue1 = color1.getBlue();

                    red1 += (int)(errRed * 7.0 / 16);
                    green1 += (int)(errGreen * 7.0 / 16);
                    blue1 += (int)(errBlue * 7.0 / 16);

                    if(red1 > 255) red1 = 255;
                    if(red1 <= 0) red1 = 0;
                    if(green1 > 255) green1 = 255;
                    if(green1 <= 0) green1 = 0;
                    if(blue1 > 255) blue1 = 255;
                    if(blue1 <= 0) blue1 = 0;

                    color = new Color(red1,green1,blue1);
                    filteredImage.setRGB(j+1,i,color.getRGB());
//                    image.setRGB(j+1,i,color.getRGB());
                }

                if (i + 1 < filteredImage.getHeight() && j - 1 > 0) {
//                    Color color2 = new Color(image.getRGB(j-1,i+1));
                    Color color2 = new Color(filteredImage.getRGB(j-1,i+1));
                    int red2 = color2.getRed();
                    int green2 = color2.getGreen();
                    int blue2 = color2.getBlue();

                    red2 += (int)(errRed * 3.0 / 16);
                    green2 += (int)(errGreen * 3.0 / 16);
                    blue2 += (int)(errBlue * 3.0 / 16);

                    if(red2 > 255) red2 = 255;
                    if(red2 <= 0) red2 = 0;
                    if(green2 > 255) green2 = 255;
                    if(green2 <= 0) green2 = 0;
                    if(blue2 > 255) blue2 = 255;
                    if(blue2 <= 0) blue2 = 0;

                    color = new Color(red2,green2,blue2);
                    filteredImage.setRGB(j-1,i+1,color.getRGB());
//                    image.setRGB(j-1,i+1,color.getRGB());
                }

                if (i + 1 < filteredImage.getHeight()) {
//                    Color color3 = new Color(image.getRGB(j,i+1));
                    Color color3 = new Color(filteredImage.getRGB(j,i+1));
                    int red3 = color3.getRed();
                    int green3 = color3.getGreen();
                    int blue3 = color3.getBlue();

                    red3 += (int)(errRed * 5.0 / 16);
                    green3 += (int)(errGreen * 5.0 / 16);
                    blue3 += (int)(errBlue * 5.0 / 16);

                    if(red3 > 255) red3 = 255;
                    if(red3 <= 0) red3 = 0;
                    if(green3 > 255) green3 = 255;
                    if(green3 <= 0) green3 = 0;
                    if(blue3 > 255) blue3 = 255;
                    if(blue3 <= 0) blue3 = 0;

                    color = new Color(red3,green3,blue3);
                    filteredImage.setRGB(j,i+1,color.getRGB());
//                    image.setRGB(j,i+1,color.getRGB());
                }

                if (i + 1 < filteredImage.getHeight() && j + 1 < filteredImage.getWidth()) {
//                    Color color4 = new Color(image.getRGB(j+1,i+1));
                    Color color4 = new Color(filteredImage.getRGB(j+1,i+1));
                    int red4 = color4.getRed();
                    int green4 = color4.getGreen();
                    int blue4 = color4.getBlue();

                    red4 += (int)(errRed * 1.0 / 16);
                    green4 += (int)(errGreen * 1.0 / 16);
                    blue4 += (int)(errBlue * 1.0 / 16);

                    if(red4 > 255) red4 = 255;
                    if(red4 <= 0) red4 = 0;
                    if(green4 > 255) green4 = 255;
                    if(green4 <= 0) green4 = 0;
                    if(blue4 > 255) blue4 = 255;
                    if(blue4 <= 0) blue4 = 0;

                    color = new Color(red4,green4,blue4);
                    filteredImage.setRGB(j+1,i+1,color.getRGB());
//                    image.setRGB(j+1,i+1,color.getRGB());
                }
            }
        }
        return filteredImage;
//        return image;
    }

    private Color findDef(int red, int green, int blue) {
        int maxDif = 0x00FFFFFF;
        int min = 0;
        for (int i = 0; i < redPalette.length; i++) {
            if (Math.abs(redPalette[i] - red) < maxDif) {
                maxDif = Math.abs(redPalette[i] - red);
                min = i;
            }
        }
        int newRed = redPalette[min];
        maxDif = 0x00FFFFFF;
        min = 0;
        for (int i = 0; i < greenPalette.length; i++) {
            if (Math.abs(greenPalette[i] - green) < maxDif) {
                maxDif = Math.abs(greenPalette[i] - green);
                min = i;
            }
        }
        int newGreen = greenPalette[min];
        maxDif = 0x00FFFFFF;
        min = 0;
        for (int i = 0; i < bluePalette.length; i++) {
            if (Math.abs(bluePalette[i] - blue) < maxDif) {
                maxDif = Math.abs(bluePalette[i] - blue);
                min = i;
            }
        }
        int newBlue = bluePalette[min];

        if(newRed > 255) newRed = 255;
        if(newRed <= 0) newRed = 0;
        if(newGreen > 255) newGreen = 255;
        if(newGreen <= 0) newGreen = 0;
        if(newBlue > 255) newBlue = 255;
        if(newBlue <= 0) newBlue = 0;

        return new Color(newRed, newGreen, newBlue);
    }
}
