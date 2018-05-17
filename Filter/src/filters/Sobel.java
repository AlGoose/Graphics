package filters;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Sobel {
    public BufferedImage makeSobel(BufferedImage image, int KOF) {
        BufferedImage filteredImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        Graphics graphics = filteredImage.createGraphics();
        graphics.drawImage(image, 0, 0, null);
        graphics.dispose();
        GreyShades grey = new GreyShades();
        image = grey.makeGreyShades(image);

        for (int i = 1; i < filteredImage.getHeight() - 1; i++) {
            for (int j = 1; j < filteredImage.getWidth() - 1; j++) {
                Color colorE = new Color(image.getRGB(j, i));
                Color colorA = new Color(image.getRGB(j - 1, i - 1));
                Color colorB = new Color(image.getRGB(j, i - 1));
                Color colorC = new Color(image.getRGB(j + 1, i - 1));
                Color colorD = new Color(image.getRGB(j - 1, i));
                Color colorF = new Color(image.getRGB(j + 1, i));
                Color colorG = new Color(image.getRGB(j - 1, i + 1));
                Color colorH = new Color(image.getRGB(j, i + 1));
                Color colorI = new Color(image.getRGB(j + 1, i + 1));

                int sx = (colorC.getRed() + 2 * colorF.getRed() + colorI.getRed()) - (colorA.getRed() + 2 * colorD.getRed() + colorG.getRed());
                int sy = (colorG.getRed() + 2 * colorH.getRed() + colorI.getRed()) - (colorA.getRed() + 2 * colorB.getRed() + colorC.getRed());
                int r = (int) Math.sqrt(sx * sx + sy * sy);

                if (r > KOF) {
                    r = 255;
                } else {
                    r = 0;
                }

                Color color = new Color(r, r, r);
                filteredImage.setRGB(j, i, color.getRGB());
            }
        }
        return filteredImage;
    }
}
