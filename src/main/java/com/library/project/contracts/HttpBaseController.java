package com.library.project.contracts;

import java.io.IOException;

import com.library.project.classes.JsonResponse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Contrôleur de base inspiré de Laravel pour Java EE
 * Implémente les méthodes RESTful similaires à Laravel
 */
public abstract class HttpBaseController extends HttpServlet implements JpaEntityManager {
    
    private static final long serialVersionUID = 1L;
    
    private static final String INDEX_PATTERN = "^/?$";
    private static final String SHOW_PATTERN = "^/(\\d+)/?$";
    private static final String CREATE_PATTERN = "^/create/?$";
    private static final String EDIT_PATTERN = "^/(\\d+)/edit/?$";
    
    protected boolean ajaxOnly() {
        return false;
    }
    
    /**
     * @param request
     * @return boolean
     */
    protected boolean ajax(HttpServletRequest request)
    {
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    }

    /**
     * @param request
     * @param response
     * @throws IOException
     */
    protected void redirectIfNotAjax(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        if (! this.ajax(request)) {
            response.sendRedirect(request.getContextPath());
        }
    }
    
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
        this.sendMethodError(response, "index");
    }
    
    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void create(
        HttpServletRequest request, 
        HttpServletResponse response
    ) throws ServletException, IOException {
        this.sendMethodError(response, "create");
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
        this.sendMethodError(response, "store");
    }
    
    /**
     * @param request
     * @param response
     * @param id
     * @throws ServletException
     * @throws IOException
     */
    protected void show(
        HttpServletRequest request, 
        HttpServletResponse response, 
        String id
    ) throws ServletException, IOException {
        this.sendMethodError(response, "show");
    }
    
    /**
     * @param request
     * @param response
     * @param id
     * @throws ServletException
     * @throws IOException
     */
    protected void edit(
        HttpServletRequest request, 
        HttpServletResponse response, 
        String id
    ) throws ServletException, IOException {
        this.sendMethodError(response, "edit");
    }
    
    /**
     * @param request
     * @param response
     * @param id
     * @throws ServletException
     * @throws IOException
     */
    protected void update(
        HttpServletRequest request, 
        HttpServletResponse response, 
        String id
    ) throws ServletException, IOException {
        this.sendMethodError(response, "update");
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
        this.sendMethodError(response, "delete");
    }
    
    @Override
    protected void doGet(
        HttpServletRequest request, 
        HttpServletResponse response
    ) throws ServletException, IOException {
        
        if (this.ajaxOnly()) {
            this.redirectIfNotAjax(request, response);
        }
                
        String pathInfo = request.getPathInfo() != null ? request.getPathInfo() : "/";

        if (pathInfo.matches(INDEX_PATTERN)) {
            this.index(request, response);
            return;
        }

        if (pathInfo.matches(CREATE_PATTERN)) {
            this.create(request, response);
            return;
        }

        String id = this.extractIdFromPath(pathInfo);
        
        if (pathInfo.matches(SHOW_PATTERN)) {
            this.show(request, response, id);
            return;
        }
        
        if (pathInfo.matches(EDIT_PATTERN)) {
            this.edit(request, response, id);
            return;
        }
        
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
    
    @Override
    protected void doPost(
        HttpServletRequest request, 
        HttpServletResponse response
    ) throws ServletException, IOException {

        if (this.ajaxOnly()) {
            this.redirectIfNotAjax(request, response);
        }
        
        String method = request.getParameter("_method");
        String pathInfo = request.getPathInfo() != null ? request.getPathInfo() : "/";
        String id = this.extractIdFromPath(pathInfo);
        
        if (method == null) {
            this.store(request, response);
            return;
        }
        
        switch (method.toUpperCase()) {
            case "PUT":
            case "PATCH":
                this.update(request, response, id);
                break;
            case "DELETE":
                this.delete(request, response, id);
                break;
            default:
                response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }
    }

    /**
     * @param response
     * @param method
     */
    private void sendMethodError(
        HttpServletResponse response, 
        String method
    ) throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_NOT_FOUND, method + " method not implemented");
    }
    
    /**
     * @param pathInfo
     * @return String
     */
    protected String extractIdFromPath(String pathInfo) {
        if (pathInfo == null) return null;
        String[] parts = pathInfo.split("/");
        for (String part : parts) {
            if (part.matches("\\d+")) {
                return part;
            }
        }
        return null;
    }
    
    /**
     * @param path
     * @param response
     * @throws IOException
     */
    protected void redirect(
        String path, 
        HttpServletResponse response
    ) throws IOException {
        response.sendRedirect(path);
    }
    
    /**
     * @param viewPath
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void view(
        String viewPath, 
        HttpServletRequest request, 
        HttpServletResponse response
    ) throws ServletException, IOException {
        request.getRequestDispatcher("WEB-INF/" + viewPath).forward(request, response);
    }

    /**
     * @param jsonResponse
     * @param request
     * @param response
     * @throws IOException
     */
    protected void json(
        JsonResponse jsonResponse,
        HttpServletResponse response
    ) throws IOException {
        JsonResponse.send(response, jsonResponse);
    }

    @Override
    public void destroy() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
        super.destroy();
    }
}