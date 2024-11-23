// cardFunctions.js

// Agregar nueva tarjeta desde el formulario de registro
document.getElementById("registerForm").addEventListener("submit", addCard);

function addCard(event) {
    event.preventDefault();

    const nombres = document.getElementById('Nombres').value.trim();
    const apellidos = document.getElementById('Apellidos').value.trim();
    const email = document.getElementById('email').value.trim();
    const telefono = document.getElementById('telefono').value.trim();
    const password = document.getElementById('password').value.trim();
    const confirmPassword = document.getElementById('confirmPassword').value.trim();

    // Validaciones
    if (!nombres || !apellidos || !email || !telefono || !password || !confirmPassword) {
        Swal.fire({ icon: 'error', title: 'Campos incompletos', text: 'Por favor, completa todos los campos obligatorios.' });
        return;
    }
    if (!email.includes('@')) {
        Swal.fire({ icon: 'error', title: 'Correo electrónico inválido', text: 'Por favor, ingresa un correo electrónico válido.' });
        return;
    }
    const telefonoRegex = /^\d{10}$/;
    if (!telefonoRegex.test(telefono)) {
        Swal.fire({ icon: 'error', title: 'Teléfono inválido', text: 'El número de teléfono debe contener exactamente 10 dígitos numéricos.' });
        return;
    }
    if (password !== confirmPassword) {
        Swal.fire({ icon: 'error', title: 'Contraseñas no coinciden', text: 'Asegúrate de que las contraseñas coincidan.' });
        return;
    }
    if (password.length < 8) {
        Swal.fire({ icon: 'error', title: 'Contraseña insegura', text: 'La contraseña debe tener al menos 8 caracteres.' });
        return;
    }
    
    Swal.fire({
        icon: 'success', 
        title: 'Registro exitoso', 
        text: 'La tarjeta ha sido registrada exitosamente.', 
        didClose: () => {
            const cardContainer = document.querySelector('.card-container');
            const card = document.createElement('div');
            card.classList.add('card');
            card.setAttribute('data-status', 'activo');
            card.setAttribute('data-password', password);
            card.innerHTML = `
                <div class="card-header">
                    <div class="card-icon">
                        <svg viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                            <path d="M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z"/>
                        </svg>
                        <span class="status-indicator"></span>
                    </div>
                    <div class="card-title">${nombres}</div>
                    <svg class="card-edit-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="24" height="24">
                        <path d="M3 17.25V21h3.75L17.81 9.94l-3.75-3.75L3 17.25z"/>
                    </svg>
                </div>
                <div class="card-description">
                    <span class="card-data"><strong>Apellidos:</strong></span><br>
                    <span>${apellidos}</span>
                </div>
                <div class="card-description">
                    <span class="card-data"><strong>Teléfono:</strong></span><br>
                    <span>${telefono}</span>
                </div>
                <div class="card-description">
                    <span class="card-data"><strong>Correo:</strong></span><br>
                    <span>${email}</span>
                </div>
            `;

            setupCardListeners(card);
            addToggleButton(card);
            addEditIconHandler(card);
            initializeScrollReveal();

            cardContainer.appendChild(card);

            closeModal('registerModal');
            document.getElementById('registerForm').reset();
        }
    });
}
// Asignar eventos a las tarjetas existentes en la página
document.addEventListener("DOMContentLoaded", () => {
    initializeScrollReveal();

    // Configurar eventos para las tarjetas existentes
    document.querySelectorAll('.card').forEach(card => {
        setupCardListeners(card);
    });
});

// Función para guardar los cambios en la tarjeta después de editar
document.getElementById("editForm").addEventListener("submit", function (event) {
    event.preventDefault();

    const nombres = document.getElementById("editNombres").value.trim();
    const apellidos = document.getElementById("editApellidos").value.trim();
    const email = document.getElementById("editEmail").value.trim();
    const telefono = document.getElementById("editTelefono").value.trim();
    const password = document.getElementById("editPassword").value.trim();
    const confirmPassword = document.getElementById("editConfirmPassword").value.trim();

    if (!nombres || !apellidos || !email || !telefono || !password || !confirmPassword) {
        Swal.fire({ icon: 'error', title: 'Campos incompletos', text: 'Por favor, completa todos los campos obligatorios.' });
        return;
    }
    if (!email.includes('@')) {
        Swal.fire({ icon: 'error', title: 'Correo electrónico inválido', text: 'Por favor, ingresa un correo electrónico válido.' });
        return;
    }
    const telefonoRegex = /^\d{10}$/;
    if (!telefonoRegex.test(telefono)) {
        Swal.fire({ icon: 'error', title: 'Teléfono inválido', text: 'El número de teléfono debe contener exactamente 10 dígitos numéricos.' });
        return;
    }
    if (password !== confirmPassword) {
        Swal.fire({ icon: 'error', title: 'Contraseñas no coinciden', text: 'Asegúrate de que las contraseñas coincidan.' });
        return;
    }
    if (password.length < 8) {
        Swal.fire({ icon: 'error', title: 'Contraseña insegura', text: 'La contraseña debe tener al menos 8 caracteres.' });
        return;
    }

    Swal.fire({
        icon: 'success', title: 'Edición exitosa', text: 'Los datos han sido actualizados exitosamente.',
    }).then(() => {
        selectedCard.querySelector(".card-title").textContent = nombres;
        selectedCard.querySelectorAll(".card-description span")[1].textContent = apellidos;
        selectedCard.querySelectorAll(".card-description span")[3].textContent = telefono;
        selectedCard.querySelectorAll(".card-description span")[5].textContent = email;
        selectedCard.setAttribute("data-password", password);

        event.target.reset();
        closeModal('editModal');
    });
});

function handleCardClick(event) {
    const card = event.currentTarget;
    // Remover el highlight de la tarjeta previamente seleccionada
    if (selectedCard) {
        selectedCard.classList.remove('highlight');
    }
 
    // Abrir el modal de edición al segundo clic
    if (selectedCard === card) {
        openEditModal(card); // Abre el modal
        return;
    }

    // Asignar la tarjeta actual como seleccionada y agregar el highlight
    selectedCard = card;
    selectedCard.classList.add('highlight');
}

// Función para remover el highlight al hacer clic fuera de las tarjetas
function handleClickOutside(event) {
    if (!event.target.closest('.card')) {
        if (selectedCard) {
            selectedCard.classList.remove('highlight');
            selectedCard = null;
        }
    }
}

// Agregar evento de clic a todas las tarjetas y al documento
document.addEventListener('click', handleClickOutside);

function addCardEventListeners(card) {
    card.addEventListener('click', handleCardClick);
}

// Llamar a esta función después de crear una tarjeta
function setupCardListeners(card) {
    card.addEventListener('click', handleCardClick);
}


// Inicializar ScrollReveal para las tarjetas
function initializeScrollReveal() {
    const cards = document.querySelectorAll('.card:not(.revealed)');

    cards.forEach(card => {
        card.classList.add('revealed');
        // Asignar un delay aleatorio a cada tarjeta
        const randomDelay = Math.random() * 300; // Delay entre 0 y 300 ms

        ScrollReveal().reveal(card, {
            duration: 400,           // Duración rápida de la animación
            distance: '10px',        // Distancia desde la que aparece
            origin: 'bottom',        // Animación desde abajo
            opacity: 0,              // Empieza invisible
            scale: 0.95,             // Comienza con un leve escalado
            easing: 'ease-in-out',   // Entrada y salida suave
            reset: true,             // La animación se reinicia si entra de nuevo en el viewport
            delay: randomDelay       // Delay aleatorio para cada tarjeta
        });
    });
}

