import { Datepicker } from 'https://cdn.jsdelivr.net/npm/vanillajs-datepicker@1.3.4/+esm'
import { bookTemplate, borrowTemplate } from './templates.data.js';
import { borrowHistoryManagerFactory } from './factories.js';

const events = {

    borrow: (params, manager) => {
        const modal = new bootstrap.Modal(document.getElementById('borrow-book-modal'));
        
        const form = document.getElementById('borrowBookForm');
        form.reset();

        const modalTitle = form.closest('.modal-content').querySelector('.modal-title');
        modalTitle.textContent = `Borrow "${params.item.title}"`;
        
        form.querySelector('[name="book_id"]').value = params.item.id;
        modal.show();
    },
    
    edit: (params, manager) => {
        const modal = new bootstrap.Modal(document.getElementById('add-book-modal'));
        const form = document.getElementById('addBookForm');
        const modalTitle = form.closest('.modal-content').querySelector('.modal-title');
        const originalTitle = modalTitle.textContent;
        const originalAction = form.action;
        
        const submitBtn = form.querySelector('button[type="submit"]');
        const originalBtnText = submitBtn.textContent;
        
        let methodInput = document.createElement('input');
        methodInput.type = 'hidden';
        methodInput.name = '_method';
        methodInput.value = 'PUT';
        form.appendChild(methodInput);
        
        let idInput = document.createElement('input');
        idInput.type = 'hidden';
        idInput.name = 'id';
        idInput.value = params.item.id;
        form.appendChild(idInput);
        
        form.action = `${pageContextPath}/book/${params.item.id}`;
        
        modalTitle.textContent = `Update Book #${params.item.id}`;
        submitBtn.textContent = 'Update Book';
        
        form.title.value = params.item.title;
        form.author.value = params.item.author;
        form.description.value = params.item.description;
        form.category.value = params.item.category;
        form.quantity.value = params.item.quantity;
        
        modal.show();
        
        document.getElementById('add-book-modal').addEventListener('hidden.bs.modal', function() {
            form.removeChild(methodInput);
            form.removeChild(idInput);
            form.reset();
            form.action = originalAction;
            modalTitle.textContent = originalTitle; 
            submitBtn.textContent = originalBtnText;
        }, {once: true});
    },
    
    view: (params, manager) => {
        const modal = new bootstrap.Modal(document.getElementById('view-book-modal'));
        const modalContent = document.getElementById('view-book-modal');
        
        modalContent.querySelector('.book-title').textContent = scapeHtml(params.item.title);
        modalContent.querySelector('.book-author').textContent = scapeHtml(params.item.author);
        modalContent.querySelector('.book-category').textContent = scapeHtml(params.item.category);
        modalContent.querySelector('.book-description').textContent = scapeHtml(params.item.description || 'No description available');
        modalContent.querySelector('.book-quantity').textContent = scapeHtml(params.item.quantity);
        modalContent.querySelector('.book-available').textContent = scapeHtml(params.item.availableQuantity);
        
        const status = modalContent.querySelector('.book-status');
        if (params.item.availableQuantity > 3) {
            status.className = 'bg-success';
            status.textContent = 'IN STOCK';
        } else if (params.item.availableQuantity >= 1) {
            status.className = 'bg-warning';
            status.textContent = 'LOW STOCK';
        } else {
            status.className = 'bg-danger';
            status.textContent = 'OUT OF STOCK';
        }

        status.className = `book-status badge  ${status.className}`
        
        modal.show();
    },
    
    delete: async (params, manager) => {

        document.querySelector('#app').loader().show();

        jsonFetch(`${pageContextPath}/book/${params.item.id}`, {
            body: new URLSearchParams({...params.item, _method: "DELETE"}).toString()
        })
        .then(response => {

            if(response.status == 201) {
                manager.remove(params.index);
            }
            
            showNotification(response.message || "Item deleted successfully", response.status)

            manager.render()

            toggleDisplay(manager.items?.length > 0, {
                showWhenTrue: document.querySelector('#books-table-container'),
                hideWhenTrue: document.querySelector('#books-warning'),
            });
        })
        .catch(err => showNotification("Something went wrong", 500))
        .finally(() => document.querySelector('#app').loader().hide())
    },
    
    return: (params, manager) => {
        const modal = new bootstrap.Modal(document.getElementById('return-book-modal'));
        const form = document.getElementById('returnBookForm');
        form.reset();
    
        const modalTitle = form.closest('.modal-content').querySelector('.modal-title');
        modalTitle.textContent = `Return "${params.item.title}"`;
        
        form.querySelector('[name="book_id"]').value = params.item.id;
        modal.show();
    },

    history: async (params, manager) => {
        const offanvasElm = document.getElementById('historyOffcanvas');
        const historyOffcanvas = bootstrap.Offcanvas.getOrCreateInstance(offanvasElm);

        const bookIdElm = document.createElement('input')
        bookIdElm.type = 'hidden';
        bookIdElm.name = 'book_id';
        bookIdElm.value = params.item.id;

        offanvasElm.querySelector('form').appendChild(bookIdElm)
        
        const borrowManager = await borrowHistoryManagerFactory(offanvasElm, borrowTemplate, {
            return: (params, manager) => {
                const modal = bootstrap.Modal.getOrCreateInstance(document.getElementById('return-book-modal'))
                
                const form = document.getElementById('returnBookForm');
                form.reset();
            
                const modalTitle = form.closest('.modal-content').querySelector('.modal-title');
                modalTitle.textContent = `Return "${params.item.book.title}"`;
                form.querySelector('[name="borrow_id"]').value = params.item.id;
                form.querySelector('[name="book_id"]').value = params.item.book.id;
                form.querySelector('[name="cin"]').value = params.item.customer.cin;

                historyOffcanvas.hide();
                modal.show();
            }
        }, 1, 25)

        window.borrowPagination = borrowManager.pagination;

        offanvasElm.addEventListener('hidden.bs.offcanvas', event => {
            bookIdElm.remove()
        })

        historyOffcanvas.show();
    }
}

