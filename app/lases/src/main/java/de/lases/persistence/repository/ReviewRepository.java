package de.lases.persistence.repository;

import de.lases.global.transport.*;
import de.lases.persistence.exception.*;
import de.lases.persistence.internal.ConfigReader;
import jakarta.enterprise.inject.spi.CDI;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Offers get/add/change/remove operations on a review and the possibility to
 * get lists of reviews.
 */
public class ReviewRepository {

    private static final Logger logger = Logger.getLogger(SubmissionRepository.class.getName());

    private static final List<String> filterColumnNames = List.of("version", "lastname", "timestamp_upload", "is_visible", "is_recommended", "comment");

    /**
     * Takes a review dto that is filled out with a valid reviewerId, paperId
     * and submissionId and returns a fully filled review dto.
     *
     * @param review A review dto that must be filled with a valid reviewerId,
     *               paperId and submissionId
     * @param transaction The transaction to use.
     * @return A fully filled review dto.
     * @throws NotFoundException If there is no review with the provided ids.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static Review get(Review review, Transaction transaction)
            throws NotFoundException {
        return null;
    }

    /**
     * Adds a review to the repository.
     *
     * @param review A fully filled review dto. (The id must not be specified,
     *               as the repository will create the id)
     * @param transaction The transaction to use.
     * @throws DataNotWrittenException If writing the data to the repository
     *                                 fails.
     * @throws InvalidFieldsException If one of the fields of the review is
     *                                null.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     * @throws NotFoundException If there is no submission with the given
     *                           submissionId or the submission has no
     *                           reviewer with the given reviewerId.
     */
    public static void add(Review review, Transaction transaction)
            throws DataNotWrittenException, NotFoundException {
    }

    /**
     * Changes the given review in the repository.
     *
     * @param review A fully filled review dto.
     * @param transaction The transaction to use.
     * @throws NotFoundException If there is no review with the provided
     *                           reviewerId, paperId and submissionId.
     * @throws DataNotWrittenException If writing the data to the repository
     *                                 fails.
     * @throws InvalidFieldsException If one of the fields of the review is
     *                                null.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static void change(Review review, Transaction transaction)
            throws NotFoundException, DataNotWrittenException {
    }

    /**
     * Takes a review dto that is filled with a valid reviewerId, paperId and
     * submissionId and removes this review from the repository.
     *
     * @param review The review to remove. Must be filled with a valid
     *               reviewerId, paperId and submissionId.
     * @param transaction The transaction to use.
     * @throws NotFoundException The specified review was not found in the
     *                           repository.
     * @throws DataNotWrittenException If writing the data to the repository
     *                                 fails.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static void remove(Review review, Transaction transaction)
            throws NotFoundException, DataNotWrittenException {
    }

    /**
     * Gets a list all reviews that belong to the specified submission that the
     * specified user can see. (An editor can see everything, the submitter
     * can view see all visible reviews, a reviewer his own reviews.)
     *
     * @param submission A submission dto filled with a valid id.
     * @param user A user dto filled with a valid id.
     * @param transaction The transaction to use.
     * @param resultListParameters The ResultListParameters dto that results
     *                             parameters from the pagination like
     *                             filtering, sorting or number of elements.
     * @return A list of fully filled review dtos for all reviews that belong
     *         to the specified submission and the specified user can see.
     * @throws DataNotCompleteException If the list is truncated.
     * @throws NotFoundException If there is no submission with the provided id
     *                           or there is no user with the provided id.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     * @throws InvalidQueryParamsException If the resultListParameters contain
     *                                     an erroneous option.
     */
    public static List<Review> getList(Submission submission, User user,
                               Transaction transaction,
                               ResultListParameters resultListParameters)
            throws DataNotCompleteException, NotFoundException {
        if (transaction == null) {
            logger.severe("Passed transaction is null.");
            throw new IllegalArgumentException("Transaction must not be null.");
        }
        if (resultListParameters == null) {
            logger.severe("Passed result-list parameters is null.");
            throw new IllegalArgumentException("ResultListParameters must not be null.");
        }
        if (user == null || user.getId() == null) {
            logger.severe("Passed  User-DTO is not sufficiently filled.");
            throw new IllegalArgumentException("User id must not be null.");
        }
        if (submission == null || submission.getId() == null) {
            logger.severe("Passed  Submission-DTO is not sufficiently filled.");
            throw new IllegalArgumentException("User id must not be null.");
        }

        Connection conn = transaction.getConnection();
        ResultSet resultSet;
        List<Review> reviewList = new ArrayList<>();

        // Case Editor or Admin
        if (user.isAdmin() || submission.getEditorId() == user.getId()) {
            try (PreparedStatement ps = conn.prepareStatement(getStatementReviewListForEditor(resultListParameters, false))) {

                int i = 1;
                if (isFilled(resultListParameters.getFilterColumns().get("version"))) {
                    ps.setString(i, "%" + resultListParameters.getFilterColumns().get("version") + "%");
                    i++;
                }
                if (isFilled(resultListParameters.getFilterColumns().get("lastname"))) {
                    ps.setString(i, "%" + resultListParameters.getFilterColumns().get("lastname") + "%");
                    i++;
                }
                if (isFilled(resultListParameters.getFilterColumns().get("comment"))) {
                    ps.setString(i, "%" + resultListParameters.getFilterColumns().get("comment") + "%");
                }

                resultSet = ps.executeQuery();

                while (resultSet.next()) {
                    Review review = new Review();
                    review.setSubmissionId(resultSet.getInt("submission_id"));
                    review.setPaperVersion(resultSet.getInt("version"));
                    review.setReviewerId(resultSet.getInt("reviewer_id"));
                    java.sql.Timestamp timestamp = resultSet.getTimestamp("timestamp_upload");
                    if (timestamp != null) {
                        review.setUploadTime(timestamp.toLocalDateTime());
                    }
                    review.setVisible(resultSet.getBoolean("is_visible"));
                    review.setAcceptPaper(resultSet.getBoolean("is_recommended"));
                    review.setComment(resultSet.getString("comment"));

                    reviewList.add(review);
                }
            } catch (SQLException e) {
                logger.severe(e.getMessage());
                throw new DatasourceQueryFailedException(e.getMessage(), e);
            }
        }
        return reviewList;
    }

