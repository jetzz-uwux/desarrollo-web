// ============================================================
// catalogo.js: formularios de libros, ligado y series (Fetch)
// ============================================================

document.addEventListener("DOMContentLoaded", () => {

    const alertBox = document.getElementById("alert-box");

    // Muestra un mensaje arriba de la página.
    // tipo: "success" (verde) o "error" (rojo)
    function avisar(mensaje, tipo) {
        alertBox.textContent = mensaje;
        alertBox.className = `alert ${tipo}`;
        setTimeout(() => alertBox.classList.add("hidden"), 5000);
    }

    // Llena un <select> con una opción por cada elemento de la lista.
    // selectId: el id del select; lista: los datos; campo: qué texto mostrar.
    function llenarSelect(selectId, lista, campo) {
        const select = document.getElementById(selectId);
        select.innerHTML = "";
        if (!lista || lista.length === 0) {
            const opcion = document.createElement("option");
            opcion.value = "";
            opcion.textContent = "No hay opciones disponibles";
            select.appendChild(opcion);
            return;
        }
        lista.forEach(item => {
            const opcion = document.createElement("option");
            opcion.value = item.id;
            opcion.textContent = item[campo];
            select.appendChild(opcion);
        });
    }

    // Pide libros, autores y géneros, y llena los tres selectores.
    async function cargarSelectores() {
        try {
            const libros = await BookNestAPI.getBooks();
            llenarSelect("libro-select", libros, "mainTitle");

            const autores = await BookNestAPI.getAuthors();
            llenarSelect("autor-select", autores, "name");

            const generos = await BookNestAPI.getGenres();
            llenarSelect("genero-select", generos, "name");
        } catch (error) {
            avisar(error.message, "error");
        }
    }

    // Vista previa local: no descarga ni sube archivos al servidor.
    const portadaInput = document.getElementById("libro-portada");
    const portadaImg = document.getElementById("libro-portada-preview");
    const portadaPlaceholder = document.getElementById("libro-portada-placeholder");
    function actualizarPortada() {
        const url = portadaInput.value.trim();
        portadaImg.hidden = true;
        portadaPlaceholder.hidden = false;
        if (url && /^https?:\/\//i.test(url)) portadaImg.src = url;
        else portadaImg.removeAttribute("src");
    }
    portadaImg.addEventListener("load", () => {
        portadaImg.hidden = false;
        portadaPlaceholder.hidden = true;
    });
    portadaImg.addEventListener("error", () => {
        portadaImg.hidden = true;
        portadaPlaceholder.hidden = false;
    });
    portadaInput.addEventListener("input", actualizarPortada);
    document.getElementById("form-libro").addEventListener("reset", () => {
        setTimeout(actualizarPortada, 0);
        document.getElementById("libro-resultado").textContent = "";
    });

    // 1. CREAR LIBRO
    document.getElementById("form-libro").addEventListener("submit", async (e) => {
        e.preventDefault();   // evita que la página se recargue

        const fecha = document.getElementById("libro-fecha").value;
        const portada = portadaInput.value.trim();
        const libro = {
            mainTitle: document.getElementById("libro-titulo").value.trim(),
            totalPages: parseInt(document.getElementById("libro-paginas").value, 10),
            synopsis: document.getElementById("libro-sinopsis").value.trim(),
            originalPublicationDate: fecha || null,
            coverUrl: portada || null
        };
        const boton = document.getElementById("btn-guardar-libro");
        const resultado = document.getElementById("libro-resultado");
        boton.disabled = true;
        boton.textContent = "Guardando...";
        resultado.textContent = "";
        try {
            let confirmarCoincidencia = false;
            // RF-25/RF-27: comprobar posibles coincidencias antes de crear.
            // La elección final es del usuario; no se fusionan fichas automáticamente.
            const respuestaDuplicados = await fetch(
                "/api/books/posibles-duplicados?titulo=" + encodeURIComponent(libro.mainTitle)
            );
            if (!respuestaDuplicados.ok) throw new Error("No se pudieron comprobar posibles duplicados");
            const coincidencias = await respuestaDuplicados.json();
            if (coincidencias.length > 0) {
                const nombres = coincidencias.slice(0, 5)
                    .map(item => `• ${item.mainTitle} (ID ${item.id})`).join("\n");
                const continuar = window.confirm(
                    "Ya existen fichas que podrían corresponder a esta obra:\n" + nombres +
                    "\n\nAceptar: crear una ficha diferente.\nCancelar: revisar las existentes y evitar duplicados."
                );
                if (!continuar) {
                    resultado.replaceChildren();
                    resultado.append(document.createTextNode(
                        "Revisa las fichas existentes antes de crear otra: "
                    ));
                    coincidencias.slice(0, 5).forEach((item, indice) => {
                        if (indice > 0) resultado.append(document.createTextNode(" · "));
                        const enlace = document.createElement("a");
                        enlace.href = "/catalogo/libros/" + encodeURIComponent(item.id);
                        enlace.textContent = item.mainTitle;
                        resultado.append(enlace);
                    });
                    resultado.className = "catalogo-result catalogo-result-error";
                    return;
                }
                confirmarCoincidencia = true;
            }
            const respuestaCrear = await fetch(
                "/api/books" + (confirmarCoincidencia ? "?confirmarCoincidencia=true" : ""),
                { method: "POST", headers: { "Content-Type": "application/json" },
                  body: JSON.stringify(libro) }
            );
            if (!respuestaCrear.ok) {
                if (respuestaCrear.status === 409) {
                    throw new Error("Ya existe una ficha similar. Vuelve a revisar el catálogo antes de crear otra.");
                }
                throw new Error(`No se pudo crear el libro (HTTP ${respuestaCrear.status})`);
            }
            const creado = await respuestaCrear.json();
            avisar("Libro creado correctamente", "success");
            e.target.reset();
            await cargarSelectores();
            if (creado && creado.id != null) {
                document.getElementById("libro-select").value = String(creado.id);
            }
            resultado.textContent = "✓ Libro guardado. Ahora puedes asociar su autor y género en el paso 2.";
            resultado.className = "catalogo-result catalogo-result-ok";
        } catch (error) {
            avisar(error.message, "error");
            resultado.textContent = "No se pudo guardar: " + error.message;
            resultado.className = "catalogo-result catalogo-result-error";
        } finally {
            boton.disabled = false;
            boton.textContent = "Guardar libro";
        }
    });

    // 2. LIGAR AUTOR A UN LIBRO
    document.getElementById("btn-ligar-autor").addEventListener("click", async () => {
        const bookId = Number(document.getElementById("libro-select").value);
        const authorId = Number(document.getElementById("autor-select").value);

        try {
            await BookNestAPI.linkAuthor(bookId, authorId);
            avisar("Autor ligado al libro", "success");
        } catch (error) {
            avisar(error.message, "error");
        }
    });

    // 3. LIGAR GÉNERO A UN LIBRO
    document.getElementById("btn-ligar-genero").addEventListener("click", async () => {
        const bookId = Number(document.getElementById("libro-select").value);
        const genreId = Number(document.getElementById("genero-select").value);

        try {
            await BookNestAPI.linkGenre(bookId, genreId);
            avisar("Género ligado al libro", "success");
        } catch (error) {
            avisar(error.message, "error");
        }
    });

    // 4. CREAR SERIE
    document.getElementById("form-serie").addEventListener("submit", async (e) => {
        e.preventDefault();

        const serie = {
            name: document.getElementById("serie-nombre").value,
            seriesType: document.getElementById("serie-tipo").value
        };

        try {
            await BookNestAPI.createSeries(serie);
            avisar("Serie creada correctamente", "success");
            e.target.reset();
        } catch (error) {
            avisar(error.message, "error");
        }
    });

    // Al abrir la página, llenar los selectores
    cargarSelectores();
});