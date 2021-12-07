package de.lases.persistence.repository;

import java.sql.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.lases.global.transport.*;
import de.lases.persistence.exception.*;
import de.lases.persistence.util.DatasourceUtil;
import org.postgresql.util.PSQLException;

/**
 * Offers get/add/change/remove operations on a paper and the possibility to
 * get lists of papers.
 */
public class PaperRepository {

    private static final Logger logger
            = Logger.getLogger(PaperRepository.class.getName());

    /**
     * Takes a paper dto that is filled out with a valid id and submission id
     * and returns a fully filled paper dto.
     *
     * @param paper       A paper dto that must be filled with a valid id.
     * @param transaction The transaction to use.
     * @return A fully filled paper dto.
     * @throws NotFoundException              If there is no paper with the provided id and
     *                                        submission id.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static Paper get(Paper paper, Transaction transaction)
            throws NotFoundException {
        return null;
    }

    /**
     * Adds a paper to the repository.
     *
     * @param paper       A fully filled paper dto. (The id must not be
     *                    specified, as the repository will create the id)
     * @param transaction The transaction to use.
     * @throws DataNotWrittenException        If writing the data to the repository
     *                                        fails.
     *                                        and submission id in the repository.
     * @throws InvalidFieldsException         If one of the fields of the paper is
     *                                        null.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static void add(Paper paper, Transaction transaction)
            throws DataNotWrittenException {
        Connection conn = transaction.getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    """
                INSERT INTO paper
                VALUES (?, ?, ?, ?, ?)
                """);
            stmt.setInt(1, paper.getVersionNumber());
            stmt.setInt(2, paper.getSubmissionId());
            stmt.setTimestamp(3, Timestamp.valueOf(paper.getUploadTime()));
            stmt.setBoolean(4, paper.isVisible());
            // TODO: sollte hier null möglich sein?, oder sollte ich meine Schnittstelle ändern?
            stmt.setBytes(5, new byte[]{});
            stmt.executeUpdate();
        } catch (SQLException ex) {
            if (ex instanceof SQLNonTransientException) {
                logger.log(Level.SEVERE, "Non transient");
            } else if (ex instanceof SQLTransientException) {
                logger.log(Level.SEVERE, "Transient");
            } else if (ex instanceof SQLRecoverableException) {
                logger.log(Level.SEVERE, "Recoverable");
            } else if (ex instanceof PSQLException) {
                logger.log(Level.SEVERE, "PSQLExeption");
            }
            DatasourceUtil.logSQLException(ex, logger);
        }
    }

    /**
     * Changes the given paper in the repository.
     *
     * @param paper       A fully filled paper dto.
     * @param transaction The transaction to use.
     * @throws NotFoundException              If there is no paper with the provided id and
     *                                        submission id.
     * @throws DataNotWrittenException        If writing the data to the repository
     *                                        fails.
     * @throws InvalidFieldsException         If one of the fields of the paper is null.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static void change(Paper paper, Transaction transaction)
            throws NotFoundException,
            DataNotWrittenException {
    }

    /**
     * Takes a paper dto that is filled with a valid id and submission id and
     * removes this paper from the repository.
     *
     * @param paper       The paper to remove. Must be filled with a valid id and
     *                    submission id.
     * @param transaction The transaction to use.
     * @throws NotFoundException              The specified paper was not found in the
     *                                        repository.
     * @throws DataNotWrittenException        If writing the data to the repository
     *                                        fails.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static void remove(Paper paper, Transaction transaction)
            throws NotFoundException, DataNotWrittenException {
    }

    /**
     * Gets a list all papers that belong to the specified submission.
     * <p>
     * The papers returned are determined by the highest privilege, which that
     * user possesses on that submission.
     * </p>
     *
     * @param submission           A submission dto filled with a valid id.
     * @param transaction          The transaction to use.
     * @param user                 The user who requests the papers.
     * @param resultListParameters The ResultListParameters dto that results
     *                             parameters from the pagination like
     *                             filtering, sorting or number of elements.
     * @return A list of fully filled paper dtos for all papers that belong
     * to the specified submission.
     * @throws DataNotCompleteException       If the list is truncated.
     * @throws NotFoundException              If there is no submission with the provided id.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     * @throws InvalidQueryParamsException    If the resultListParameters contain
     *                                        an erroneous option.
     */
    public static List<Paper> getList(Submission submission,
                                      Transaction transaction,
                                      User user,
                                      ResultListParameters resultListParameters)
            throws DataNotCompleteException, NotFoundException {
        return null;
    }

    /**
     * Get the PDF file for the provided paper.
     *
     * @param paper       A paper dto filled with a valid paper id and submission id.
     * @param transaction The transaction to use.
     * @return A file containing the PDF for the specified paper.
     * @throws NotFoundException              If there is no paper with the provided id and
     *                                        submission id.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static FileDTO getPDF(Paper paper, Transaction transaction)
            throws NotFoundException {
        return null;
    }

    /**
     * Sets the PDF belonging to a specified paper.
     *
     * @param paper       A paper dto filled with a valid paper id and submission id.
     * @param pdf         A file dto filled with a pdf file.
     * @param transaction The transaction to use.
     * @throws DataNotWrittenException        If writing the data to the repository
     *                                        fails.
     * @throws NotFoundException              If there is no paper with the provided id and
     *                                        submission id.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static void setPDF(Paper paper, FileDTO pdf, Transaction transaction)
            throws DataNotWrittenException, NotFoundException {
    }

    /**
     * Takes a submission dto that is filled out with a valid id
     * and returns a fully filled paper dto for the newest paper in that
     * submission.
     *
     * @param submission  A submission dto that must be filled with a valid id.
     * @param user       The user who requests the papers.
     * @param transaction The transaction to use.
     * @return A fully filled paper dto.
     * @throws NotFoundException              If there is no submission with the provided id.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public Paper getNewestPaperForSubmission(Submission submission,
                                             User user,
                                             Transaction transaction)
            throws NotFoundException {
        return null;
    }

}
