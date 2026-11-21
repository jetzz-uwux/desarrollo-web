const urlCollections = "http://localhost:8081/api/collections"
const urlBooks = "http://localhost:8081/api/books"

let libroSeleccionadoId = null

// Alterar el mensaje de bienvenida
function mostrarMensaje(texto, Exito) {
    const elementoMensaje = document.getElementById("mensaje")
    if (!elementoMensaje) return
    elementoMensaje.innerText = texto
    if (Exito) {
        elementoMensaje.className = "alert success"
    } else {
        elementoMensaje.className = "alert error"
    }
}

// Funcion con apoyo de la IA, para validar la sesion.
function exigirSesion(mostrarAlerta = true) {
    const usuarioId = localStorage.getItem("usuarioId")
    const correoUsuario = localStorage.getItem("usuarioEmail")
    const estadoSesion = document.getElementById("estadoColecciones")

    if (!usuarioId || usuarioId.trim() === "" || usuarioId === "null" || usuarioId === "undefined") {
        if (estadoSesion) estadoSesion.innerText = "Sin sesión activa."
        if (mostrarAlerta) mostrarMensaje("Debes iniciar sesión primero.", false)
        return null
    }
    if (estadoSesion) {
        if (correoUsuario) {
            estadoSesion.innerText = "Sesión activa: " + correoUsuario
        } else {
            estadoSesion.innerText = "Sesión activa."
        }
    }
    return usuarioId.trim()
}

// Mensaje de error
async function lanzarErrorHttp(respuesta, mensajeError) {
    const detalles = await respuesta.text()
    const errorHttp = new Error(`${mensajeError} (HTTP ${respuesta.status})`)
    errorHttp.status = respuesta.status
    errorHttp.detalle = detalles
    throw errorHttp
}

// Funcion de cargar la coleccion
function cargarColecciones() {
    //Validacion de sesion
    const usuarioId = exigirSesion(false)
    const contenedorLista = document.getElementById("listaColecciones")
    const selectColeccionDestino = document.getElementById("selectColeccionDestino")

    if (!usuarioId) {
        if (contenedorLista) contenedorLista.innerText = "Inicia sesión para ver tus colecciones."
        if (selectColeccionDestino) selectColeccionDestino.innerHTML = `<option value="">Sin colecciones</option>`
        return
    }

    //Llamada al backend
    fetch(urlCollections)
        .then(async respuesta => {
            if (!respuesta.ok) {
                await lanzarErrorHttp(respuesta, "No se pudieron cargar las colecciones")
            }
            return respuesta.json()
        })
        .then(coleccionesRecibidas => {
            let coleccionesDelUsuario = []
            if (Array.isArray(coleccionesRecibidas)) {
                coleccionesDelUsuario = coleccionesRecibidas.filter(
                    coleccion => coleccion.user && String(coleccion.user.id) === String(usuarioId)
                )
            }

            if (selectColeccionDestino) {
                if (coleccionesDelUsuario.length === 0) {
                    selectColeccionDestino.innerHTML = `<option value="">Sin colecciones</option>`
                } else {
                    selectColeccionDestino.innerHTML = coleccionesDelUsuario
                        .map(coleccion => `<option value="${coleccion.id}">${coleccion.name}</option>`)
                        .join("")
                }
            }

            if (!contenedorLista) return
            contenedorLista.innerHTML = ""

            if (coleccionesDelUsuario.length === 0) {
                contenedorLista.innerText = "Aún no tienes colecciones."
                return
            }

            coleccionesDelUsuario.forEach(coleccion => {
                const tarjetaColeccion = document.createElement("div")
                tarjetaColeccion.className = "form-group"

                const etiquetaNombre = document.createElement("p")
                etiquetaNombre.innerText = coleccion.name

                const inputNuevoNombre = document.createElement("input")
                inputNuevoNombre.type = "text"
                inputNuevoNombre.id = "edit-" + coleccion.id
                inputNuevoNombre.value = coleccion.name

                const contenedorAcciones = document.createElement("div")
                contenedorAcciones.className = "form-actions mt-1"

                const botonGuardar = document.createElement("button")
                botonGuardar.type = "button"
                botonGuardar.className = "btn btn-secondary"
                botonGuardar.innerText = "Guardar nombre"
                botonGuardar.onclick = () => actualizarColeccion(coleccion.id)

                const botonEliminar = document.createElement("button")
                botonEliminar.type = "button"
                botonEliminar.className = "btn btn-danger"
                botonEliminar.innerText = "Eliminar"
                botonEliminar.onclick = () => eliminarColeccion(coleccion.id)

                contenedorAcciones.append(botonGuardar, botonEliminar)
                tarjetaColeccion.append(etiquetaNombre, inputNuevoNombre, contenedorAcciones)
                contenedorLista.appendChild(tarjetaColeccion)
            })
        })
        .catch(error => {
            let textoError = "No se pudieron cargar las colecciones."
            if (error && error.message) {
                textoError = error.message
            }
            mostrarMensaje(textoError, false)
        })
}

function crearColeccion() {
    //Validacion de sesion
    const usuarioId = exigirSesion()
    if (!usuarioId) return
    const inputNombre = document.getElementById("nombreColeccion")
    const nombreColeccion = inputNombre ? inputNombre.value.trim() : ""
    if (!nombreColeccion) {
        mostrarMensaje("Escribe un nombre para la colección.", false)
        return
    }

    //Llamada al backend
    fetch(urlCollections, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ name: nombreColeccion, user: { id: Number(usuarioId) } })
    })
        .then(async respuesta => {
            if (!respuesta.ok) {
                await lanzarErrorHttp(respuesta, "No se pudo crear la colección")
            }
            if (inputNombre) inputNombre.value = ""
            mostrarMensaje("Colección creada.", true)
            cargarColecciones()
        })
        .catch(error => {
            let textoError = "No se pudo crear la colección."
            if (error && error.message) {
                textoError = error.message
            }
            mostrarMensaje(textoError, false)
        })
}

