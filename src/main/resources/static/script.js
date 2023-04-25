window.addEventListener('keypress', (event) => {
    switch (event.key) {
        case 'h':
            document.getElementById('hit-button').click();
            break;

        case 's':
            document.getElementById('stand-button').click();
            break;
    }
});
