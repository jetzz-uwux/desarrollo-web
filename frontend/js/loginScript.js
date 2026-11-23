urlUsers = "http://localhost:8081/api/users"

function mostrarMensaje(texto, esExito) {
    msg = document.getElementById("mensaje")
    msg.innerText = texto
    msg.classList.remove("hidden")
    msg.classList.remove("success")
    msg.classList.remove("error")

    if (esExito) {
        msg.classList.add("success")
    } else {
        msg.classList.add("error")
    }
}

function iniciarSesion() {
    email = document.getElementById("loginEmail").value
    passwordHash = document.getElementById("loginPassword").value

    cuerpo = '{"email":"' + email + '","passwordHash":"' + passwordHash + '"}'
    console.log("Payload login:", cuerpo)

    fetch(urlUsers + "/login", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: cuerpo
    })
    .then(async respuesta => {
        if (respuesta.ok) {
            datos = await respuesta.json()
            console.log("Respuesta login (" + respuesta.status + "):", datos)

            localStorage.setItem("usuarioId", datos.id)
            localStorage.setItem("usuarioUsername", datos.username || "")
            localStorage.setItem("usuarioEmail", datos.email || email)
            localStorage.setItem("usuarioPasswordHash", passwordHash)
            mostrarMensaje("Inicio de sesión exitoso.", true)
            window.location.href = "/index.html";
        } else {
            detalle = await respuesta.text()
            console.log("Respuesta login (" + respuesta.status + "):", detalle)
            mostrarMensaje("Correo o contraseña incorrectos.", false)
        }
    })
    .catch(error => {
        console.error("Error de red en login:", error)
        mostrarMensaje("No se pudo conectar con el servidor.", false)
    })
}

function crearUsuario() {
    username = document.getElementById("regUsername").value
    email = document.getElementById("regEmail").value
    passwordHash = document.getElementById("regPassword").value

    cuerpo = '{"username":"' + username + '","email":"' + email + '","passwordHash":"' + passwordHash + '"}'
    console.log("Payload registro:", cuerpo)

    fetch(urlUsers + "/register", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: cuerpo
    })
    .then(async respuesta => {
        detalle = await respuesta.text()
        console.log("Respuesta registro (" + respuesta.status + "):", detalle)

        if (respuesta.ok) {
            mostrarMensaje("Usuario registrado correctamente.", true)
            //Auto login tras crear usuario:
            fetch(urlUsers + "/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: '{"email":"' + email + '","passwordHash":"' + passwordHash + '"}'
            })
            .then(async respLogin => {
                if (respLogin.ok) {
                    datos = await respLogin.json()
                    console.log("Auto login tras registro (" + respLogin.status + "):", datos)

                    localStorage.setItem("usuarioId", datos.id)
                    localStorage.setItem("usuarioUsername", datos.username || username)
                    localStorage.setItem("usuarioEmail", datos.email || email)
                    localStorage.setItem("usuarioPasswordHash", passwordHash)
                } else {
                    detalleLogin = await respLogin.text()
                    console.log("Auto login fallido (" + respLogin.status + "):", detalleLogin)
                }
            })
            .catch(err => {
                console.error("Error de red en auto login:", err)
            })
        } else {
            mostrarMensaje("No se pudo registrar el usuario. Revisa los datos.", false)
        }
    })
    .catch(error => {
        console.error("Error de red en registro:", error)
        mostrarMensaje("No se pudo conectar con el servidor.", false)
    })
}
