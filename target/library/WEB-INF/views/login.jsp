<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Library Login Page</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Lato:wght@400;700&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    </head>
    <body class="d-flex flex-column h-100 align-items-center" data-bs-theme="dark">

        <div class="d-flex flex-column p-5 rounded-6 my-auto w-100 mx-2" style="max-width: 600px;">

            <form 
                class="d-flex flex-column"
                action="${pageContext.request.contextPath}/login" 
                method="POST" 
            >
                
                <div class="form-floating mb-3 p-1">
                    <input value="${param.email}" name="email" type="email" placeholder="Email" class="form-control border-0 bg-dark" autofocus>
                    <label class="form-label text-muted text-uppercase">Email</label>
                </div>

                <div class="form-floating mb-3 p-1">
                    <input value="${param.password}" name="password" type="password" placeholder="Password" class="form-control border-0 bg-dark">
                    <label class="form-label text-muted text-uppercase">Password</label>
                </div>
                
                <button type="submit" class="btn btn-block btn-primary text-white text-uppercase text-opacity-50" style="height:50px;">Submit</button>
            </form>

            <c:if test="${requestScope.error != null}">
                <div class="alert alert-danger small mt-4 mb-0">
                    ${requestScope.error}
                </div>
            </c:if>
            
        </div>
    </body>
</html>