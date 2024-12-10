<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ attribute name="title" required="false" rtexprvalue="true" %>
<%@ attribute name="scripts" fragment="true" %>
<%@ attribute name="stylesheets" fragment="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${empty title ? 'Library Management' : title}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Lato:wght@300;400;700&display=swap" rel="stylesheet">
    <script src="${pageContext.request.contextPath}/js/loader.js?v=1.0.3"></script>
    <script src="${pageContext.request.contextPath}/js/template.js?v=1.0.3"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css?v=1.0.3">
    <jsp:invoke fragment="stylesheets"/>
</head>
<body data-bs-theme="dark">
    <nav class="navbar navbar-expand-lg border-0 mb-3">
        <div class="container">
            <a class="navbar-brand text-white" href="<c:url value='/'/>">Library System</a>
            
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav ms-auto me-lg-3">
                    <li class="nav-item">
                        <a class="nav-link mb-0" href="${pageContext.request.contextPath}/">
                            <i class="bi bi-book me-2"></i> Books
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link mb-0" href="${pageContext.request.contextPath}/borrow">
                            <i class="bi bi-book me-2"></i> History
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link mb-0" href="${pageContext.request.contextPath}/customer">
                            <i class="bi bi-clock-history me-2"></i> Customers
                        </a>
                    </li>
                    <li class="nav-item">
                        <form action="${pageContext.request.contextPath}/logout" method="POST">
                            <input name="_method" value="DELETE" type="hidden">
                            <button type="submit" class="nav-link mb-0">
                                <i class="bi bi-box-arrow-right me-1"></i>
                                Logout
                            </button>
                        </form>
                    </li>
                </ul>
                
            </div>
        </div>
    </nav>

    <div class="container mb-5" id="app">
        <div class="bg-light shadow-sm px-2 py-3 rounded-6">
            <jsp:doBody/>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

    <div class="toast-container position-fixed top-0 end-0 p-3">
        <div id="notificationToast" class="toast align-items-center border-0" role="alert" aria-live="assertive" aria-atomic="true">
            <div class="d-flex">
                <div class="toast-body">
                </div>
                <button type="button" class="btn-close me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
            </div>
        </div>
    </div>

    <script>
        // Global variables for js
        const pageContextPath = "${pageContext.request.contextPath}";
    </script>

    <script src="${pageContext.request.contextPath}/js/main.js?v=1.0.3"></script>

    <jsp:invoke fragment="scripts"/>
</body>
</html>