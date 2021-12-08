package de.lases.control.internal;

import java.io.IOException;
import java.lang.reflect.MalformedParametersException;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

import de.lases.business.service.CustomizationService;
import de.lases.business.service.UserService;
import de.lases.global.transport.FileDTO;
import de.lases.global.transport.Privilege;
import de.lases.global.transport.User;
import de.lases.persistence.exception.InvalidFieldsException;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.swing.text.html.Option;

/**
 * Serves images under the /image/* url.
 */
@WebServlet("/image*")
public class ImageServlet extends HttpServlet {

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
     * The logo is served under {@code /image?type=logo}, the avatars under
     * {@code /image?type=avatar&user=userId}.
     * Here the URL parameter {@code type} determines whether the logo or an avatar
     * is requested. The other URL parameter {@code user} expects a valid userId to determine
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
        if (Objects.equals(request.getParameter("type"), "logo")) {

            deliverRequestedImage(response, true, Optional.empty());
        } else if (Objects.equals(request.getParameter("type"), "avatar")) {
            User user = sessionInformation.getUser();
            Integer urlUserId = Integer.getInteger(request.getParameter("user"));

            if (user == null || user.getId() == null || user.getPrivileges() == null) {

                logger.severe("There must be a user in the session in order to request a user's avatar."
                        + "This user must contain an id and privileges.");
                throw new InvalidFieldsException();
            } else if (user.isAdmin() || user.getPrivileges().contains(Privilege.EDITOR)) {

                deliverRequestedImage(response, false, Optional.of(urlUserId));
            } else if (user.isRegistered() && Objects.equals(user.getId(), urlUserId)) {

                deliverRequestedImage(response, false, Optional.of(urlUserId));
            } else {
                logger.severe("An avatar was requested where the required privileges were not found."
                        + " Requested was user: " + urlUserId + "avatar.");
                throw new IllegalCallerException();
            }
        } else {
            logger.warning("A request for an image could not be handled due to the type URL"
                    + "parameter being invalid: type=" + request.getParameter("type"));
            throw new MalformedParametersException();
        }
    }

    private void deliverRequestedImage(HttpServletResponse response, boolean isLogo, Optional<Integer> userId) {

    }

}

