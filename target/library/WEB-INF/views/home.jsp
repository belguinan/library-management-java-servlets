<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>

<t:auth-layout title="Books List">

    <jsp:attribute name="stylesheets">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/vanillajs-datepicker@1.3.4/dist/css/datepicker-bs5.min.css">
    </jsp:attribute>

    <jsp:attribute name="scripts">
        <script type="module" src="${pageContext.request.contextPath}/js/home.js?v=1.0.1"></script>
    </jsp:attribute>

    <jsp:body>

        <div class="container bg-light rounded-6">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h4 class="m-0">Books Management</h4>
                <button type="button" class="btn btn-primary rounded-3" data-bs-toggle="modal" data-bs-target="#add-book-modal">
                    <i class="bi bi-plus"></i>
                    <small class="text-muted">
                        Add New Book
                    </small>
                </button>
            </div>

            <div class="mb-4">
                <form method="GET" action="${pageContext.request.contextPath}/book" class="d-flex gap-3" id="filter">
                    <div class="flex-grow-1">
                        <div class="input-group bg-dark-subtle rounded-3 p-1">
                            <span class="input-group-text border-0 bg-transparent text-muted">
                                <i class="bi bi-search"></i>
                            </span>
                            <input type="text" 
                                class="form-control border-0 bg-transparent shadow-none text-white" 
                                id="searchInput"
                                placeholder="Search books by title, author or category..."
                                name="search">
                        </div>
                    </div>
                    <div class="d-flex gap-2">
                        <input type="checkbox" id="in-stock" class="btn-check jq-filter" name="stock[]" value="in-stock" />
                        <label for="in-stock" class="btn btn-dark-subtle rounded-3 px-5 d-flex align-items-center gap-2">
                            <i class="bi bi-check-circle-fill text-success"></i>
                            <small>In Stock</small>
                        </label>

                        <input type="checkbox" id="low-stock" class="btn-check jq-filter" name="stock[]" value="low-stock" />
                        <label for="low-stock" class="btn btn-dark-subtle rounded-3 px-5 d-flex align-items-center gap-2">
                            <i class="bi bi-exclamation-circle-fill text-warning"></i>
                            <small>Low Stock</small>
                        </label>

                        <input type="checkbox" id="out-of-stock" class="btn-check jq-filter" name="stock[]" value="out-of-stock" />
                        <label for="out-of-stock" class="btn btn-dark-subtle rounded-3 px-5 d-flex align-items-center gap-2">
                            <i class="bi bi-x-circle-fill text-danger"></i>
                            <small>Out Stock</small>
                        </label>
                    </div>
                </form>
            </div>
            
            <div id="books-table-container" style="display:none;">
                <table class="table table-borderless table-striped rounded-3 overflow-hidden bg-dark">
                    <thead class="bg-dark">
                        <tr>
                            <th class="px-3 py-3 text-muted text-uppercase fw-bold text-muted">ID</th>
                            <th class="px-3 py-3 text-muted text-uppercase fw-bold text-white">Title</th>
                            <th class="px-3 py-3 text-muted text-uppercase fw-bold text-muted">Author</th>
                            <th class="px-3 py-3 text-muted text-uppercase fw-bold text-muted">Category</th>
                            <th class="px-3 py-3 text-muted text-uppercase"></th>
                            <th class="px-3 py-3 text-muted text-uppercase"></th>
                        </tr>
                    </thead>
                    <tbody id="books-container">
                        
                    </tbody>
                </table>

                <nav class="mt-3 d-flex justify-content-between align-items-center">
                    <div class="text-muted small opacity-50">
                        <code class="text-white">
                            Page (<span id="current-page">1</span>)
                        </code>
                        <code class="text-white mx-1"> - </code>
                        <code class="text-white">Total <span id="current-total-items">0</span> items</code>
                    </div>
                    <ul class="pagination rounded-3 mb-0">
                        <li class="btn rounded-3 btn-dark-subtle me-2 jq-pagination" onclick="return pagination.prevPage(event)">
                            <i class="bi bi-chevron-left"></i>
                        </li>
                        <li class="btn rounded-3 btn-dark-subtle jq-pagination" onclick="return pagination.nextPage(event)">
                            <i class="bi bi-chevron-right"></i>
                        </li>
                    </ul>
                </nav>

            </div>

            <div id="books-warning" class="alert alert-warning text-center" style="display:none;">
                <div class="mb-2">
                    <i class="bi bi-inbox fs-2 mb-2 d-block"></i> No books found
                </div>
                
                <button type="button" class="btn btn-warning btn-sm" data-bs-toggle="modal" data-bs-target="#add-book-modal">
                    <small>New Book</small>
                </button>
            </div>

            <div class="modal fade" id="add-book-modal" data-bs-backdrop="static" data-bs-keyboard="true" tabindex="-1" aria-hidden="true">
                <div class="modal-dialog modal-dialog-centered modal-dialog-scrollable">
                    <div class="modal-content">
                        <form action="${pageContext.request.contextPath}/book" method="POST" id="addBookForm">
                            <div class="modal-header border-bottom-0">
                                <h5 class="modal-title">Add New Book</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body">
                                <div class="form-floating mb-3">
                                    <input type="text" class="form-control" name="title" id="title" placeholder="Title" required>
                                    <label class="text-muted mb-1 text-uppercase" for="title">Title</label>
                                </div>
                                
                                <div class="form-floating mb-3">
                                    <input type="text" class="form-control" name="author" id="author" placeholder="Author" required>
                                    <label class="text-muted mb-1 text-uppercase" for="author">Author</label>
                                </div>
                                
                                <div class="form-floating mb-3">
                                    <textarea class="form-control" name="description" id="description" placeholder="Description" style="height: 200px"></textarea>
                                    <label class="text-muted mb-1 text-uppercase" for="description">Description</label>
                                </div>

                                <div class="row g-3">
                                    <div class="mb-3 col-md">
                                        <label class="text-muted mb-1 text-uppercase opacity-50 small">Category</label>
                                        <select class="form-select" name="category" required>
                                            <option value="" selected disabled>Select a category</option>
                                            <option value="fiction">Fiction</option>
                                            <option value="non-fiction">Non-Fiction</option>
                                            <option value="mystery">Mystery & Thriller</option>
                                            <option value="science">Science & Technology</option>
                                            <option value="biography">Biography & Memoir</option>
                                            <option value="history">History</option>
                                            <option value="business">Business & Economics</option>
                                            <option value="poetry">Poetry</option>
                                            <option value="children">Children's Books</option>
                                            <option value="young-adult">Young Adult</option>
                                            <option value="fantasy">Fantasy</option>
                                            <option value="romance">Romance</option>
                                            <option value="self-help">Self-Help</option>
                                            <option value="art">Art & Photography</option>
                                            <option value="comics">Comics & Graphic Novels</option>
                                        </select>
                                    </div>

                                    <div class="mb-3 col-md">
                                        <label class="text-muted mb-1 text-uppercase opacity-50 small">Quantity</label>
                                        <input type="number" class="form-control" name="quantity" placeholder="Total Quantity" min="1" value="1" required>
                                    </div>
                                </div>
                                
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                <button type="submit" class="btn btn-primary">Save Book</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>

            <div class="modal fade" id="view-book-modal" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-hidden="true">
                <div class="modal-dialog modal-dialog-centered modal-lg">
                    <div class="modal-content">
                        <div class="modal-header border-bottom-0">
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body p-4">
                            <div class="row align-items-start g-4">
                                <div class="col-md-8">
                                    <div class="pe-md-4">
                                        <div class="mb-4">
                                            <label class="text-muted opacity-50 small text-uppercase mb-2">Title</label>
                                            <div class="book-title h5 mb-0"></div>
                                        </div>
                                    
                                        <div class="mb-4">
                                            <label class="text-muted opacity-50 small text-uppercase mb-2">Author</label>
                                            <div class="book-author h5 mb-0"></div>
                                        </div>
                                        
                                        <div class="mb-4">
                                            <label class="text-muted opacity-50 small text-uppercase mb-2">Category</label>
                                            <div class="book-category h5 mb-0 text-capitalize"></div>
                                        </div>
                                        
                                        <div>
                                            <label class="text-muted opacity-50 small text-uppercase mb-2">Description</label>
                                            <div class="book-description"></div>
                                        </div>
                                    </div>
                                </div>
                                
                                <div class="col-md-4">
                                    <div class="text-center">
                                        <div class="bg-dark bg-opacity-75 rounded-4 pb-4 px-4 mb-2">
                                            <i class="bi bi-book text-white opacity-75 display-1"></i>
                                        </div>
                                        
                                        <div class="mb-5">
                                            <div class="book-status"></div>
                                        </div>
                                        
                                        <div class="list-group list-group-flush border rounded-3">
                                            <div class="list-group-item bg-transparent">
                                                <small class="text-muted opacity-50 text-uppercase d-block mb-1">Total Quantity</small>
                                                <span class="book-quantity h5 mb-0"></span>
                                            </div>
                                            <div class="list-group-item bg-transparent">
                                                <small class="text-muted opacity-50 text-uppercase d-block mb-1">Available</small>
                                                <span class="book-available h5 mb-0"></span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="modal fade" id="borrow-book-modal" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-hidden="true">
                <div class="modal-dialog modal-dialog-centered">
                    <div class="modal-content">
                        <form action="${pageContext.request.contextPath}/customer" method="POST" id="borrowBookForm">
                            <input type="hidden" name="book_id">
                            <div class="modal-header border-bottom-0">
                                <h5 class="modal-title">Borrow Book</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body">
                                <div class="form-floating mb-3">
                                    <input type="text" class="form-control" name="cin" placeholder="CIN" required>
                                    <label class="text-muted mb-1 text-uppercase">CIN</label>
                                </div>
                                <div class="form-floating mb-3">
                                    <input type="text" class="form-control" name="name" placeholder="Name">
                                    <label class="text-muted mb-1 text-uppercase">Name</label>
                                </div>
                                <div class="form-floating mb-3">
                                    <input type="email" class="form-control" name="email" placeholder="Email">
                                    <label class="text-muted mb-1 text-uppercase">Email</label>
                                </div>
                                
                                <div class="form-floating">
                                    <input type="text" 
                                        class="form-control" 
                                        name="expected_return_at" 
                                        id="returnDatePicker"
                                        placeholder="Expected Return Date"
                                        required>
                                    <label class="text-muted mb-1 text-uppercase">Expected Return Date</label>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                <button type="submit" class="btn btn-primary">Borrow</button>
                            </div>
                        </form>
                    </div>
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
                                    <input type="text" class="form-control" name="cin" placeholder="CIN" required>
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


            <div class="offcanvas offcanvas-start" style="min-width: 1200px;" tabindex="-1" id="historyOffcanvas" aria-labelledby="historyOffcanvasLabel">
                <div class="offcanvas-header border-bottom-0">
                    <h5 class="offcanvas-title" id="historyOffcanvasLabel">Borrowing History</h5>
                    <button type="button" class="btn-close text-reset" data-bs-dismiss="offcanvas" aria-label="Close"></button>
                </div>
                <div class="offcanvas-body p-0">
                    <div class="p-2">

                        <form method="GET" action="${pageContext.request.contextPath}/borrow" class="d-flex gap-3" id="filterForm">
                        </form>

                        <div id="borrow-table-container" style="display:none;">
                            <table class="table table-borderless table-striped rounded-3 overflow-hidden bg-dark">
                                <thead class="table-dark">
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
                                    <li class="btn btn-sm btn-dark me-2 jq-pagination" onclick="return borrowPagination.prevPage(event)">
                                        <i class="bi bi-chevron-left"></i>
                                    </li>
                                    <li class="btn btn-sm btn-dark jq-pagination" onclick="return borrowPagination.nextPage(event)">
                                        <i class="bi bi-chevron-right"></i>
                                    </li>
                                </ul>
                            </nav>
                        </div>

                        <div id="borrow-warning" class="alert alert-warning text-center" style="display:none;">
                            <div class="mb-2">
                                <i class="bi bi-clock-history fs-2 mb-2 d-block"></i> This book wasn't borrowed yet!
                            </div>
                        </div>
                    </div>
                </div>
            </div>

        </div>

    </jsp:body>

</t:auth-layout>