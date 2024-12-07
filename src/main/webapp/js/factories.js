export async function borrowHistoryManagerFactory(parentElm, template, events, page, perPage) {

    async function getPage(page = 1, perPage = 25) {

        document.body.loader().show();
    
        const form = document.querySelector('#filterForm')
        
        const formParams = (new URLSearchParams(form === null ? new FormData() : new FormData(form))).toString()
        
        const response = await jsonFetch(`${pageContextPath}/borrow?page=${page}&perPage=${perPage}&${formParams}`, {
            method: "GET"
        })
    
        parentElm.querySelector('.current-page').innerHTML = response?.data?.currentPage || 1;
        parentElm.querySelector('.current-total-items').innerHTML = response?.data?.totalItems || 0;
    
        document.body.loader().hide();
    
        return response;
    }

    const borrowHistoryManager = new TemplateManager('#borrow-container', template);

    borrowHistoryManager.refresh = async (page, perPage) => {

        const response = await getPage(page, perPage)

        borrowHistoryManager.previousResponse = response
    
        if (response?.data?.data?.length) {
            borrowHistoryManager.setItems(response.data.data);
        }

        if (page === 1) {
            toggleDisplay(response?.data?.data?.length > 0, {
                showWhenTrue: document.querySelector('#borrow-table-container'),
                hideWhenTrue: document.querySelector('#borrow-warning'),
            });
        }
        
        borrowHistoryManager.setEvents(events).render();
    }

    borrowHistoryManager.pagination = () => {
        let currentPage = page;
        const itemsPerPage = perPage;
    
        return {
            page: currentPage,
            perPage: itemsPerPage,
            nextPage: async () => {
                if (currentPage >= borrowHistoryManager.previousResponse.data.totalPages) {
                    return;
                }
                currentPage++;
                await borrowHistoryManager.refresh(currentPage, itemsPerPage);
            },
            prevPage: async () => {
                if (currentPage < 2) {
                    return;
                }
                currentPage--;
                await borrowHistoryManager.refresh(currentPage, itemsPerPage);
            }
        }
    }

    await borrowHistoryManager.refresh(page, perPage)
    
    return borrowHistoryManager;
}