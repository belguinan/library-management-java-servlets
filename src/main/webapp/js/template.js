class TemplateManager {
    constructor(containerId, template) {
        this.items = [];
        this.container = document.querySelector(containerId);
        this.template = template;
        this.container.addEventListener('click', this.handleClick.bind(this));
    }

    handleClick(event) {
        const button = event.target.closest('[jq-data-action]');
        
        if (!button) return;

        const action = button.getAttribute('jq-data-action');
        const id = parseInt(button.dataset.id);
        const index = parseInt(button.dataset.index);
        
        this.fire(action, {item: this.items[index], index}, this);
    }
    
    setEvents(events = {}) {
        this.events = events;
        return this;
    }

    escapeValue(str) {
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
    
    setItems(items) {
        this.items = items;
        return this;
    }
    
    add(item, unshift = true) {
        if (unshift) {
            this.items.unshift(item);
        } else {
            this.items.push(item);
        }
        
        return this;
    }
    
    remove(index) {
        this.items.splice(index, 1);
        return this;
    }
    
    update(newItem) {
        const index = this.items.findIndex(item => item.id === newItem.id);
        if (index !== -1) {
            this.items[index] = newItem;
        }
        return this;
    }
    
    fire(event, ...args) {
        this.events[event](...args);
    }
    
    render() {
        const self = this;
        const html = this.items.map((item, index) => {
            const values = { ...item, index, escaped: (val) => this.escapeValue(val) };
            return this.template(
                values, 
                index, 
                (value) => this.escapeValue(value), 
                self
            );
        }).join('');
        
        this.container.innerHTML = html;
    }
}