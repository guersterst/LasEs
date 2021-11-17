package de.lases.global.transport;

/**
 * Encapsulates a file that is passed between the layers.
 */
public class File {

    private byte[] file;

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
}
