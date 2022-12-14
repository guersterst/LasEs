package de.lases.control.internal;

import de.lases.business.service.CustomizationService;
import de.lases.business.service.UserService;
import de.lases.global.transport.FileDTO;
import de.lases.global.transport.Privilege;
import de.lases.global.transport.User;
import de.lases.persistence.exception.InvalidFieldsException;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Serial;
import java.lang.reflect.MalformedParametersException;
import java.net.MalformedURLException;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Serves images under the /image* url.
 */
public class ImageServlet extends HttpServlet {

    @Serial
    private static final long serialVersionUID = 2523808732109510250L;

    @Inject
    private UserService userService;

    @Inject
    private CustomizationService customizationService;

    @Inject
    private SessionInformation sessionInformation;

    private static final Logger logger = Logger.getLogger(ImageServlet.class.getName());

    /**
     * Answers a get request that requests a specific image resource specified
     * over the url. Redirects to a 404 page if the user does not have the
     * rights to see the resource.
     * The logo is served under {@code /image?id=logo}, the avatars under
     * {@code /image?id=userId}.
     * Here the URL parameter {@code id} expects a valid userId to determine
     * which user's avatar to deliver.
     *
     * @param request  The servlet request.
     * @param response The servlet response.
     * @throws ServletException If the request for the GET could not be handled.
     * @throws IOException      If an input or output error is detected when the
     *                          servlet handles the GET request.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (Objects.equals(request.getParameter("id"), "logo")) {

            // Deliver a logo.
            deliverRequestedImage(response, true, Optional.empty());
            return;
        }

        int urlUserId;
        try {
             urlUserId = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException ex) {

            logger.warning("A request for an image could not be handled due to the id URL-"
                    + "parameter being invalid: id=" + request.getParameter("id"));
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            throw new MalformedParametersException();
        }

        // Check if the required privileges are given and deliver the avatar.
        User user = sessionInformation.getUser();
        if (user == null || user.getId() == null || user.getPrivileges() == null) {

            logger.severe("There must be a user in the session in order to request a user's avatar."
                    + "This user must contain an id and privileges.");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new InvalidFieldsException();
        } else if (user.isAdmin() || user.getPrivileges().contains(Privilege.EDITOR)) {

            // Deliver avatar to users with required privileges.
            deliverRequestedImage(response, false, Optional.of(urlUserId));
        } else if (user.isRegistered() && Objects.equals(user.getId(), urlUserId)) {

            // Deliver user's own avatar.
            deliverRequestedImage(response, false, Optional.of(urlUserId));
        } else {
            logger.severe("An avatar was requested where the required privileges were not found."
                    + " Requested was user: " + urlUserId + "avatar.");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            throw new MalformedURLException();
        }
    }

    /**
     * Write a requested image to the response output-stream.
     *
     * @param response The {@code HttpServletResponse} to write the image to.
     * @param isLogo   Determines whether a logo or an avatar should be delivered.
     * @param userID   The id of the user whose avatar is to be delivered.
     *                 May be {@code Optional.empty} if the {@code isLogo} flag is set to {@code true}.
     */
    private void deliverRequestedImage(HttpServletResponse response, boolean isLogo, Optional<Integer> userID) {
        FileDTO img = fetchImage(response, isLogo, userID);
        byte[] imgBytes = img.getFile();
        configureResponse(response, imgBytes);
        try {
            response.getOutputStream().write(imgBytes);
            response.getOutputStream().close();
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.severe("The writing of the image to the response's outputstream has failed.");
        }
    }

    /**
     * Configures the response to use browser-private caching for a maximum time.
     * Also sets other metadata, like the content-length, content-type and response status.
     *
     * @param response The {@code HttpServletResponse} to write the image to.
     * @param imgBytes The bytes which are to be written onto the output-stream.
     *                 This does not occur here though. Rather they are used for metadata calculations.
     */
    private static void configureResponse(HttpServletResponse response, byte[] imgBytes) {
        response.setContentLength(imgBytes.length);
        response.setContentType("image/*");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    /**
     * Fetches an image from the database.
     *
     * @param response The {@code HttpServletResponse}.
     * @param isLogo   Determines whether a logo or an avatar should be delivered.
     * @param userId   The id of the user whose avatar is to be delivered.
     *                 May be {@code Optional.empty} if the {@code isLogo} flag is set to {@code true}.
     * @return The requested image wrapped in a fileDTO.
     */
    private FileDTO fetchImage(HttpServletResponse response, boolean isLogo, Optional<Integer> userId) {
        FileDTO img;
        if (isLogo) {
            img = customizationService.getLogo();
        } else if (userId.isPresent()) {
            User requestedUser = new User();
            requestedUser.setId(userId.get());
            img = userService.getAvatar(requestedUser);
        } else {
            logger.severe("There was no userID passed when requesting an avatar delivery.");
            throw new IllegalArgumentException();
        }

        if (img == null || img.getFile() == null) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.severe("The image file must no be null");
            // throw new IllegalStateException(); No illegal state, there is just no image served.
        }
        return img;
    }
}


