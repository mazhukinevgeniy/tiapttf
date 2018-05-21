package homemade.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class AssetLoader {
    private static final int TRIES_TO_GET_FILE = 5;

    final protected Image getImage(String filename) {
        InputStream input = getClass().getResourceAsStream(filename);

        for (int i = 0; i < TRIES_TO_GET_FILE; i++)
            try {
                return ImageIO.read(input);
            } catch (IOException e) {
                e.printStackTrace();
            }

        throw new RuntimeException("couldn't load asset");
    }

    final protected Image createRotatedCopy(Image image, double angleInRadians) {
        int width = image.getWidth(null),
                height = image.getHeight(null);


        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D graphics = bufferedImage.createGraphics();
        graphics.drawImage(image, 0, 0, null);
        graphics.dispose();

        //buffered image is ready

        double sin = Math.abs(Math.sin(angleInRadians)),
                cos = Math.abs(Math.cos(angleInRadians));


        int newW = (int) Math.floor(width * cos + height * sin),
                newH = (int) Math.floor(height * cos + width * sin);

        BufferedImage rotatedImage = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
        graphics = rotatedImage.createGraphics();

        graphics.translate((newW - width) / 2, (newH - height) / 2);
        graphics.rotate(angleInRadians, width / 2, height / 2);
        graphics.drawRenderedImage(bufferedImage, null);
        graphics.dispose();

        return rotatedImage;
    }

    final protected Image stackSprites(Image[] sprites) {
        assert sprites.length > 1;

        Image base = createRotatedCopy(sprites[0], 0);

        Graphics graphics = base.getGraphics();

        for (int i = 1; i < sprites.length; i++) {
            graphics.drawImage(sprites[i], 0, 0, null);
        }

        return base;
    }
}