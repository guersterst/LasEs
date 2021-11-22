package de.lases.control.internal;

import java.io.IOException;

import de.lases.business.service.CustomizationService;
import de.lases.business.service.UserService;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Serves images under the /image/* url.
 */
@WebServlet("/image/*")
public class ImageServlet extends HttpServlet {

    @Inject
    private UserService userService;

    @Inject
    private CustomizationService customizationService;

    /**
     * Answers a get request that requests a specific image resource specified
     * over the url. Redirects to a 404 page if the user does not have the
     * rights to see the resource.
     * The logo is served under /image/logo, the avatars under
     * /image/avatar/userId
     *
     * @param request The servlet request.
     * @param response The servlet response.
     * @throws ServletException If the request for the GET could not be handled.
     * @throws IOException If an input or output error is detected when the
     *                     servlet handles the GET request.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

}

