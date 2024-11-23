// modalFunctions.js

// Función para abrir el modal de registro
function openModal() {
    const modal = document.getElementById("registerModal");
    modal.style.display = "block";
    setTimeout(() => {
        modal.classList.add("show");
    }, 10);
}

// Función para cerrar los modales
function closeModal(modalId) {
    const modal = document.getElementById(modalId);
    modal.classList.remove("show");
    setTimeout(() => {
        modal.style.display = "none";
    }, 300);

}

// Función para abrir el modal de edición
let selectedCard = null;

function openEditModal(card) {
    selectedCard = card;
    const editModal = document.getElementById("editModal");

    const nombres = card.querySelector(".card-title").textContent;
    const apellidos = card.querySelectorAll(".card-description span")[1].textContent;
    const telefono = card.querySelectorAll(".card-description span")[3].textContent;
    const email = card.querySelectorAll(".card-description span")[5].textContent;
    const password = card.getAttribute("data-password");

    document.getElementById("editNombres").value = nombres;
    document.getElementById("editApellidos").value = apellidos;
    document.getElementById("editTelefono").value = telefono;
    document.getElementById("editEmail").value = email;
    document.getElementById("editPassword").value = password;
    document.getElementById("editConfirmPassword").value = password;

    editModal.style.display = "block";
    setTimeout(() => {
        editModal.classList.add("show");
    }, 10);

      // Agregar evento para eliminar highlight al cerrar el modal
      const closeModalButton = document.querySelector(".close-button");
      closeModalButton.addEventListener("click", () => {
          removeHighlightOnModalClose();
      });
}

// Cerrar el modal al hacer clic fuera del contenido
window.onclick = function (event) {
    if (event.target.classList.contains('modal')) {
        closeModal(event.target.id);
    }
};

function addEditIconHandler(card) {
    const editIcon = card.querySelector('.card-edit-icon');
    editIcon.addEventListener('click', (event) => {
        event.stopPropagation(); // Evitar que otros eventos de clic se activen
        openEditModal(card);
    });
}