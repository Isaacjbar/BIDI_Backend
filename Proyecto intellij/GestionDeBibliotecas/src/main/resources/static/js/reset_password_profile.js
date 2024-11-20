document.addEventListener("DOMContentLoaded", function () {
    document.getElementById("recoverPasswordForm").addEventListener("submit", async function (event) {
        event.preventDefault();

        const newPassword = document.getElementById("newPassword").value;
        const userId = document.getElementById("userId").value;  // Asumimos que tienes un campo oculto o de algún tipo que contenga el ID del usuario
        const token = localStorage.getItem("jwt");  // Recuperamos el token JWT desde el almacenamiento local

        if (!token) {
            alert("No estás autenticado. Por favor, inicia sesión.");
            return;
        }

        try {
            const response = await fetch("/sibi/global/recover-password-from-profile", {
                method: "PUT",  // Usamos PUT para actualizar la contraseña
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`  // Pasamos el token JWT en el encabezado de la solicitud
                },
                body: JSON.stringify({
                    usuarioId: userId,  // ID del usuario al que se le está recuperando la contraseña
                    contrasena: newPassword  // Nueva contraseña
                })
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