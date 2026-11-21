**Herramienta:** Google Gemini
**Respuesta obtenida:** 

Así quedó buscarLibrosColeccion() en js/coleccionesScript.js:

JavaScript

function buscarLibrosColeccion() {
    const input = document.getElementById("buscarTituloColeccion")
    const contenedor = document.getElementById("resultadosLibros")
    const texto = input ? input.value.trim() : ""
    const ruta = texto ? `${urlBooks}?q=${encodeURIComponent(texto)}` : urlBooks

    libroSeleccionadoId = null

    fetch(ruta)
        .then(async r => r.ok ? r.json() : lanzarErrorHttp(r, "No se pudieron buscar los libros"))
        .then(libros => {
            if (!contenedor) return
            contenedor.innerHTML = ""

            if (!Array.isArray(libros) || libros.length === 0) {
                contenedor.innerText = "Sin resultados."
                return
            }

            libros.forEach(libro => {
                const btn = document.createElement("button")
                btn.type = "button"
                btn.className = "btn btn-outline"
                btn.innerText = libro.mainTitle
                btn.onclick = () => {
                    libroSeleccionadoId = libro.id
                    mostrarMensaje(`Libro seleccionado: ${libro.mainTitle}`, true)
                }
                contenedor.appendChild(btn)
            })
        })
        .catch(err => mostrarMensaje(err.message || "No se pudieron buscar los libros.", false))
}



