package de.lases.business.util;

import de.lases.global.transport.File;

/**
 * Provides functionality to generate a small thumbnail to be used in user avatars and the applications logo.
 */
public final class AvatarUtil {

    private static final int WIDTH = 800;

    private static final int HEIGHT = 800;

    /**
     * Generates a small thumbnail from a given image.
     *
     * @param file The {@link File} containing an image as a byte array.
     * @return The image converted into a thumbnail.
     * @throws IllegalArgumentException Thrown, when the given image does not fulfill the necessary criteria to be
     *                                  converted into a thumbnail (e.g. is smaller than the given {@link AvatarUtil#WIDTH} or {@link AvatarUtil#HEIGHT}.
     */
    public static File generateThumbnail(File file) throws IllegalArgumentException {
        return null;
    }

}
