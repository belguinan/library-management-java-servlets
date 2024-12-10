
window.toggleDisplay = (cond, elements) => {
    if(cond) {
        elements.hideWhenTrue.style.display = 'none';
        elements.showWhenTrue.style.display = 'block';
        return;
    }

    elements.hideWhenTrue.style.display = 'block';
    elements.showWhenTrue.style.display = 'none';
}

window.showNotification = (message, status) => {
    const toast = document.getElementById('notificationToast');
    const toastBody = toast.querySelector('.toast-body');
    
    // Set message
    toastBody.textContent = message;
    
    // Reset classes
    toast.classList.remove('text-bg-success', 'text-bg-danger');
    
    // Add appropriate class based on status
    if (status === 201 || status === 200) {
        toast.classList.add('text-bg-success');
    } else {
        toast.classList.add('text-bg-danger');
    }
    
    // Show toast
    const bsToast = new bootstrap.Toast(toast, {
        autohide: true,
        delay: 5000
    });
    bsToast.show();
}

window.submitForm = (form, method = 'POST', headers = {}) => {
    
    return new Promise((resolve, reject) => {
        const formData = new FormData(form);
        const urlEncodedData = new URLSearchParams(formData).toString();

        jsonFetch(form.action, {
            method: method || 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                'X-Requested-With': 'XMLHttpRequest',
                ...headers
            },
            body: urlEncodedData
        })
        .then(data => resolve(data))
        .catch(err => reject(err))
    })
}

window.jsonFetch = (url, params) => {
    return new Promise((resolve, reject) => {
        fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                'X-Requested-With': 'XMLHttpRequest',
            },
            ...params
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Something went wrong');
                }
                return response.json();
            })
            .then(data => {
                resolve(data)
            })
            .catch(error => {
                reject(error)
            });
    })
}

function currentTimestamp() {
    return Math.floor(Date.now());
}

function formatTimestamp(timestamp) {
    const date = new Date(timestamp);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hours = date.getHours();
    const minutes = String(date.getMinutes()).padStart(2, '0');
    const ampm = hours >= 12 ? 'PM' : 'AM';
    const formattedHours = String(hours % 12 || 12).padStart(2, '0');

    return `${month}/${day}/${year} ${formattedHours}:${minutes} ${ampm}`;
}

function scapeHtml(str) {
    if (typeof str !== 'string') return str;
    const entityMap = {
        '&': '&amp;',
        '<': '&lt;',
        '>': '&gt;',
        '"': '&quot;',
        "'": '&#39;',
        '/': '&#x2F;',
        '`': '&#x60;',
        '=': '&#x3D;',
        '{': '&#x7b;',
        '}': '&#x7d;',
        '[': '&#x5b;',
        ']': '&#x5d;'
    };
    return str.replace(/[&<>"'`={}[\]/]/g, s => entityMap[s]);
}

function debounce(callback, delay = 1000) {
    var time;
  
    return (...args) => {
        clearTimeout(time);
        time = setTimeout(() => {
            callback(...args);
        }, delay);
    };
  }