    public static int getCountItemsList(Submission submission, User user,
                                       Transaction transaction,
                                       ResultListParameters resultListParameters)
            throws DataNotCompleteException, NotFoundException {
        if (transaction == null) {
            logger.severe("Passed transaction is null.");
            throw new IllegalArgumentException("Transaction must not be null.");
        }
        if (resultListParameters == null) {
            logger.severe("Passed result-list parameters is null.");
            throw new IllegalArgumentException("ResultListParameters must not be null.");
        }
        if (user == null || user.getId() == null) {
            logger.severe("Passed  User-DTO is not sufficiently filled.");
            throw new IllegalArgumentException("User id must not be null.");
        }
        if (submission == null || submission.getId() == null) {
            logger.severe("Passed  Submission-DTO is not sufficiently filled.");
            throw new IllegalArgumentException("User id must not be null.");
        }

        Connection conn = transaction.getConnection();
        ResultSet resultSet;
        List<Review> reviewList = new ArrayList<>();

        // Case Editor or Admin
        if (user.isAdmin() || submission.getEditorId() == user.getId()) {
            try (PreparedStatement ps = conn.prepareStatement(getStatementReviewListForEditor(resultListParameters, true))) {

                int i = 1;
                if (isFilled(resultListParameters.getFilterColumns().get("version"))) {
                    ps.setString(i, "%" + resultListParameters.getFilterColumns().get("version") + "%");
                    i++;
                }
                if (isFilled(resultListParameters.getFilterColumns().get("lastname"))) {
                    ps.setString(i, "%" + resultListParameters.getFilterColumns().get("lastname") + "%");
                    i++;
                }
                if (isFilled(resultListParameters.getFilterColumns().get("comment"))) {
                    ps.setString(i, "%" + resultListParameters.getFilterColumns().get("comment") + "%");
                }

                resultSet = ps.executeQuery();

                if (resultSet.next()) {
                    return resultSet.getInt("count");
                }

            } catch (SQLException e) {
                logger.severe(e.getMessage());
                throw new DatasourceQueryFailedException(e.getMessage(), e);
            }
        }
        return -1;
    }



