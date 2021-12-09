package de.lases.business.util;

import de.lases.global.transport.FileDTO;
import de.lases.persistence.exception.DataNotWrittenException;
import de.lases.persistence.exception.InvalidFieldsException;
import de.lases.persistence.repository.ConnectionPool;
import de.lases.persistence.repository.SystemSettingsRepository;
import de.lases.persistence.repository.Transaction;

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

    private static final int IMAGE_WIDTH = 150;

    private static final int IMAGE_HEIGHT = 150;

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
            Image scaledImg = getBufferedImageFromByteArray(imageFile).getScaledInstance(IMAGE_WIDTH, IMAGE_HEIGHT,
                    BufferedImage.SCALE_SMOOTH);
            ImageIO.write(castToBufferedImage(scaledImg), "jpg", bos);
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
     * Converts the byte array contained in the file to an {@code Image}.
     *
     * @param file The file containing a byte array.
     * @return The byte array as an {@code Image}.
     * @throws IOException If the conversion fails.
     */
    private static BufferedImage getBufferedImageFromByteArray(FileDTO file) throws IOException {
        InputStream byteStream = new ByteArrayInputStream(file.getFile());
        return ImageIO.read(byteStream);
    }

    private static BufferedImage castToBufferedImage(Image img) {
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

    //TODO REMOVE OR TURN INTO TESTS
    public static void main(String[] args) throws IOException, DataNotWrittenException {

        //TEST AvatarUtil
        /*
        // Get image.
        BufferedImage img = ImageIO.read(AvatarUtil.class.getClassLoader().getResource("face.jpg"));

        // Create fileDTO.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(img, "jpg", baos);
        FileDTO fileDTO = new FileDTO();
        fileDTO.setFile(baos.toByteArray());

        // Write the image.
        FileDTO thumbnail = generateThumbnail(fileDTO);
        BufferedImage bufferedThumbnail = getBufferedImageFromByteArray(thumbnail);
        File outputFile = new File("thumbnail.jpg");
        ImageIO.write(bufferedThumbnail, "jpg", outputFile);
         */


        //TEST setLogo
        ConnectionPool.init();

        // Get image.
        BufferedImage img = ImageIO.read(AvatarUtil.class.getClassLoader().getResource("face.jpg"));

        // Create fileDTO.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(img, "jpg", baos);
        FileDTO fileDTO = new FileDTO();
        fileDTO = generateThumbnail(fileDTO);
        fileDTO.setFile(baos.toByteArray());

        Transaction transaction = new Transaction();
        SystemSettingsRepository.setLogo(fileDTO, transaction);
        transaction.commit();
    }

}
