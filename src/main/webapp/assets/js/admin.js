document.addEventListener('DOMContentLoaded', () => {
    const addButton = document.querySelector('#add-option');
    const optionList = document.querySelector('.option-inputs');
    if (!addButton || !optionList) return;
    let nextOption = Array.from(optionList.querySelectorAll('input[name="options"]'))
        .reduce((highest, input) => {
            const match = input.id.match(/\d+$/);
            return Math.max(highest, match ? Number(match[0]) : 0);
        }, 0) + 1;

    optionList.querySelectorAll('.remove-option').forEach((button) => {
        button.addEventListener('click', () => button.closest('.option-input-row').remove());
    });

    addButton.addEventListener('click', () => {
        const count = nextOption++;
        const row = document.createElement('div');
        row.className = 'option-input-row';
        const label = document.createElement('label');
        label.className = 'sr-only';
        label.htmlFor = `option-${count}`;
        label.textContent = `Option ${count}`;
        const input = document.createElement('input');
        input.id = `option-${count}`;
        input.name = 'options';
        input.required = true;
        input.maxLength = 180;
        input.placeholder = `Option ${count}`;
        const remove = document.createElement('button');
        remove.className = 'remove-option';
        remove.type = 'button';
        remove.textContent = '×';
        remove.setAttribute('aria-label', `Remove option ${count}`);
        remove.addEventListener('click', () => row.remove());
        row.append(label, input, remove);
        optionList.append(row);
        input.focus();
    });
});
