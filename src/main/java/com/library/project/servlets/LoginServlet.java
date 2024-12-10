package com.library.project.servlets;

import java.io.IOException;

import com.library.project.beans.User;
import com.library.project.contracts.HttpBaseController;
import com.library.project.exceptions.InvalidArgumentException;
import com.library.project.factories.UserFactory;
import com.library.project.repositories.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(urlPatterns = {"/login", "/logout"})
public class LoginServlet extends HttpBaseController {
    
    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void index(
        HttpServletRequest request, 
        HttpServletResponse response
    ) throws ServletException, IOException {
        this.view("views/login.jsp", request, response);
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void store(
        HttpServletRequest request, 
        HttpServletResponse response
    ) throws ServletException, IOException {

        try (UserRepository repository = new UserRepository(entityManagerFactory)) {
            User user = UserFactory.fromLoginRequest(request, repository);
            request.getSession().setAttribute("user", user);
            response.sendRedirect(request.getContextPath() + "/");
        } catch (InvalidArgumentException e) {
            request.setAttribute("error", e.getMessage());
        } catch (Exception e) {
            request.setAttribute("error", "Invalid login creds!");
        }

        this.index(request, response);
    }

    /**
     * @param request
     * @param response
     * @param id
     * @throws ServletException
     * @throws IOException
     */
    protected void delete(
        HttpServletRequest request, 
        HttpServletResponse response,
        String id
    ) throws ServletException, IOException {

        HttpSession session = request.getSession();
        
        session.removeAttribute("user");
        session.invalidate();

        this.redirect(request.getContextPath(), response);
    }
}
