package filters;

import java.awt.*;
import java.awt.image.BufferedImage;

public class GaussBlur{
    public BufferedImage makeGaussBlur(BufferedImage image){
        BufferedImage filteredImage = image;

        double[][] core = {{0.5 / 6, 0.75 / 6, 0.5 / 6},
                {0.75 / 6, 1.0 / 6, 0.75 / 6},
                {0.5 / 6, 0.75 / 6, 0.5 / 6}};

        for (int i = 1; i < filteredImage.getHeight() - 1; i++){
            for (int j = 1; j < filteredImage.getWidth() - 1; j++){

                Color color1 = new Color(image.getRGB(j-1,i-1));
                Color color2 = new Color(image.getRGB(j,i-1));
                Color color3 = new Color(image.getRGB(j+1,i-1));
                Color color4 = new Color(image.getRGB(j-1,i));
                Color color5 = new Color(image.getRGB(j,i));
                Color color6 = new Color(image.getRGB(j+1,i));
                Color color7 = new Color(image.getRGB(j-1,i+1));
                Color color8 = new Color(image.getRGB(j,i+1));
                Color color9 = new Color(image.getRGB(j+1,i+1));

                int red1 = color1.getRed();
                int red2 = color2.getRed();
                int red3 = color3.getRed();
                int red4 = color4.getRed();
                int red5 = color5.getRed();
                int red6 = color6.getRed();
                int red7 = color7.getRed();
                int red8 = color8.getRed();
                int red9 = color9.getRed();

                int green1 = color1.getGreen();
                int green2 = color2.getGreen();
                int green3 = color3.getGreen();
                int green4 = color4.getGreen();
                int green5 = color5.getGreen();
                int green6 = color6.getGreen();
                int green7 = color7.getGreen();
                int green8 = color8.getGreen();
                int green9 = color9.getGreen();

                int blue1 = color1.getBlue();
                int blue2 = color2.getBlue();
                int blue3 = color3.getBlue();
                int blue4 = color4.getBlue();
                int blue5 = color5.getBlue();
                int blue6 = color6.getBlue();
                int blue7 = color7.getBlue();
                int blue8 = color8.getBlue();
                int blue9 = color9.getBlue();

                int red = (int)(
                        core[0][0] * red1 +
                        core[0][1] * red2 +
                        core[0][2] * red3 +
                        core[1][0] * red4 +
                        core[1][1] * red5 +
                        core[1][2] * red6 +
                        core[2][0] * red7 +
                        core[2][1] * red8 +
                        core[2][2] * red9
                        );

                int green = (int)(
                        core[0][0] * green1 +
                        core[0][1] * green2 +
                        core[0][2] * green3 +
                        core[1][0] * green4 +
                        core[1][1] * green5 +
                        core[1][2] * green6 +
                        core[2][0] * green7 +
                        core[2][1] * green8 +
                        core[2][2] * green9
                );

                int blue = (int)(
                        core[0][0] * blue1 +
                        core[0][1] * blue2 +
                        core[0][2] * blue3 +
                        core[1][0] * blue4 +
                        core[1][1] * blue5 +
                        core[1][2] * blue6 +
                        core[2][0] * blue7 +
                        core[2][1] * blue8 +
                        core[2][2] * blue9
                );


                Color color = new Color(red,green,blue);
                filteredImage.setRGB(j,i,color.getRGB());
            }
        }
        return filteredImage;
    }
}
