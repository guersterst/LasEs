package de.lases.persistence.repository;

import java.util.List;

import de.lases.global.transport.*;
import de.lases.persistence.exception.*;

/**
 * Offers get/add/change/remove operations on a paper and the possibility to
 * get lists of papers.
 */
public class PaperRepository {

    /**
     * Takes a paper dto that is filled out with a valid id and submission id
     * and returns a fully filled paper dto.
     *
     * @param paper A paper dto that must be filled with a valid id.
     * @return A fully filled paper dto.
     * @throws NotFoundException If there is no paper with the provided id and
     *                           submission id.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static Paper get(Paper paper) throws NotFoundException {
        return null;
    }

    /**
     * Adds a paper to the repository.
     *
     * @param paper A fully filled paper dto.
     *
     * @throws DataNotWrittenException If writing the data to the repository
     *                                 fails.
     * @throws KeyExistsException If there is already a paper with the same id
     *                            and submission id in the repository.
     * @throws InvalidFieldsException If one of the fields of the paper is
     *                                null.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static void add(Paper paper) throws DataNotWrittenException,
            KeyExistsException {
    }

    /**
     * Changes the given paper in the repository.
     *
     * @param paper A fully filled paper dto.
     * @throws NotFoundException If there is no paper with the provided id and
     *                           submission id.
     * @throws DataNotWrittenException If writing the data to the repository
     *                                 fails.
     * @throws InvalidFieldsException If one of the fields of the paper is null.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static void change(Paper paper) throws NotFoundException,
            DataNotWrittenException {
    }

    /**
     * Takes a paper dto that is filled with a valid id and submission id and
     * removes this paper from the repository.
     *
     * @param paper The paper to remove. Must be filled with a valid id and
     *              submission id.
     * @throws NotFoundException The specified paper was not found in the
     *                           repository.
     * @throws DataNotWrittenException If writing the data to the repository
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static void remove(Paper paper) throws NotFoundException,
            DataNotWrittenException {
    }

    /**
     * Gets a list all papers that belong to the specified submission.
     *
     * @param submission A submission dto filled with a valid id.
     * @return A list of fully filled paper dtos for all papers that belong
     *         to the specified submission.
     * @throws DataNotCompleteException If the list is truncated.
     * @throws NotFoundException If there is no submission with the provided id.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static List<Paper> getList(Submission submission)
            throws DataNotCompleteException, NotFoundException {
        return null;
    }

    /**
     * Get the PDF file for the provided paper.
     *
     * @param paper A paper dto filled with a valid paper id and submission id.
     * @return A file containing the PDF for the specified paper.
     * @throws NotFoundException If there is no paper with the provided id and
     *                           submission id.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static File getPDF(Paper paper) throws NotFoundException {
        return null;
    }

    /**
     * Sets the PDF belonging to a specified paper.
     *
     * @param paper A paper dto filled with a valid paper id and submission id.
     * @param pdf A file dto filled with a pdf file.
     * @throws DataNotWrittenException If writing the data to the repository
     *                                 fails.
     * @throws NotFoundException If there is no paper with the provided id and
     *                           submission id.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static void setPDF(Paper paper, File pdf)
            throws DataNotWrittenException, NotFoundException {
    }

}