    private static boolean isFilled(String s) {
        return s != null && !s.isEmpty();
    }

    private static String getStatementReviewListForEditor(ResultListParameters resultListParameters, boolean doCount) {
        StringBuilder sb = new StringBuilder();

        if (doCount) {
            sb.append("SELECT COUNT (DISTINCT CONCAT(p.version, ':', r.reviewer_id)) as count\n");
        } else {
            sb.append("SELECT DISTINCT r.*, u.lastname\n");
        }

        sb.append("""
                    FROM submission s, paper p, review r, "user" u
                    WHERE s.id = p.submission_id AND p.version = r.version AND r.reviewer_id = u.id
                    """).append("\n");

        if (resultListParameters.getVisibleFilter() != null) {
            sb.append(switch (resultListParameters.getVisibleFilter()) {
                case ALL -> "";
                case RELEASED -> " AND r.is_visible = TRUE\n";
                case NOT_RELEASED -> " And r.is_visible = FALSE\n";
            });
        }

        if (resultListParameters.getRecommendedFilter() != null) {
            sb.append(switch (resultListParameters.getRecommendedFilter()) {
                case ALL -> "";
                case RECOMMENDED -> " AND r.is_recommended = TRUE\n";
                case NOT_RECOMMENDED -> " AND r.is_recommended = FALSE\n";
            });
        }

        if (isFilled(resultListParameters.getFilterColumns().get("version"))) {
            sb.append(" AND r.version::VARCHAR ILIKE ?\n");
        }
        if (isFilled(resultListParameters.getFilterColumns().get("lastname"))) {
            sb.append(" AND u.lastname ILIKE ?\n");
        }
        if (isFilled(resultListParameters.getFilterColumns().get("comment"))) {
            sb.append(" AND r.comment ILIKE ?\n");
        }

        if (!doCount) {
            if (isFilled(resultListParameters.getSortColumn())) {
                if (resultListParameters.getSortColumn().equals("lastname")) {
                    sb.append("ORDER BY u.lastname");
                } else {
                    sb.append("ORDER BY r.").append(resultListParameters.getSortColumn());
                }
                sb.append(" ")
                        .append(resultListParameters.getSortOrder() == SortOrder.ASCENDING ? "ASC" : "DESC")
                        .append("\n");
            }

            // Set limit and offset
            ConfigReader configReader = CDI.current().select(ConfigReader.class).get();
            int paginationLength = Integer.parseInt(configReader.getProperty("MAX_PAGINATION_LIST_LENGTH"));
            sb.append("LIMIT ").append(paginationLength)
                    .append(" OFFSET ").append(paginationLength * (resultListParameters.getPageNo() - 1));
        }

        return sb.toString();
    }

    /**
     * Get the PDF file for the provided review.
     *
     * @param review A review dto filled with a valid reviewerId, paperId and
     *              submissionId.
     * @param transaction The transaction to use.
     * @return A file containing the PDF for the specified review.
     * @throws NotFoundException If there is no review with the provided ids.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static FileDTO getPDF(Paper review, Transaction transaction)
            throws NotFoundException {
        return null;
    }

    /**
     * Sets the PDF belonging to a specified review.
     *
     * @param review A review dto filled with a valid reviewerId, paperId and
     *               submissionId.
     * @param pdf A file dto filled with a pdf file.
     * @param transaction The transaction to use.
     * @throws DataNotWrittenException If writing the data to the repository
     *                                 fails.
     * @throws NotFoundException If there is no review with the provided ids.
     * @throws DatasourceQueryFailedException If the datasource cannot be
     *                                        queried.
     */
    public static void setPDF(Review review, FileDTO pdf, Transaction transaction)
            throws DataNotWrittenException, NotFoundException {
    }

}
