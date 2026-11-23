const urlProgress = "http://localhost:8081/api/progress"
const urlBooksProgreso = "http://localhost:8081/api/books"

//buscar libros
function buscarLibrosProgreso() {
    const inputBusqueda = document.getElementById("buscarTituloProgreso")
    let textoBusqueda = ""
    if (inputBusqueda) {
        textoBusqueda = inputBusqueda.value.trim()
    }

    let rutaConsulta = urlBooksProgreso
    if (textoBusqueda) {
        //Crear ruta con comillas inversas
        rutaConsulta = `${urlBooksProgreso}?q=${encodeURIComponent(textoBusqueda)}`
    }

    fetch(rutaConsulta)
        .then(async respuesta => {
            if (!respuesta.ok) {
                await lanzarErrorHttp(respuesta, "No se pudieron cargar los libros para progreso")
            }
            return respuesta.json()
        })
        .then(librosEncontrados => {
            console.log("GET libros progreso:", librosEncontrados)

            const selectLibroProgreso = document.getElementById("selectLibroProgreso")
            if (!selectLibroProgreso) return
            selectLibroProgreso.innerHTML = ""

            if (!Array.isArray(librosEncontrados) || librosEncontrados.length === 0) {
                const opcionVacia = document.createElement("option")
                opcionVacia.value = ""
                opcionVacia.innerText = "Sin resultados"
                selectLibroProgreso.appendChild(opcionVacia)
                return
            }

            librosEncontrados.forEach(libro => {
                const opcionLibro = document.createElement("option")
                opcionLibro.value = libro.id
                opcionLibro.innerText = libro.mainTitle
                selectLibroProgreso.appendChild(opcionLibro)
            })
        })
        .catch(error => {
            console.error("Error buscando libros para progreso:", error)
            let textoError = "Error de conexión al buscar libros."
            if (error && error.message) {
                textoError = error.message
            }
            mostrarMensaje(textoError, false)
        })
}

//Guardar progreso del libro
function guardarProgreso() {
    const usuarioId = exigirSesion()
    if (!usuarioId) {
        return
    }

    const selectLibroProgreso = document.getElementById("selectLibroProgreso")
    const selectEstadoLectura = document.getElementById("estadoLectura")
    const inputPaginaActual = document.getElementById("paginaActual")

    let libroId = ""
    if (selectLibroProgreso) {
        libroId = selectLibroProgreso.value
    }

    let estadoLectura = ""
    if (selectEstadoLectura) {
        estadoLectura = selectEstadoLectura.value
    }

    let valorPaginaTexto = ""
    if (inputPaginaActual) {
        valorPaginaTexto = inputPaginaActual.value
    }
    const paginaActual = parseInt(valorPaginaTexto, 10)

    if (!libroId) {
        mostrarMensaje("Selecciona un libro.", false)
        return
    }

    if (isNaN(paginaActual) || paginaActual < 0) {
        mostrarMensaje("La página actual debe ser 0 o mayor.", false)
        return
    }

    const cuerpoProgreso = JSON.stringify({
        status: estadoLectura,
        current_page: paginaActual
    })

    //Crear ruta con comillas inversas
    const rutaGuardado = `${urlProgress}/user/${encodeURIComponent(usuarioId)}/book/${encodeURIComponent(libroId)}`

    fetch(rutaGuardado, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: cuerpoProgreso
    })
        .then(async respuesta => {
            console.log("POST progreso status:", respuesta.status)
            if (!respuesta.ok) {
                await lanzarErrorHttp(respuesta, "No se pudo guardar el progreso")
            }
            mostrarMensaje("Progreso guardado correctamente.", true)
            if (typeof cargarTablaColeccionYProgreso === "function") {
                cargarTablaColeccionYProgreso()
            }
        })
        .catch(error => {
            console.error("Error POST progreso:", error)
            let textoError = "Error de conexión al guardar progreso."
            if (error && error.message) {
                textoError = error.message
            }
            mostrarMensaje(textoError, false)
        })
}

document.addEventListener("DOMContentLoaded", function () {
    buscarLibrosProgreso()
})