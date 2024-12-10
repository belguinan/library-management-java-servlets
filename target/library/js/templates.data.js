export const bookTemplate = (book, index, escaped, manager) => `
    <tr>
        <td class="align-middle px-3 py-3">
            <span class="badge rounded-pill bg-dark-subtle text-white-50">#${escaped(book.id)}</span>
        </td>
        <td class="align-middle px-3 py-3">
            <span class="text-white text-capitalize">${escaped(book.title)}</span>
        </td>
        <td class="align-middle px-3 py-3 text-muted text-capitalize">${escaped(book.author)}</td>
        <td class="align-middle px-3 py-3 text-muted text-uppercase">
                ${escaped(book.category)}
        </td>
        <td class="align-middle px-3 py-3">
            ${book.availableQuantity > 3 ? `
                <div style="min-height: 25px; min-width: 150px; line-height: 1.4rem;" class="badge bg-success bg-opacity-25 position-relative">
                    <i class="bi bi-check-circle-fill text-success me-1"></i>
                    <span class="text-success">IN STOCK</span>
                    <span class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-success">${book.availableQuantity}</span>
                </div>
            ` : book.availableQuantity >= 1 ? `
                <div style="min-height: 25px; min-width: 150px; line-height: 1.4rem;" class="badge bg-warning bg-opacity-25 position-relative">
                    <i class="bi bi-exclamation-circle-fill text-warning me-1"></i>
                    <span class="text-warning">LOW STOCK</span>
                    <span class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-warning text-dark">${book.availableQuantity}</span>
                </div>
            ` : `
                <div style="min-height: 25px; min-width: 150px; line-height: 1.4rem;" class="badge bg-danger bg-opacity-25">
                    <i class="bi bi-x-circle-fill text-danger me-1"></i>
                    <span class="text-danger">OUT OF STOCK</span>
                    </div>
            `}
        </td>
        <td class="align-middle px-3 py-3 text-end">
            <div class="d-flex gap-2 justify-content-end">
                <div class="btn-group me-">
                    <button jq-data-action="edit" data-id="${book.id}" data-index="${index}" 
                            class="btn border-0 btn-dark-subtle px-2" title="Edit">
                        <i class="bi bi-pencil text-white-50"></i>
                    </button>
                    <button jq-data-action="view" data-id="${book.id}" data-index="${index}" 
                            class="btn border-0 btn-dark-subtle px-2" title="View">
                        <i class="bi bi-eye text-white-50"></i>
                    </button>
                    <button jq-data-action="history" data-id="${book.id}" data-index="${index}" 
                            class="btn border-0 btn-dark-subtle px-2" title="History">
                        <i class="bi bi-clock-history text-white-50"></i>
                    </button>
                    <button jq-data-action="delete" data-id="${book.id}" data-index="${index}" 
                            class="btn border-0 btn-dark-subtle px-2" title="Delete">
                        <i class="bi bi-trash text-danger"></i>
                    </button>
                </div>

                <div class="btn-group">
                    <button ${book.availableQuantity < book.quantity ? 'jq-data-action="return"' : 'disabled'} 
                        data-id="${book.id}" data-index="${index}"
                        class="btn border-0 btn-dark-subtle text-white ${book.availableQuantity < book.quantity ? '' : 'opacity-75'}"
                        title="Return"
                    >
                        <span class="small fw-medium" style="font-size: 11px;">RETURN</span>
                    </button>
                    <button ${book.availableQuantity === 0 ? 'disabled' : 'jq-data-action="borrow"'}
                        data-id="${book.id}" data-index="${index}"
                        class="btn border-0 btn-primary ${book.availableQuantity === 0 ? 'opacity-25' : ''}"
                        title="Borrow"
                    >
                        <span class="small fw-medium" style="font-size: 11px;">BORROW</span>
                    </button>
                </div>
            </div>
        </td>
    </tr>
`;

export const borrowTemplate = (borrow, index, escaped, manager) => `
    <tr>
        <td class="align-middle px-3 py-3">
            <span class="badge rounded-pill bg-dark-subtle text-white-50">#${escaped(borrow?.book?.id)}</span>
        </td>
        <td class="align-middle px-3 py-3">
            <span class="text-muted text-capitalize">${escaped(borrow?.book?.title || 'N/A')}</span>
        </td>
        <td class="align-middle px-3 py-3 text-muted text-capitalize">${escaped(borrow?.book?.author || 'N/A')}</td>
        <td class="align-middle px-3 py-3 text-muted">
            <a href="mailto:${escaped(borrow?.customer?.email || 'N/A')}" class="text-decoration-none text-white">
                ${escaped(borrow?.customer?.email || 'N/A')}
            </a>
        </td>
        <td class="align-middle px-3 py-3 text-muted">
            ${formatTimestamp(borrow?.createdAt)}
        </td>
        <td class="align-middle px-3 py-3 text-muted text-uppercase">
            <div style="min-height: 25px; min-width: 100px; line-height: 1.4rem;" class="badge bg-success bg-opacity-10 position-relative">
                <span class="text-success">
                    ${escaped(borrow?.customer?.cin || 'N/A')}
                </span>
            </div>
        </td>
        <td class="align-middle px-3 py-3">
            ${borrow.returnedAt !== null ? `
                <div style="min-height: 25px; min-width: 165px; line-height: 1.4rem;" class="badge bg-success bg-opacity-10 position-relative">
                    <i class="bi bi-check-circle-fill text-success me-1"></i>
                    <span class="text-success">${formatTimestamp(borrow?.returnedAt)}</span>
                </div>
            ` : (borrow.returnedAt === null && borrow.expectedReturnAt > currentTimestamp()) ? `
                <div style="min-height: 25px; min-width: 165px; line-height: 1.4rem;" class="badge bg-warning bg-opacity-10 position-relative">
                    <i class="bi bi-exclamation-circle-fill text-warning me-1"></i>
                    <span class="text-warning">${formatTimestamp(borrow.expectedReturnAt)}</span>
                    <span class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-warning text-dark small text-uppercase">expected</span>
                </div>
            ` : `
                <div style="min-height: 25px; min-width: 165px; line-height: 1.4rem;" class="badge bg-danger bg-opacity-25">
                    <i class="bi bi-x-circle-fill text-danger me-1"></i>
                    <span class="text-danger">${formatTimestamp(borrow?.expectedReturnAt)}</span>
                    <span class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-warning text-dark">PAST DUE</span>
                </div>
            `}
        </td>
        <td class="align-middle px-3 py-3 text-end">
            <div class="d-flex gap-2 justify-content-end">
                <div class="btn-group">
                    <button ${borrow.returnedAt === null && borrow?.book?.availableQuantity < borrow?.book?.quantity ? 'jq-data-action="return"' : 'disabled'} 
                        data-id="${borrow?.book?.id}" data-cin="${borrow?.customer?.cin}" data-index="${index}"
                        class="btn border-0 btn-dark-subtle text-white ${borrow.returnedAt === null && borrow?.book?.availableQuantity < borrow?.book?.quantity ? '' : 'opacity-25'}"
                        title="Return"
                    >
                        <span class="small fw-medium" style="font-size: 11px;">RETURN</span>
                    </button>
                </div>
            </div>
        </td>
    </tr>
`