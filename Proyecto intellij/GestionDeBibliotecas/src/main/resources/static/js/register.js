document.addEventListener("DOMContentLoaded", function () {
    document.getElementById("registroForm").addEventListener("submit", async function (event) {
        event.preventDefault();

        // Obtener los valores del formulario
        const nombre = document.getElementById("nombre").value;
        const apellidos = document.getElementById("apellidos").value;
        const correo = document.getElementById("correo").value;
        const contrasena = document.getElementById("contrasena").value;
        const numeroTelefono = document.getElementById("numeroTelefono").value;

        try {
            const response = await fetch("/api/usuarios/registro", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Accept": "*/*"
                },
                body: JSON.stringify({
                    nombre: nombre,
                    apellidos: apellidos,
                    correo: correo,
                    contrasena: contrasena,
                    numeroTelefono: numeroTelefono,
                    rol: "CLIENTE",  // Este valor puede cambiar según sea necesario
                    codigo: "123456",  // Esto puede ser generado o definido en el backend
                    codigoGeneradoEn: new Date().toISOString()
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
            } else if (data.type === 'SUCCESS') {
                alert("Éxito: " + data.message);
                window.location.href = "/login";  // Redirigir al login después del éxito
            }

        } catch (error) {
            // Mostrar mensaje de error al usuario
            alert("Error: " + error.message);
        }
    });
});