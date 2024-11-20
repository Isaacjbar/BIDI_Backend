document.addEventListener("DOMContentLoaded", function () {
    document.getElementById("recoverPasswordForm").addEventListener("submit", async function (event) {
        event.preventDefault();

        const codigo = document.getElementById("codigo").value;
        const contrasena = document.getElementById("password").value;

        try {
            const response = await fetch("/sibi/global/reset-password", {
                method: "PUT", // Usamos PUT porque tu backend está esperando un PUT
                headers: {
                    "Content-Type": "application/json",
                    "Accept": "*/*"
                },
                body: JSON.stringify(
                    {
                        codigo: codigo,  // Cambiar 'email' por 'codigo' si tu backend espera un código
                        contrasena: contrasena
                    }
                )
            });

            if (!response.ok) {
                const error = await response.json();
                throw new Error(error.message || "Error en el servidor");
            }

            const data = await response.json();

            if (data.type === 'ERROR') {
                alert("Error: " + data.message);
            } else if (data.type === 'WARNING') {
                alert("Advertencia: " + data.message);
            } else if (data.type === 'SUCCESS') {
                alert("Éxito: " + data.message);
                window.location.href = "/sibi/login.html";
            }
        } catch (error) {
            alert("Error: " + error.message);
        }
    });
});