package com.library.project.classes;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;

@JsonInclude(JsonInclude.Include.NON_NULL)
public final class JsonResponse {

    /**
     * Http response code
     */
    private int status;

    /**
     * Http message
     */
    private String message;

    /**
     * Object returned within the response
     */
    private Object data;

    /**
     * jackson ObjectMapper
     */
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * @param status
     * @param message
     */
    public JsonResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    /**
     * @param status
     * @param message
     */
    public JsonResponse(int status, Object data) {
        this.status = status;
        this.data = data;
    }

    /**
     * @param status
     * @param message
     * @param data
     */
    public JsonResponse(int status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    /**
     * No arg constructor
     */
    public JsonResponse() {
        this.status = 200;
        this.message = "";
    }

    /**
     * @return int
     */
    public int getStatus() {
        return this.status;
    }

    /**
     * @return String
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * @return Object
     */
    public Object getData() {
        return this.data;
    }

    /**
     * @param response
     * @param jsonResponse
     * @throws IOException
     */
    public static void send(
        HttpServletResponse response, 
        JsonResponse jsonResponse
    ) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        mapper.writeValue(response.getWriter(), jsonResponse);
    }
}