package filters;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Negative{
    public BufferedImage makeNegative(BufferedImage image){
        BufferedImage filteredImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        Graphics g = filteredImage.createGraphics();
        g.drawImage(image,0,0,null);
        g.dispose();

        for (int i = 0; i < filteredImage.getHeight(); i++) {
            for (int j = 0; j < filteredImage.getWidth(); j++) {
                Color color = new Color(image.getRGB(j,i));
                int red = 255 - color.getRed();
                int green = 255 - color.getGreen();
                int blue = 255 - color.getBlue();
                color = new Color(red,green,blue);
                filteredImage.setRGB(j,i,color.getRGB());
            }
        }
        return filteredImage;
    }
}