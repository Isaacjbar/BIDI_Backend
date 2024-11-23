// menuFunctions.js

document.addEventListener('DOMContentLoaded', () => {
    const menuToggle = document.querySelector('.menu-toggle');
    const sideMenu = document.querySelector('.side-menu');

    function toggleMenu() {
        sideMenu.classList.toggle('active');
    }

    menuToggle.addEventListener('click', toggleMenu);

    const closeBtn = document.querySelector('.close-btn');
    if (closeBtn) {
        closeBtn.addEventListener('click', toggleMenu);
    }
});
