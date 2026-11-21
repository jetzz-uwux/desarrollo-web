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

    // 1. CREAR LIBRO
    document.getElementById("form-libro").addEventListener("submit", async (e) => {
        e.preventDefault();   // evita que la página se recargue

        const libro = {
            mainTitle: document.getElementById("libro-titulo").value,
            totalPages: parseInt(document.getElementById("libro-paginas").value, 10),
            synopsis: document.getElementById("libro-sinopsis").value
        };

        try {
            await BookNestAPI.createBook(libro);
            avisar("Libro creado correctamente", "success");
            e.target.reset();       // limpia el formulario
            cargarSelectores();     // para que el libro nuevo aparezca en el selector
        } catch (error) {
            avisar(error.message, "error");
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