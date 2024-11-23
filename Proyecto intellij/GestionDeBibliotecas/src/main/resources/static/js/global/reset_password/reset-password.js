document.addEventListener("DOMContentLoaded", function () {

    const email = localStorage.getItem("email");
    const resetButton = document.getElementById("resetButton");

    resetButton.addEventListener("click", async function (event) {
        event.preventDefault();

        const code = document.getElementById("code").value;
        const password = document.getElementById("password").value;
        const passwordConfirmed = document.getElementById("passwordConfirmed").value;

        if (password !== passwordConfirmed) {
            showAlert('error', 'Error', 'Las contraseñas no coinciden.', '');
            return;
        }

        try {
            const response = await fetch("http://localhost:8080/sibi/global/reset-password", {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json",
                    "Accept": "*/*"
                },
                body: JSON.stringify(
                    {
                        correo: email,
                        codigo: code,
                        contrasena: password
                    }
                ),
                credentials: 'include'
            });

            if (!response.ok) {
                const error = await response.json();
                showAlert('error', 'Error', error.message, '');
            }

            const data = await response.json();
            if (data.type === 'SUCCESS') {
                showAlert('success', 'Éxito', data.message, '/GestionDeBibliotecas/templates/global/login.html');
            }
        } catch (error) {
            showAlert('error', 'Error', 'Hubo un error, vuelve a intentarlo', '');
        }
        localStorage.removeItem("email");
    });
});