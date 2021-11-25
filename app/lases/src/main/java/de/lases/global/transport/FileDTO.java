package de.lases.global.transport;

import java.io.InputStream;

/**
 * Encapsulates a file that is passed between the layers.
 */
public class FileDTO {

    private byte[] file;

    private InputStream inputStream;

    public byte[] getFile() {
        return file;
    }

    /**
     * Set the file as a byte array.
     *
     * @param file The file.
     */
    public void setFile(byte[] file) {
        this.file = file;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    /**
     * Sets the file as an {@code InputStream}.
     *
     * @param inputStream The file as an {@code InputStream}.
     */
    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }
}
