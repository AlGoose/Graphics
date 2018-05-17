package filters;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Rotate {
    private static final int SIZE = 4;
    private double[] rotationMatrix;
    private int angle;


    public Rotate(int angle) {
        this.angle = angle;
        double angleInRadians = angle * Math.PI / 180;
        rotationMatrix = new double[SIZE];
        rotationMatrix[0] = Math.cos(angleInRadians);
        rotationMatrix[1] = Math.sin(angleInRadians);
        rotationMatrix[2] = -Math.sin(angleInRadians);
        rotationMatrix[3] = Math.cos(angleInRadians);
    }

    public BufferedImage makeRotate(BufferedImage image) {
        BufferedImage filteredImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);

        int width = image.getWidth();
        int height = image.getHeight();

        int centerX = width / 2;
        int centerY = height / 2;

        double tmp = (Math.PI * ((angle % 90) + 45.)) / 180.;
        int newSize = (int)Math.sqrt(Math.pow((175 / Math.tan(tmp) - 175), 2) + Math.pow(350 - Math.abs(175 / Math.tan(tmp) - 175), 2));

        Image newImage = new ImageIcon(image.getScaledInstance(newSize, newSize, BufferedImage.SCALE_DEFAULT)).getImage();

        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for(int k = 0; k < width; k ++) {
            for(int t = 0; t < height; t++) {
                bi.setRGB(k, t, Color.white.getRGB());
            }
        }

        Graphics g2 = bi.createGraphics();
        g2.drawImage(newImage,(width - newSize) / 2,(width - newSize) / 2,null);
        g2.dispose();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int sourcePixelX = centerX + (int) ((i - centerX) * rotationMatrix[0] + (j - centerY) * rotationMatrix[1]);
                int sourcePixelY = centerY + (int) ((i - centerX) * rotationMatrix[2] + (j - centerY) * rotationMatrix[3]);

                int sourceRGB;
                if (sourcePixelX < 0 || sourcePixelX > width - 1 || sourcePixelY < 0 || sourcePixelY > height - 1) {
                    sourceRGB = Color.WHITE.getRGB();
                } else {
                    sourceRGB = bi.getRGB(sourcePixelX, sourcePixelY);
                }
                filteredImage.setRGB(i, j, sourceRGB);
            }
        }

        return filteredImage;
    }
}