const booksManager = await booksManagerFactory(bookTemplate, events, 1, 25);

window.pagination = booksManager.pagination();

initBorrowModalDateRange();









document.getElementById('addBookForm').addEventListener('submit', function(event) {
    event.preventDefault();
    const form = event.target;
    
    form.loader().show();
    
    submitForm(form)
        .then((response) => {
            if(response.status == 419) {
                showNotification(response.message, response.status);
                return;
            }
 
            const isUpdate = form.querySelector('input[name="_method"]')?.value === 'PUT';
            const message = isUpdate ? "Book updated successfully" : "New book created successfully";
            
            showNotification(message, response.status);
 
            if (isUpdate) {
                booksManager.update(response.data)
            } else {
                booksManager.add(response.data);
            }
 
            booksManager.render();

            toggleDisplay(booksManager.items?.length > 0, {
                showWhenTrue: document.querySelector('#books-table-container'),
                hideWhenTrue: document.querySelector('#books-warning'),
            });

            const modal = bootstrap.Modal.getInstance(document.getElementById('add-book-modal'));
            form.reset();

            modal.hide();
        })
        .catch((err) => showNotification(err.message, 500))
        .finally(() => form.loader().hide());
});

document.getElementById('borrowBookForm').addEventListener('submit', async function(event) {
    event.preventDefault();
    const form = event.target;
    
    form.loader().show();
    
    try {

        const customerResponse = await submitForm(form)

        if (customerResponse.status !== 201) {
            throw new Error(customerResponse.message || "Error processing customer request")
        }

        const borrowResponse = await jsonFetch(`${pageContextPath}/borrow`, {
            method: 'POST',
            body: new URLSearchParams({
                customer_id: customerResponse.data.id,
                book_id: form.book_id.value,
                expected_return_at: form.expected_return_at.value
            }).toString()
        });

        if (borrowResponse.status !== 201) {
            throw new Error(borrowResponse.message || "Error processing borrow request")
        }
    
        showNotification("Book borrowed successfully", borrowResponse.status);
        
        const bookId = parseInt(form.book_id.value);
        const bookIndex = booksManager.items.findIndex(book => book.id === bookId);
        
        if (bookIndex !== -1) {
            booksManager.items[bookIndex].availableQuantity--;
            booksManager.render();
        }

        const modal = bootstrap.Modal.getInstance(document.getElementById('borrow-book-modal'));

        form.reset();

        modal.hide();
    
    } catch (error) {
        showNotification(error.message || "Somthing went wrong", 500);
    } finally {
        form.loader().hide();
    }
});

