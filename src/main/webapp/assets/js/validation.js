document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('form[data-validate]').forEach((form) => {
        form.addEventListener('submit', (event) => {
            const password = form.querySelector('[name="password"]');
            const confirmation = form.querySelector('[name="confirmPassword"]');
            if (password && confirmation && password.value !== confirmation.value) {
                event.preventDefault();
                confirmation.setCustomValidity('Passwords do not match.');
                confirmation.reportValidity();
                confirmation.addEventListener('input', () => confirmation.setCustomValidity(''), { once: true });
                return;
            }
            if (!form.checkValidity()) {
                event.preventDefault();
                form.reportValidity();
                return;
            }
            const submit = form.querySelector('button[type="submit"]');
            if (submit) {
                submit.disabled = true;
                submit.textContent = 'Please wait…';
            }
        });
    });
});
