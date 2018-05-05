package filters;

import java.awt.*;
import java.awt.image.BufferedImage;

public class OrderDithering{
    private static final int[][] BAYER_16_16 = new int[16][16];
    static {
        int[][] BATER_8_8 =
                {
                    {0, 48, 12, 60, 3, 51, 15, 63},
                    {32, 16, 44, 28, 35, 19, 47, 31},
                    {8, 56, 4, 52, 11, 59, 7, 55},
                    {40, 24, 36, 20, 43, 27, 39, 23},
                    {2, 50, 14, 62, 1, 49, 13, 61},
                    {34, 18, 46, 30, 33, 17, 45, 29},
                    {10, 58, 6, 54, 9, 57, 5, 53},
                    {42, 26, 38, 22, 41, 25, 37, 21}
                };
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                BAYER_16_16[i][j] = BATER_8_8[i][j] * 4;
            }
        }
        for (int i = 8; i < 16; i++){
            for (int j = 0; j < 8; j++){
                BAYER_16_16[i][j] = BATER_8_8[i % 8][j] * 4 + 2;
            }
        }
        for (int i = 0; i < 8; i++){
            for (int j = 8; j < 16; j++){
                BAYER_16_16[i][j] = BATER_8_8[i][j % 8] * 4 + 3;
            }
        }
        for (int i = 8; i < 16; i++){
            for (int j = 8; j < 16; j++){
                BAYER_16_16[i][j] = BATER_8_8[i % 8][j % 8] * 4 + 1;
            }
        }
    }


    public BufferedImage makeOrderDither(BufferedImage image){
        BufferedImage filteredImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

        for (int i = 0; i < filteredImage.getHeight(); i++){
            for (int j = 0; j < filteredImage.getWidth(); j++){
                Color color = new Color(image.getRGB(j,i));
                Color newColor = findNewColor(color, i ,j);
                filteredImage.setRGB(j,i,newColor.getRGB());
            }
        }
        return filteredImage;
    }

    private Color findNewColor(Color color, int i, int j){
        int newRed, newGreen, newBlue;
        if (color.getRed() > BAYER_16_16[i % 16][j % 16]) newRed = 255;
        else newRed = 0;

        if (color.getGreen() > BAYER_16_16[i % 8][j % 8]) newGreen = 255;
        else newGreen = 0;

        if (color.getBlue() > BAYER_16_16[i % 8][j % 8]) newBlue = 255;
        else newBlue = 0;

        return new Color(newRed, newGreen, newBlue);
    }
}
