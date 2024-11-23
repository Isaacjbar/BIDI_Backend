document.addEventListener("DOMContentLoaded", function () {
    const loginButton = document.getElementById("loginButton");
    const registerButton = document.getElementById("registerButton");

    const emailLogin = document.getElementById("emailLogin");
    const passwordLogin = document.getElementById("passwordLogin");

    const nameRegister = document.getElementById("nameRegister");
    const surnameRegister = document.getElementById("surnameRegister");
    const emailRegister = document.getElementById("emailRegister");
    const passwordRegister = document.getElementById("passwordRegister");
    const passwordRegisterConfirmed = document.getElementById("passwordRegisterConfirmed");

    loginButton.addEventListener("click", async function (event) {
        event.preventDefault();

        const email = emailLogin.value;
        const password = passwordLogin.value;

        try {
            const response = await fetch("http://localhost:8080/sibi/auth/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Accept": "*/*"
                },
                body: JSON.stringify({
                    email: email,
                    password: password
                }),
                credentials: 'include'
            });

            if (!response.ok) {
                const error = await response.json();
                if (error.message === "Usuario no encontrado") {
                    showAlert('error', 'Error', 'Usuario no encontrado', '');
                } else if (error.message.includes("Correo o contraseña incorrectos")) {
                    showAlert('error', 'Error', 'Correo o contraseña incorrectos', '');
                } else if (error.message === "El usuario está inactivo") {
                    showAlert('error', 'Error', 'El usuario está inactivo', '');
                } else {
                    showAlert('error', 'Error', 'Error al procesar la solicitud', '');
                }
            }

            const authResponse = await response.json();
            localStorage.setItem("jwt", authResponse.jwt);

            if (authResponse.role.includes("ADMINISTRADOR")) {
                showAlert('success', 'Éxito', 'Inicio de sesión exitoso', '/GestionDeBibliotecas/templates/admin/dashboard.html');
            } else if (authResponse.role.includes("CLIENTE")) {
                showAlert('success', 'Éxito', 'Inicio de sesión exitoso', '/GestionDeBibliotecas/templates/customer/menu.html');
            }

        } catch (error) {
            showAlert('error', 'Error', 'Hubo un error, vuelve a intentarlo', '');
        }
    });

    registerButton.addEventListener("click", async function (event) {
        event.preventDefault();

        const name = nameRegister.value;
        const surname = surnameRegister.value;
        const email = emailRegister.value;
        const password = passwordRegister.value;
        const passwordConfirmed = passwordRegisterConfirmed.value;

        if (password !== passwordConfirmed) {
            showAlert('error', 'Error', 'Las contraseñas no coinciden.', '');
            return;
        }

        try {
            const response = await fetch("http://localhost:8080/sibi/global/register", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Accept": "*/*"
                },
                body: JSON.stringify(
                    {
                        nombre: name,
                        apellidos: surname,
                        correo: email,
                        contrasena: password
                    }
                )
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

            const successResponse = await response.json();
            showAlert('success', 'Éxito', successResponse.text + 'Por favor, inicia sesión', '/GestionDeBibliotecas/templates/global/login.html');
        } catch (error) {
            showAlert('error', 'Error', 'Hubo un error, vuelve a intentarlo', '');
        }
    });
});