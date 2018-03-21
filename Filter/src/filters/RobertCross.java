package filters;

import java.awt.*;
import java.awt.image.BufferedImage;

public class RobertCross {
    public BufferedImage makeRobertCross(BufferedImage image){
        BufferedImage filteredImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        Graphics graphics = filteredImage.createGraphics();
        graphics.drawImage(image,0,0,null);
        graphics.dispose();
        GreyShades grey = new GreyShades();
        image = grey.makeGreyShades(image);

        for (int i = 1; i < filteredImage.getHeight() - 1; i++){
            for (int j = 0; j < filteredImage.getWidth() - 1; j++){
                Color color1 = new Color(image.getRGB(j,i));
                Color color2 = new Color(image.getRGB(j+1,i+1));
                Color color3 = new Color(image.getRGB(j+1,i));
                Color color4 = new Color(image.getRGB(j,i-1));

                int r1 = color1.getRed() - color2.getRed();
                int r2 = color3.getRed() - color4.getRed();
                int r = (int)Math.sqrt(r2 * r2 + r1 * r1);

                if (r < 0)
                    r = 0;
                if (r > 255)
                    r = 255;

                Color color = new Color(r,r,r);
                filteredImage.setRGB(j,i,color.getRGB());
            }
        }
        return filteredImage;
    }
}
