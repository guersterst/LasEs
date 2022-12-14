package de.lases.business.util;

import de.lases.global.transport.FileDTO;
import de.lases.persistence.exception.InvalidFieldsException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.logging.Logger;

/**
 * Provides functionality to generate a small thumbnail to be used in user
 * avatars and the applications logo.
 */
public final class AvatarUtil {

    private static final int IMAGE_WIDTH = 300;

    private static final Logger logger = Logger.getLogger(AvatarUtil.class.getName());

    /**
     * Generates a small thumbnail from a given image.
     *
     * @param imageFile The {@link FileDTO} containing an image as a byte array.
     * @return The image converted into a thumbnail.
     * @throws IOException If the creation of an image or its scaling has failed.
     */
    public static FileDTO generateThumbnail(FileDTO imageFile) throws IOException {
        if (imageFile.getFile() == null) {
            throw new InvalidFieldsException();
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        FileDTO thumbnail = new FileDTO();
        try {

            // Create image instance, scale and write to an output stream.
            BufferedImage bufferedImage = castToBufferedImageFromFileDTO(imageFile);
            int imageHeight = (int) (bufferedImage.getHeight() * ((double) IMAGE_WIDTH / bufferedImage.getWidth()));
            Image scaledImg = bufferedImage.getScaledInstance(IMAGE_WIDTH, imageHeight,
                    BufferedImage.SCALE_SMOOTH);
            ImageIO.write(castToBufferedImageFromImage(scaledImg), "jpg", bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            logger.severe("An error in writing or reading an image has occurred.");
            throw new IOException();
        }

        thumbnail.setFile(bos.toByteArray());
        return thumbnail;
    }

    /**
     * Converts the byte array contained in the file to an {@code BufferedImage}.
     *
     * @param file The file containing a byte array.
     * @return The byte array as an {@code BufferedImage}.
     * @throws IOException If the conversion fails.
     */
    private static BufferedImage castToBufferedImageFromFileDTO(FileDTO file) throws IOException {
        InputStream byteStream = new ByteArrayInputStream(file.getFile());
        return ImageIO.read(byteStream);
    }

    /**
     * Converts the {@code Image} to a {@code BufferedImage}.
     *
     * @param img The image to be converted.
     * @return The {@code Image} as a {@code BufferedImage}.
     */
    private static BufferedImage castToBufferedImageFromImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency.
        BufferedImage bImage = new BufferedImage(img.getWidth(null), img.getHeight(null),
                BufferedImage.TYPE_INT_RGB);

        // Draw the image onto the buffered image.
        Graphics2D bGr = bImage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        return bImage;
    }
}
