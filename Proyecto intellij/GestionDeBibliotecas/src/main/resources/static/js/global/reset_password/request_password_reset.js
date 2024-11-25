document.addEventListener("DOMContentLoaded", function () {

    const requestButton = document.getElementById("requestButton");

    requestButton.addEventListener("click", async function (event) {
        event.preventDefault();
        const email = document.getElementById("email").value;
        localStorage.setItem('email', email);

        try {
            const response = await fetch("http://localhost:8080/sibi/global/request-password-reset", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Accept": "*/*"
                },
                body: JSON.stringify(
                    {
                        correo: email
                    }
                ),
                credentials: 'include'
            });

            if (!response.ok) {
                const errorResponse = await response.json();
                if (typeof errorResponse === 'object' && !errorResponse.text) {
                    const errorMessages = Object.values(errorResponse).join("\n");
                    showAlert('error', 'Error', errorMessages, '');
                } else if (errorResponse.text) {
                    showAlert('error', 'Error', errorResponse.text, '');
                }
                return;
            }

            const data = await response.json();
            if (data.type === 'SUCCESS') {
                showAlert('success', 'Éxito', data.text, '/GestionDeBibliotecas/templates/global/new_password.html');
            }
        } catch (error) {
            showAlert('error', 'Error', 'Hubo un error, vuelve a intentarlo', '');
        }
    });
});