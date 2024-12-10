
export const customerTemplate = (customer, index, escaped, manager) => `
    <tr>
        <td class="align-middle px-3 py-3">
            <span class="badge rounded-pill bg-dark-subtle text-white-50">#${escaped(customer?.id)}</span>
        </td>
        <td class="align-middle px-3 py-3 text-muted text-capitalize">${escaped(customer?.name || 'N/A')}</td>
        <td class="align-middle px-3 py-3">
            <a href="mailto:${escaped(customer?.email || 'N/A')}" class="text-decoration-none text-white">
                ${escaped(customer?.email || 'N/A')}
            </a>
        </td>
        <td class="align-middle px-3 py-3 text-muted text-uppercase">
            <div style="min-height: 25px; min-width: 100px; line-height: 1.4rem;" class="badge bg-success bg-opacity-10 position-relative">
                <span class="text-success">
                    ${escaped(customer?.cin || 'N/A')}
                </span>
            </div>
        </td>
        <td class="align-middle px-3 py-3 text-muted">
            ${formatTimestamp(customer.createdAt)}
        </td>
    </tr>
`

const manager = await customerManagerFactory(document.getElementById('customer-table-container'), customerTemplate, {}, 1, 25);

window.pagination = manager.pagination();

export async function customerManagerFactory(parentElm, template, events, page, perPage) {

    async function getPage(page = 1, perPage = 25) {

        document.body.loader().show();
        
        const response = await jsonFetch(`${pageContextPath}/customer?page=${page}&perPage=${perPage}`, {
            method: "GET"
        })
    
        parentElm.querySelector('.current-page').innerHTML = response?.data?.currentPage || 1;
        parentElm.querySelector('.current-total-items').innerHTML = response?.data?.totalItems || 0;
    
        document.body.loader().hide();
    
        return response;
    }

    const customerManager = new TemplateManager('#customer-container', template);

    customerManager.refresh = async (page, perPage) => {

        const response = await getPage(page, perPage)

        console.log('response', response)

        customerManager.previousResponse = response
    
        if (response?.data?.data?.length) {
            customerManager.setItems(response.data.data);
        }

        if (page === 1) {
            toggleDisplay(response?.data?.data?.length > 0, {
                showWhenTrue: document.querySelector('#customer-table-container'),
                hideWhenTrue: document.querySelector('#customer-warning'),
            });
        }
        
        customerManager.setEvents(events).render();
    }

    customerManager.pagination = () => {
        let currentPage = page;
        const itemsPerPage = perPage;
    
        return {
            page: currentPage,
            perPage: itemsPerPage,
            nextPage: async () => {
                if (currentPage >= customerManager.previousResponse.data.totalPages) {
                    return;
                }
                currentPage++;
                await customerManager.refresh(currentPage, itemsPerPage);
            },
            prevPage: async () => {
                if (currentPage < 2) {
                    return;
                }
                currentPage--;
                await customerManager.refresh(currentPage, itemsPerPage);
            }
        }
    }

    await customerManager.refresh(page, perPage)
    
    return customerManager;
}
