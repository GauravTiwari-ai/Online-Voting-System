document.addEventListener('DOMContentLoaded', () => {
    const form = document.querySelector('.vote-form');
    if (!form) return;

    form.addEventListener('submit', (event) => {
        if (!form.querySelector('input[name="optionId"]:checked')) {
            event.preventDefault();
            window.alert('Please choose one option before submitting your vote.');
            return;
        }
        if (!window.confirm(form.dataset.confirm)) {
            event.preventDefault();
            return;
        }
        const button = form.querySelector('button[type="submit"]');
        button.disabled = true;
        button.textContent = 'Submitting…';
    });
});
