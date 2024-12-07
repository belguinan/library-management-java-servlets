#!/bin/bash

BASE_PATH="src/main/java/com/library/project/servlets"
PACKAGE="com.library.project.servlets"

if [ $# -eq 0 ]; then
    echo "Usage: ./make-controller.sh ServeletName"
    exit 1
fi

SERVLET_NAME=$1
if [[ $SERVLET_NAME != *Servlet ]]; then
    SERVLET_NAME="${SERVLET_NAME}Servlet"
fi

ROUTE_NAME=$(echo "${SERVLET_NAME%Servlet}" | tr '[:upper:]' '[:lower:]')
FILE_PATH="$BASE_PATH/$SERVLET_NAME.java"

if [ -f "$FILE_PATH" ]; then
    echo "Servlet $SERVLET_NAME already exists!"
    exit 1
fi

mkdir -p "$BASE_PATH"

cat > "$FILE_PATH" << EOF
package $PACKAGE;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.library.project.contracts.HttpBaseController;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/$ROUTE_NAME/*")
public class $SERVLET_NAME extends HttpBaseController {
    
    @Override
    protected void index(
        HttpServletRequest request, 
        HttpServletResponse response
    ) throws ServletException, IOException {
        this.view("$ROUTE_NAME/index", request, response);
    }
    
    @Override
    protected void create(
        HttpServletRequest request, 
        HttpServletResponse response
    ) throws ServletException, IOException {
        this.view("$ROUTE_NAME/create", request, response);
    }
    
    @Override
    protected void store(
        HttpServletRequest request, 
        HttpServletResponse response
    ) throws ServletException, IOException {
        this.redirect(request.getContextPath() + "/$ROUTE_NAME", response);
    }
    
    @Override
    protected void show(
        HttpServletRequest request, 
        HttpServletResponse response, 
        String id
    ) throws ServletException, IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("id", id);
        this.view("$ROUTE_NAME/show", data, request, response);
    }
    
    @Override
    protected void edit(
        HttpServletRequest request, 
        HttpServletResponse response, 
        String id
    ) throws ServletException, IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("id", id);
        this.view("$ROUTE_NAME/edit", data, request, response);
    }
    
    @Override
    protected void update(
        HttpServletRequest request, 
        HttpServletResponse response, 
        String id
    ) throws ServletException, IOException {
        this.redirect(request.getContextPath() + "/$ROUTE_NAME", response);
    }
    
    @Override
    protected void delete(
        HttpServletRequest request, 
        HttpServletResponse response, 
        String id
    ) throws ServletException, IOException {
        this.redirect(request.getContextPath() + "/$ROUTE_NAME", response);
    }
}
EOF

echo "Servlet $SERVLET_NAME created successfully!"
echo "Views created in: $VIEW_PATH"