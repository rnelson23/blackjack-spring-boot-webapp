window.addEventListener('keypress', (event) => {
    if (event.key === 'h') {
        document.getElementById('hit-button').click();
    }

    if (event.key === 's') {
        document.getElementById('stand-button').click();
    }
});
