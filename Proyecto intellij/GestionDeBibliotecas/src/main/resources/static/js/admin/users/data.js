document.addEventListener("DOMContentLoaded", async function () {

    async function addCards() {

        try {
            const jwt = localStorage.getItem('jwt');
            const response = await fetch("http://localhost:8080/sibi/admin/user/find-all", {
                method: "GET",
                headers: {
                    "Authorization": "Bearer " + jwt,
                    "Content-Type": "application/json",
                    "Accept": "*/*"
                },
                credentials: 'include'
            });

            const data = await response.json();
            if (data.result && Array.isArray(data.result)) {
                const cardContainer = document.querySelector('.card-container');

                data.result.forEach(user => {

                    const card = document.createElement('div');
                    card.classList.add('card');
                    card.setAttribute('data-status', user.estado);
                    card.setAttribute('data-rol', user.rol);

                    card.innerHTML = `
                        <div class="card-header">
                            <div class="card-icon">
                                <svg viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                                    <path
                                        d="M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z"/>
                                </svg>
                                <span class="status-indicator"></span>
                            </div>
                            <div class="card-title">${user.nombre}</div>
                            <svg class="card-edit-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"
                                 width="24" height="24">
                                <path
                                    d="M3 17.25V21h3.75L17.81 9.94l-3.75-3.75L3 17.25zm2.92-2.92L5 17.34V19h1.66l.92-.92-1.66-1.67zm2.83-2.83l1.66 1.66 7.5-7.5-1.66-1.66-7.5 7.5zM20.71 5.63l-2.34-2.34c-.39-.39-1.02-.39-1.41 0l-1.16 1.16 3.75 3.75 1.16-1.16c-.39-.39-.39-1.02 0-1.41z"/>
                            </svg>
                        </div>
                        <div class="card-description">
                            <span class="card-data"><strong>Apellidos:</strong></span><br>
                            <span>${user.apellidos || "No disponible"}</span>
                        </div>
                        <div class="card-description">
                            <span class="card-data"><strong>Teléfono:</strong></span><br>
                            <span>${user.numeroTelefono || "No disponible"}</span>
                        </div>
                        <div class="card-description">
                            <span class="card-data"><strong>Correo:</strong></span><br>
                            <span>${user.correo}</span>
                        </div>
                    `;

                    addToggleButton(card);
                    setupCardListeners(card);
                    addEditIconHandler(card);

                    cardContainer.appendChild(card);
                    setupCardListeners(card);
                });
            }
        } catch (error) {
            showAlert('error', 'Error', 'Hubo un error al mostrar los datos, vuelve a intentarlo', '');
        }
    }

    initializeScrollReveal();
    await addCards();
});