function actualizarColeccion(coleccionId) {
    //Validacion de sesion
    const usuarioId = exigirSesion()
    if (!usuarioId) return
    const inputEdicion = document.getElementById("edit-" + coleccionId)
    let nombreActualizado = ""
    if (inputEdicion) {
        nombreActualizado = inputEdicion.value.trim()
    }
    if (!nombreActualizado) {
        mostrarMensaje("El nombre no puede estar vacío.", false)
        return
    }

    //Llamada al backend, armando la URL con comillas invertidas para sustituir texto por las variables
    //encodeURIComponent evita que se rompa si entran caracteres especiales
    fetch(`${urlCollections}/${coleccionId}?userId=${encodeURIComponent(usuarioId)}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ name: nombreActualizado, user: { id: Number(usuarioId) } })
    })
        .then(async respuesta => {
            if (!respuesta.ok) {
                await lanzarErrorHttp(respuesta, "No se pudo actualizar la colección")
            }
            mostrarMensaje("Colección actualizada.", true)
            cargarColecciones()
        })
        .catch(error => {
            let textoError = "No se pudo actualizar la colección."
            if (error && error.message) {
                textoError = error.message
            }
            mostrarMensaje(textoError, false)
        })
}

function eliminarColeccion(coleccionId) {
    //Validacion de sesion
    const usuarioId = exigirSesion()
    if (!usuarioId) return

    //Llamada al backend, armando la URL con comillas invertidas para sustituir texto por las variables
    //encodeURIComponent evita que se rompa si entran caracteres especiales
    fetch(`${urlCollections}/${coleccionId}?userId=${encodeURIComponent(usuarioId)}`, { method: "DELETE" })
        .then(async respuesta => {
            if (!respuesta.ok) {
                await lanzarErrorHttp(respuesta, "No se pudo eliminar la colección")
            }
            mostrarMensaje("Colección eliminada.", true)
            cargarColecciones()
        })
        .catch(error => {
            let textoError = "No se pudo eliminar la colección."
            if (error && error.message) {
                textoError = error.message
            }
            mostrarMensaje(textoError, false)
        })
}

//Busqueda, apoyado por IAG y modificado por mi
function buscarLibrosColeccion() {
    const inputBusqueda = document.getElementById("buscarTituloColeccion")
    const contenedorResultados = document.getElementById("resultadosLibros")
    let textoBusqueda = ""
    if (inputBusqueda) {
        textoBusqueda = inputBusqueda.value.trim()
    }

    let rutaConsulta = urlBooks
    if (textoBusqueda) {
        rutaConsulta = `${urlBooks}?q=${encodeURIComponent(textoBusqueda)}`
    }

    libroSeleccionadoId = null

    fetch(rutaConsulta)
        .then(async respuesta => {
            if (!respuesta.ok) {
                await lanzarErrorHttp(respuesta, "No se pudieron buscar los libros")
            }
            return respuesta.json()
        })
        .then(librosEncontrados => {
            if (!contenedorResultados) return
            contenedorResultados.innerHTML = ""

            if (!Array.isArray(librosEncontrados) || librosEncontrados.length === 0) {
                contenedorResultados.innerText = "Sin resultados."
                return
            }

            librosEncontrados.forEach(libro => {
                const botonLibro = document.createElement("button")
                botonLibro.type = "button"
                botonLibro.className = "btn btn-outline"
                botonLibro.innerText = libro.mainTitle
                botonLibro.onclick = () => {
                    libroSeleccionadoId = libro.id
                    mostrarMensaje(`Libro seleccionado: ${libro.mainTitle}`, true)
                }
                contenedorResultados.appendChild(botonLibro)
            })
        })
        .catch(error => {
            let textoError = "No se pudieron buscar los libros."
            if (error && error.message) {
                textoError = error.message
            }
            mostrarMensaje(textoError, false)
        })
}

function agregarLibroColeccion() {
    //Validar sesion
    const usuarioId = exigirSesion()
    if (!usuarioId) return
    const selectDestino = document.getElementById("selectColeccionDestino")
    let coleccionId = ""
    if (selectDestino) {
        coleccionId = selectDestino.value
    }
    if (!coleccionId || !libroSeleccionadoId) {
        mostrarMensaje("Selecciona una colección y haz clic en un libro.", false)
        return
    }

    //Llamada al backend, armando la URL con comillas invertidas para sustituir texto por las variables
    //encodeURIComponent evita que se rompa si entran caracteres especiales
    fetch(`${urlCollections}/${coleccionId}/books/${libroSeleccionadoId}?userId=${encodeURIComponent(usuarioId)}`, {
        method: "POST"
    })
        .then(async respuesta => {
            if (!respuesta.ok) {
                await lanzarErrorHttp(respuesta, "No se pudo guardar el libro en la colección")
            }
            mostrarMensaje("Libro guardado en la colección.", true)
        })
        .catch(error => {
            let textoError = "No se pudo guardar el libro en la colección."
            if (error && error.message) {
                textoError = error.message
            }
            mostrarMensaje(textoError, false)
        })
}

document.addEventListener("DOMContentLoaded", () => {
    cargarColecciones()
    buscarLibrosColeccion()
})