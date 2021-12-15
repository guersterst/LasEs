package de.lases.persistence.repository;

import de.lases.global.transport.*;
import de.lases.persistence.exception.*;
import de.lases.persistence.util.DatasourceUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Offers get/change operations on the relationship between reviewer and
 * submission.
 */
public class ReviewedByRepository {

    private static final Logger logger = Logger.getLogger(ReviewedByRepository.class.getName());

    /**
     * Returns the ReviewedBy dto for the given submission and user.
     *
     * @param submission A submission dto with a valid id.
     * @param user A user dto with a valid id.
     * @param transaction The transaction to use.
     * @return A fully filled {@code ReviewedBy} dto for the specified
     *         submission and user. Returns null if there is no such
     *         relationship.
     * @throws NotFoundException If there is no submission with the provided id,
     *                           there is no user with the provided id or there
     *                           is no
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static ReviewedBy get(Submission submission, User user,
                                 Transaction transaction)
            throws NotFoundException {
        return null;
    }

    /**
     * Changes the given {@code ReviewedBy} in the repository.
     *
     * @param reviewedBy A fully filled {@code ReviewedBy} dto.
     * @param transaction The transaction to use.
     * @throws NotFoundException If there is no reviewedBy relationship with
     *                           the given user id and submission id.
     * @throws DataNotWrittenException If writing the data to the repository
     *                                 fails.
     * @throws InvalidFieldsException If one of the fields of the
     *                                provided {@code ReviewedBy} dto is null.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static void change(ReviewedBy reviewedBy, Transaction transaction)
            throws NotFoundException, DataNotWrittenException {
    }

    /**
     * Gets a list of {@code ReviewedBy} dto's.
     *
     * @param submission A fully filled {@code ReviewedBy} dto.
     * @param transaction The transaction to use.
     *  @throws NotFoundException  If there is no scientific forum with the
     *                             provided id.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     * @return A list of {@code ReviewedBy} dto's.
     */
    public static List<ReviewedBy> getList(Submission submission, Transaction transaction) {
        if (submission.getId() == null) {
            logger.severe("A passed DTO is not sufficiently filled.");
            throw new InvalidFieldsException("Submission id must not be null");
        }

        Connection connection = transaction.getConnection();

        ResultSet resultSet;
        List<ReviewedBy> reviewedByList = new ArrayList<>();

        String sql = "SELECT * FROM reviewed_by WHERE submission_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, submission.getId());

            resultSet = statement.executeQuery();

            if (resultSet != null) {
                logger.finest("Got a list of reviewedBy DTO's");
            }

            while (resultSet.next()) {
                ReviewedBy reviewed = new ReviewedBy();

                reviewed.setReviewerId(resultSet.getInt("submission_id"));
                reviewed.setHasAccepted(AcceptanceStatus.valueOf(resultSet.getString("has_accepted")));
                reviewed.setSubmissionId(resultSet.getInt("submission_id"));
                reviewed.setTimestampDeadline(resultSet.getTimestamp("timestamp_deadline").toLocalDateTime());

                reviewedByList.add(reviewed);
            }
        } catch (SQLException exception) {
            DatasourceUtil.logSQLException(exception, logger);
            throw 
        }

        return reviewedByList;
    }

}
