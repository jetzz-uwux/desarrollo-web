**Herramienta:** Google Gemini
**Respuesta obtenida:** 
Salvaguardas en el frontend (coleccionesScript.js)
Para evitar que alguien dispare acciones sin haber iniciado sesión (aunque manipule botones o llame funciones desde consola), podemos centralizar una función exigirSesion() que lea localStorage.getItem("usuarioId") y bloquee creación, edición, borrado, agregar libro a colección y guardado de progreso si no existe:

JavaScript

function exigirSesion(mostrarAlerta = true) {
    const id = localStorage.getItem("usuarioId")
    const email = localStorage.getItem("usuarioEmail")
    const estado = document.getElementById("estadoColecciones")

    if (!id || id.trim() === "" || id === "null" || id === "undefined") {
        if (estado) estado.innerText = "Sin sesión activa."
        if (mostrarAlerta) mostrarMensaje("Debes iniciar sesión primero.", false)
        return null
    }
    if (estado) estado.innerText = email ? "Sesión activa: " + email : "Sesión activa."
    return id.trim()
}
Y al inicio de cada acción (crearColeccion, actualizarColeccion, eliminarColeccion, agregarLibroColeccion, guardarProgreso), llamas primero a:

JavaScript


usuarioId = exigirSesion()
if (!usuarioId) {
    return
}
Además, si no hay sesión al cargar la página, puedes deshabilitar los botones de acción en el DOM (btn.disabled = true) para que la interfaz lo refleje de inmediato.