document.getElementById('returnBookForm').addEventListener('submit', function(event) {
    event.preventDefault();
    const form = event.target;
    
    form.loader().show();
    
    submitForm(form)
        .then((response) => {
            if(response.status !== 201) {
                throw new Error(response.message || "Error processing return request");
            }
            
            showNotification("Book returned successfully", response.status);
            
            const bookId = parseInt(form.book_id.value);
            const bookIndex = booksManager.items.findIndex(book => book.id === bookId);
            
            if (bookIndex !== -1) {
                booksManager.items[bookIndex].availableQuantity++;
                booksManager.render();
            }

            const modal = bootstrap.Modal.getInstance(document.getElementById('return-book-modal'));
            form.reset();
            modal.hide();
        })
        .catch((err) => showNotification(err.message || "Something went wrong", 500))
        .finally(() => form.loader().hide());
});

async function handleFilterForm(event) {
    await booksManager.refresh(1, 25)
}

const debouncedHandleFilterForm = debounce(handleFilterForm, 300);
document.querySelector('#searchInput').addEventListener('keyup', debouncedHandleFilterForm);
document.querySelectorAll('.jq-filter').forEach(elm => elm.addEventListener('change', debouncedHandleFilterForm))











async function getPage(page = 1, perPage = 25) {

    document.body.loader().show();

    const form = document.getElementById('filter')
    
    const formParams = (new URLSearchParams(new FormData(form))).toString()
    
    const response = await jsonFetch(`${pageContextPath}/book?page=${page}&perPage=${perPage}&${formParams}`, {
        method: "GET"
    })

    document.querySelector('#current-page').innerHTML = response?.data?.currentPage || 1;

    document.querySelector('#current-total-items').innerHTML = response?.data?.totalItems || 0;

    document.body.loader().hide();

    return response;
}

async function booksManagerFactory(bookTemplate, events, page, perPage) {

    const booksManager = new TemplateManager('#books-container', bookTemplate);

    booksManager.refresh = async (page, perPage) => {

        const response = await getPage(page, perPage)

        booksManager.previousResponse = response
    
        if (response?.data?.data?.length) {
            booksManager.setItems(response.data.data);
        }

        if (page === 1) {
            toggleDisplay(response?.data?.data?.length > 0, {
                showWhenTrue: document.querySelector('#books-table-container'),
                hideWhenTrue: document.querySelector('#books-warning'),
            });
        }
        
        booksManager.setEvents(events).render();
    }

    booksManager.pagination = () => {
        let currentPage = page;
        const itemsPerPage = perPage;
    
        return {
            page: currentPage,
            perPage: itemsPerPage,
            nextPage: async () => {
                if (currentPage >= booksManager.previousResponse.data.totalPages) {
                    return;
                }
                currentPage++;
                await booksManager.refresh(currentPage, itemsPerPage);
            },
            prevPage: async () => {
                if (currentPage < 2) {
                    return;
                }
                currentPage--;
                await booksManager.refresh(currentPage, itemsPerPage);
            }
        }
    }

    await booksManager.refresh(page, perPage)
    
    return booksManager;
}


function initBorrowModalDateRange() {
    const returnDateInput = document.getElementById('returnDatePicker');

    const datepicker = new Datepicker(returnDateInput, {
        format: 'yyyy-mm-dd',
        minDate: new Date(),
        maxDate: (() => {
            const max = new Date();
            max.setDate(max.getDate() + 15);
            return max;
        })(),
        todayHighlight: true,
        todayBtn: true,
        clearBtn: false,
        autoclose: true
    });

    document.getElementById('borrow-book-modal').addEventListener('show.bs.modal', function() {
        const today = new Date();
        const maxDate = new Date();
        maxDate.setDate(today.getDate() + 15);
        
        datepicker.setOptions({
            minDate: today,
            maxDate: maxDate
        });
        
        returnDateInput.value = '';
    });
}