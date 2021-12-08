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

    private static final int IMAGE_WIDTH = 100;

    private static final int IMAGE_HEIGHT = 100;

    private static final Logger logger = Logger.getLogger(AvatarUtil.class.getName());

    /**
     * Generates a small thumbnail from a given image.
     *
     * @param file The {@link FileDTO} containing an image as a byte array.
     * @return The image converted into a thumbnail.
     * @throws IOException If the creation of an image and its scaling has failed.
     */
    public static FileDTO generateThumbnail(FileDTO file) throws IOException {
        if (file.getFile() == null) {
            throw new InvalidFieldsException();
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        FileDTO thumbnail = new FileDTO();
        try {

            // Create image instance, scale and write to an output stream.
            Image scaledImg = getImageFromByteArray(file).getScaledInstance(IMAGE_WIDTH, IMAGE_HEIGHT,
                    BufferedImage.SCALE_SMOOTH);
            ImageIO.write((BufferedImage) scaledImg, "jpg", bos);
        } catch (IOException e) {
            logger.severe("An error in writing or reading an image has occurred.");
            throw new IOException();
        }

        thumbnail.setFile(bos.toByteArray());
        return thumbnail;
    }

    /**
     * Converts the byte array contained in the file to an {@code Image}.
     *
     * @param file The file containing a byte array.
     * @return The byte array as an {@code Image}.
     * @throws IOException If the conversion fails.
     */
    private static BufferedImage getImageFromByteArray(FileDTO file) throws IOException {
        InputStream byteStream = new ByteArrayInputStream(file.getFile());
        return ImageIO.read(byteStream);
    }

}
