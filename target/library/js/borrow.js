
import { borrowTemplate } from './templates.data.js';
import { borrowHistoryManagerFactory } from './factories.js';

const manager = await borrowHistoryManagerFactory(document.getElementById('borrow-table-container'), borrowTemplate, {
    return: (params, manager) => {
        const modal = new bootstrap.Modal(document.getElementById('return-book-modal'));
        const form = document.getElementById('returnBookForm');
        form.reset();
    
        const modalTitle = form.closest('.modal-content').querySelector('.modal-title');
        modalTitle.textContent = `Return "${params.item.book.title}"`;
        
        form.querySelector('[name="borrow_id"]').value = params.item.id;
        form.querySelector('[name="book_id"]').value = params.item.book.id;
        form.querySelector('[name="cin"]').value = params.item.customer.cin;
        modal.show();
    },
}, 1, 25);

window.pagination = manager.pagination();

const debouncedHandleFilterForm = debounce(handleFilterForm, 300);
document.querySelector('#searchInput').addEventListener('keyup', debouncedHandleFilterForm);

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
            
            const id = parseInt(form.borrow_id.value);
            const index = manager.items.findIndex(borrow => borrow.id === id);
            
            if (index !== -1) {
                manager.items[index].book.availableQuantity++;
                manager.items[index].returnedAt = currentTimestamp();
                manager.render();
            }

            const modal = bootstrap.Modal.getInstance(document.getElementById('return-book-modal'));
            form.reset();
            modal.hide();
        })
        .catch((err) => showNotification(err.message || "Something went wrong", 500))
        .finally(() => form.loader().hide());
});


async function handleFilterForm(event) {
    await manager.refresh(1, 25)
}

