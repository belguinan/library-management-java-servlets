package com.library.project.filters;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(
        ServletRequest request, 
        ServletResponse response, 
        FilterChain chain
    ) throws IOException, ServletException {

        HttpServletRequest servletRequest = (HttpServletRequest) request;
        HttpServletResponse servletResponse = (HttpServletResponse) response;
        HttpSession session = servletRequest.getSession(false);
        
        String path = servletRequest.getRequestURI();
        
        if (this.isResource(path)) {
            chain.doFilter(request, response);
            return;
        }
        
        if (
            (session == null || session.getAttribute("user") == null) &&
            !path.endsWith("/login")
        ) {
            servletResponse.sendRedirect(servletRequest.getContextPath() + "/login");
            return;
        }
        
        chain.doFilter(request, response);
    }

    /**
     * @param path
     * @return
     */
    private boolean isResource(String path) {
        return path.matches(".*(css|js|svg)$") || path.startsWith("/images/");
    }
}
