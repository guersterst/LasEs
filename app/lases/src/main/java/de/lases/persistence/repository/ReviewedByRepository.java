package de.lases.persistence.repository;

import de.lases.business.service.SubmissionService;
import de.lases.global.transport.*;
import de.lases.persistence.exception.*;

import java.sql.*;
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
        Connection conn = transaction.getConnection();
        String query = "SELECT * FROM reviewed_by WHERE submission_id = ? AND reviewer_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, submission.getId());
            ps.setInt(2, user.getId());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                ReviewedBy reviewedBy = new ReviewedBy();
                reviewedBy.setReviewerId(user.getId());
                reviewedBy.setSubmissionId(submission.getId());
                Timestamp timestamp = rs.getTimestamp("timestamp_deadline");
                if (timestamp != null) {
                    reviewedBy.setTimestampDeadline(timestamp.toLocalDateTime());
                }
                reviewedBy.setHasAccepted(AcceptanceStatus.valueOf(rs.getString("has_accepted")));

                return reviewedBy;
            }else {
                throw new NotFoundException("No entry in reviewed_by for user: " + user);
            }
        } catch (SQLException e) {
            logger.severe("ReviewedBy.get Exception: " + e.getMessage());
            throw new DatasourceQueryFailedException();
        }
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

}
