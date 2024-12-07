<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>

<t:auth-layout title="Borrowing History">

    <jsp:attribute name="scripts">
        <script type="module" src="${pageContext.request.contextPath}/js/borrow.js?v=1.0.2"></script>
    </jsp:attribute>

    <jsp:body>

        <div class="container bg-light rounded-6">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h4 class="m-0">Borrowing History</h4>
            </div>

            <div class="mb-4">
                <form method="GET" action="${pageContext.request.contextPath}/borrow" class="d-flex gap-3" id="filterForm">

                    <c:if test="${param.book_id != null}">
                        <input name="book_id" type="hidden" value="<c:out value="${param.book_id}" />">
                    </c:if>
                
                    <div class="flex-grow-1">
                        <div class="input-group bg-dark-subtle rounded-3 p-1">
                            <span class="input-group-text border-0 bg-transparent text-muted">
                                <i class="bi bi-search"></i>
                            </span>
                            <input type="text" 
                                class="form-control border-0 bg-transparent shadow-none text-white" 
                                id="searchInput"
                                placeholder="Search history by book title or by customer..."
                                name="search">
                        </div>
                    </div>
                </form>
            </div>
            
            <div id="borrow-table-container" style="display:none;">
                <table class="table table-borderless table-striped rounded-3 overflow-hidden bg-dark">
                    <thead class="bg-dark">
                        <tr>
                            <th class="px-3 py-3 text-muted text-uppercase fw-bold text-muted">Book ID</th>
                            <th class="px-3 py-3 text-muted text-uppercase fw-bold text-muted">Title</th>
                            <th class="px-3 py-3 text-muted text-uppercase fw-bold text-muted">Customer</th>
                            <th class="px-3 py-3 text-muted text-uppercase fw-bold text-white">Email</th>
                            <th class="px-3 py-3 text-muted text-uppercase fw-bold text-muted">Borrowed At</th>
                            <th class="px-3 py-3 text-muted text-uppercase fw-bold text-muted"></th>
                            <th class="px-3 py-3 text-muted text-uppercase fw-bold text-muted"></th>
                            <th class="px-3 py-3 text-muted text-uppercase fw-bold text-muted"></th>
                        </tr>
                    </thead>
                    <tbody id="borrow-container">
                        
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

            <div id="borrow-warning" class="alert alert-warning text-center" style="display:none;">
                <div class="mb-2">
                    <i class="bi bi-clock-history fs-2 mb-2 d-block"></i> No borrowed books found
                </div>
            </div>

            <div class="modal fade" id="return-book-modal" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-hidden="true">
                <div class="modal-dialog modal-dialog-centered">
                    <div class="modal-content">
                        <form action="${pageContext.request.contextPath}/borrow" method="POST" id="returnBookForm">
                            <input type="hidden" name="book_id">
                            <input type="hidden" name="borrow_id">
                            <input type="hidden" name="_method" value="PUT">
                            <div class="modal-header border-bottom-0">
                                <h5 class="modal-title">Return Book</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body">
                                <div class="form-floating mb-3">
                                    <input type="text" class="form-control" name="cin" placeholder="CIN" required readonly style="cursor: not-allowed;">
                                    <label class="text-muted mb-1 text-uppercase">CIN</label>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                <button type="submit" class="btn btn-primary">Return Book</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>

        </div>

    </jsp:body>

</t:auth-layout>