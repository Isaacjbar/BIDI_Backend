// filterFunctions.js

// Cambia el texto y filtra las cards según el estado (activo o inactivo)
document.getElementById("statusSwitch").addEventListener("change", function () {
    const statusText = document.getElementById("statusText");
    const showActive = this.checked;
    statusText.textContent = showActive ? "Activos" : "Inactivos";

    // Filtra las cards según el estado del switch
    filterCards(showActive ? "activo" : "inactivo");
});

// Función para filtrar las cards según el estado activo o inactivo
function filterCards(status) {
    const cards = document.querySelectorAll('.card');
    cards.forEach(card => {
        card.style.display = card.getAttribute('data-status') === status ? "block" : "none";
    });
}
