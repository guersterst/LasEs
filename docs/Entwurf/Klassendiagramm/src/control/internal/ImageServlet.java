package control.internal;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import business.service.CustomizationService;
import business.service.UserService;

@WebServlet("/image/*")
public class ImageServlet extends HttpServlet {
	
    //private ImageService service;
	private UserService userService;
	
	private CustomizationService customizationService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

}

