<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>

<t:auth-layout title="Customers List">

    <jsp:attribute name="scripts">
        <script type="module" src="${pageContext.request.contextPath}/js/customer.js?v=1.0.2"></script>
    </jsp:attribute>

    <jsp:body>

        <div class="container bg-light rounded-6">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h4 class="m-0">Customers List</h4>
            </div>

            <div id="customer-table-container" style="display:none;">
                <table class="table table-borderless table-striped rounded-3 overflow-hidden bg-dark">
                    <thead class="bg-dark">
                        <tr>
                            <th class="px-3 py-3 text-muted text-uppercase fw-bold text-muted">ID</th>
                            <th class="px-3 py-3 text-muted text-uppercase fw-bold text-muted">Name</th>
                            <th class="px-3 py-3 text-muted text-uppercase fw-bold text-white">Email</th>
                            <th class="px-3 py-3 text-muted text-uppercase fw-bold text-muted">CIN</th>
                            <th class="px-3 py-3 text-muted text-uppercase fw-bold text-muted">Created at</th>
                        </tr>
                    </thead>
                    <tbody id="customer-container">
                        
                    </tbody>
                </table>
                <nav class="mt-3 d-flex justify-content-between align-items-center">
                    <div class="text-muted small opacity-50">
                        <code class="text-white">
                            Page (<span class="current-page">1</span>)
                        </code>
                        <code class="text-white mx-1"> - </code>
                        <code class="text-white">Total <span class="current-total-items">0</span> items</code>
                    </div>
                    <ul class="pagination mb-0">
                        <li class="btn btn-sm btn-dark me-2 jq-pagination" onclick="return pagination.prevPage(event)">
                            <i class="bi bi-chevron-left"></i>
                        </li>
                        <li class="btn btn-sm btn-dark jq-pagination" onclick="return pagination.nextPage(event)">
                            <i class="bi bi-chevron-right"></i>
                        </li>
                    </ul>
                </nav>
            </div>

            <div id="customer-warning" class="alert alert-warning text-center" style="display:none;">
                <div class="mb-2">
                    <i class="bi bi-clock-history fs-2 mb-2 d-block"></i> No customers to show here!
                </div>
            </div>
        </div>

    </jsp:body>

</t:auth-layout>