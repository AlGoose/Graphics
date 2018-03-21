package filters;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Gamma {
    public BufferedImage makeGamma(BufferedImage image){
        BufferedImage filteredImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        Graphics graphics = filteredImage.createGraphics();
        graphics.drawImage(image,0,0,null);
        graphics.dispose();
        double gamma = 0.1;

        for (int i = 0; i < filteredImage.getHeight(); i++){
            for (int j = 0; j < filteredImage.getWidth(); j++){
                Color color = new Color(image.getRGB(j,i));
                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();

                red = (int)(255 * (Math.pow((double)red / (double)255, gamma)));
                green = (int)(255 * (Math.pow((double)green / (double)256, gamma)));
                blue  = (int)(255 * (Math.pow((double)blue / (double)255, gamma)));

                if (red > 255)
                    red = 255;
                if (green > 255)
                    green = 255;
                if (blue > 255)
                    blue = 255;

                if (red < 0)
                    red = 0;
                if (green < 0)
                    green = 0;
                if (blue < 0)
                    blue = 0;

                color = new Color(red,green,blue);
                filteredImage.setRGB(j,i,color.getRGB());
            }
        }
        return filteredImage;
    }
}
