package filters;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Rotate {
    public static final int COEFFICIENTS_COUNT = 4;
    private double[] rotationMatrix;

    public Rotate(int angle) {
        double angleInRadians = angle * Math.PI / 180;
        rotationMatrix = new double[COEFFICIENTS_COUNT];
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

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int sourcePixelX = centerX + (int) ((i - centerX) * rotationMatrix[0] + (j - centerY) * rotationMatrix[1]);
                int sourcePixelY = centerY + (int) ((i - centerX) * rotationMatrix[2] + (j - centerY) * rotationMatrix[3]);

                int sourceRGB;
                if (sourcePixelX < 0 || sourcePixelX > width - 1 || sourcePixelY < 0 || sourcePixelY > height - 1) {
                    sourceRGB = Color.WHITE.getRGB();
                } else {
                    sourceRGB = image.getRGB(sourcePixelX, sourcePixelY);
                }
                filteredImage.setRGB(i, j, sourceRGB);
            }
        }

        return filteredImage;
    }
}
