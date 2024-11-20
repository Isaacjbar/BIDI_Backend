document.addEventListener("DOMContentLoaded", function () {
    const loginButton = document.getElementById("loginButton");
    const registerButton = document.getElementById("registerButton");

    const emailLogin = document.getElementById("emailLogin");
    const passwordLogin = document.getElementById("passwordLogin");

    const nameRegister = document.getElementById("nameRegister");
    const emailRegister = document.getElementById("emailRegister");
    const passwordRegister = document.getElementById("passwordRegister");
    const passwordRegisterConfirmed = document.getElementById("passwordRegisterConfirmed");

    loginButton.addEventListener("click", async function (event) {
        event.preventDefault();

        const email = emailLogin.value;
        const password = passwordLogin.value;

        try {
            const response = await fetch("/sibi/auth/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Accept": "*/*"
                },
                body: JSON.stringify(
                    {
                        email: email,
                        password: password
                    }
                )
            });

            if (!response.ok) {
                const error = await response.json();
                throw new Error(error.message || "Error en el servidor");
            }

            const authResponse = await response.json();
            alert("Inicio de sesión exitoso. Redirigiendo...");
            localStorage.setItem("jwt", authResponse.jwt);

            if (authResponse.role.includes("ADMINISTRADOR")) {
                window.location.href = "/admin/dashboard"; // Página para el administrador
            } else if (authResponse.role.includes("CLIENTE")) {
                window.location.href = "/cliente/dashboard"; // Página para el cliente
            }
        } catch (error) {
            alert("Error: " + error.message);
        }
    });

    registerButton.addEventListener("click", async function (event) {
        event.preventDefault();

        const name = nameRegister.value;
        const email = emailRegister.value;
        const password = passwordRegister.value;
        const passwordConfirmed = passwordRegisterConfirmed.value;

        // Validar que las contraseñas coincidan
        if (password !== passwordConfirmed) {
            alert("Las contraseñas no coinciden");
            return;
        }

        try {
            const response = await fetch("/sibi/global/register", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Accept": "*/*"
                },
                body: JSON.stringify(
                    {
                        name: name,
                        email: email,
                        password: password
                    }
                )
            });

            if (!response.ok) {
                const error = await response.json();
                throw new Error(error.message || "Error en el servidor");
            }

            const registerResponse = await response.json();
            alert("Registro exitoso. Por favor, inicia sesión." + registerResponse);

            window.location.href = "/sibi/login.html"; // corregir
        } catch (error) {
            alert("Error: " + error.message);
        }
    });
});