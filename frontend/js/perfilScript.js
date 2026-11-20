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

function precargarDatosLocales() {
    idGuardado = localStorage.getItem("usuarioId")
    usernameGuardado = localStorage.getItem("usuarioUsername")
    emailGuardado = localStorage.getItem("usuarioEmail")
    passwordGuardado = localStorage.getItem("usuarioPasswordHash")

    if (usernameGuardado) {
        document.getElementById("editUsername").value = usernameGuardado
    }
    if (emailGuardado) {
        document.getElementById("editEmail").value = emailGuardado
        document.getElementById("estadoPerfil").innerText = "Sesión guardada para: " + emailGuardado
    } else if (idGuardado) {
        document.getElementById("estadoPerfil").innerText = "Sesión activa (ID: " + idGuardado + ")."
    }
    if (passwordGuardado) {
        document.getElementById("editPassword").value = passwordGuardado
    }
}

function actualizarUsuario() {
    id = localStorage.getItem("usuarioId")
    username = document.getElementById("editUsername").value
    email = document.getElementById("editEmail").value
    passwordHash = document.getElementById("editPassword").value

    if (!id) {
        mostrarMensaje("No hay una sesión activa. Inicia sesión primero.", false)
        return
    }

    cuerpo = '{"username":"' + username + '","email":"' + email + '","passwordHash":"' + passwordHash + '"}'
    console.log("Payload PUT usuario (ID " + id + "):", cuerpo)

    fetch(urlUsers + "/" + id, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: cuerpo
    })
    .then(async respuesta => {
        detalle = await respuesta.text()
        console.log("Respuesta PUT (" + respuesta.status + "):", detalle)

        if (respuesta.ok) {
            localStorage.setItem("usuarioUsername", username)
            localStorage.setItem("usuarioEmail", email)
            localStorage.setItem("usuarioPasswordHash", passwordHash)
            document.getElementById("estadoPerfil").innerText = "Sesión guardada para: " + email
            mostrarMensaje("Cambios guardados correctamente.", true)
        } else {
            mostrarMensaje("No se pudo actualizar el usuario. Verifica los datos.", false)
        }
    })
    .catch(error => {
        console.error("Error de red en PUT usuario:", error)
        mostrarMensaje("No se pudo conectar con el servidor.", false)
    })
}

function borrarUsuario() {
    id = localStorage.getItem("usuarioId")
    if (!id) {
        mostrarMensaje("No hay una sesión activa para eliminar.", false)
        return
    }

    console.log("Solicitando DELETE para usuario ID:", id)

    fetch(urlUsers + "/" + id, {
        method: "DELETE"
    })
    .then(async respuesta => {
        detalle = await respuesta.text()
        console.log("Respuesta DELETE (" + respuesta.status + "):", detalle)

        if (respuesta.ok) {
            localStorage.removeItem("usuarioId")
            localStorage.removeItem("usuarioUsername")
            localStorage.removeItem("usuarioEmail")
            localStorage.removeItem("usuarioPasswordHash")
            document.getElementById("editUsername").value = ""
            document.getElementById("editEmail").value = ""
            document.getElementById("editPassword").value = ""
            document.getElementById("estadoPerfil").innerText = "No hay sesión activa."
            mostrarMensaje("Usuario eliminado correctamente.", true)
        } else {
            mostrarMensaje("No se pudo eliminar el usuario.", false)
        }
    })
    .catch(error => {
        console.error("Error de red en DELETE usuario:", error)
        mostrarMensaje("No se pudo conectar con el servidor.", false)
    })
}

function cerrarSesion() {
    localStorage.removeItem("usuarioId")
    localStorage.removeItem("usuarioUsername")
    localStorage.removeItem("usuarioEmail")
    localStorage.removeItem("usuarioPasswordHash")

    document.getElementById("editUsername").value = ""
    document.getElementById("editEmail").value = ""
    document.getElementById("editPassword").value = ""
    document.getElementById("estadoPerfil").innerText = "Sesión cerrada."

    console.log("Sesión cerrada y datos limpiados de localStorage.")
    mostrarMensaje("Sesión cerrada.", true)
}

document.addEventListener("DOMContentLoaded", function() {
    precargarDatosLocales()
})