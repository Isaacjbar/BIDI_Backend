document.addEventListener("DOMContentLoaded", function () {
    document.getElementById("recoverPasswordForm").addEventListener("submit", async function (event) {
        event.preventDefault();

        const email = document.getElementById("email").value;

        try {
            const response = await fetch("/sibi/global/request-password-reset", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Accept": "*/*"
                },
                body: JSON.stringify(
                    {
                        email: email
                    }
                )
            });

            if (!response.ok) {
                const error = await response.json();
                throw new Error(error.message || "Error en el servidor");
            }

            const data = await response.json();

            // Mostrar mensaje de acuerdo al estado de la respuesta
            if (data.type === 'ERROR') {
                alert("Error: " + data.message);
            } else if (data.type === 'WARNING') {
                alert("Advertencia: " + data.message);
            } else if (data.type === 'SUCCESS') {
                alert("Éxito: " + data.message);
                window.location.href = "/sibi/login.html";  // Redirigir al login después del éxito
            }

        } catch (error) {
            // Mostrar mensaje de error al usuario
            alert("Error: " + error.message);
        }
    });
});