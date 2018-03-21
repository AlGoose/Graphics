package filters;

import java.awt.*;
import java.awt.image.BufferedImage;

public class GreyShades {
    public BufferedImage makeGreyShades(BufferedImage image){
        BufferedImage filteredImage = image;

        for (int i = 0; i < filteredImage.getHeight(); i++){
            for (int j = 0; j < filteredImage.getWidth(); j++){
                Color color = new Color(image.getRGB(j,i));
                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();

                int y = (int)(((0.333) * red +
                        (0.333) * green +
                        (0.333) * blue) / (0.999));

                if (y < 0)
                    y = 0;

                if (y > 255)
                    y = 255;

                color = new Color(y,y,y);
                filteredImage.setRGB(j,i,color.getRGB());
            }
        }

        return filteredImage;
    }
}
