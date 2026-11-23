// Acciones del catálogo: edición y eliminación de fichas de libros.
document.addEventListener("DOMContentLoaded", () => {
    const panel = document.getElementById("editar-libro-panel");
    const formulario = document.getElementById("form-editar-libro");
    const mensaje = document.getElementById("catalogo-accion-mensaje");
    if (!panel || !formulario || !mensaje) return;

    const campo = id => document.getElementById(id);
    const mostrarError = texto => {
        mensaje.textContent = texto;
        mensaje.scrollIntoView({behavior: "smooth", block: "center"});
    };
    async function errorDeRespuesta(respuesta) {
        if (respuesta.status === 409) {
            return "Este libro tiene información relacionada (serie, PDF, colección, progreso, reseñas, calificaciones o títulos alternativos) y no se puede eliminar.";
        }
        if (respuesta.status === 404) return "No se encontró el libro.";
        return `No se pudo completar la operación (HTTP ${respuesta.status}).`;
    }

    document.querySelectorAll(".js-eliminar-libro").forEach(boton => {
        boton.addEventListener("click", async () => {
            const id = boton.dataset.bookId;
            const titulo = boton.dataset.bookTitle;
            if (!window.confirm(`¿Eliminar definitivamente "${titulo}" del catálogo? Esta acción no se puede deshacer.`)) return;
            boton.disabled = true;
            try {
                const respuesta = await fetch(`/api/books/${encodeURIComponent(id)}`, {method: "DELETE"});
                if (!respuesta.ok) throw new Error(await errorDeRespuesta(respuesta));
                window.location.reload();
            } catch (error) {
                mostrarError(error.message);
                boton.disabled = false;
            }
        });
    });

    document.querySelectorAll(".js-editar-libro").forEach(boton => {
        boton.addEventListener("click", async () => {
            boton.disabled = true;
            try {
                const respuesta = await fetch(`/api/books/${encodeURIComponent(boton.dataset.bookId)}`);
                if (!respuesta.ok) throw new Error(await errorDeRespuesta(respuesta));
                const libro = await respuesta.json();
                campo("editar-libro-id").value = libro.id;
                campo("editar-libro-titulo").value = libro.mainTitle || "";
                campo("editar-libro-categoria").value = libro.category || "";
                campo("editar-libro-paginas").value = libro.totalPages ?? "";
                campo("editar-libro-fecha").value = libro.originalPublicationDate || "";
                campo("editar-libro-portada").value = libro.coverUrl || "";
                campo("editar-libro-sinopsis").value = libro.synopsis || "";
                panel.hidden = false;
                panel.scrollIntoView({behavior: "smooth"});
            } catch (error) {
                mostrarError(error.message);
            } finally {
                boton.disabled = false;
            }
        });
    });

    campo("cancelar-edicion").addEventListener("click", () => {
        panel.hidden = true;
        formulario.reset();
    });

    formulario.addEventListener("submit", async evento => {
        evento.preventDefault();
        const guardar = formulario.querySelector("button[type=submit]");
        guardar.disabled = true;
        const id = campo("editar-libro-id").value;
        const libro = {
            mainTitle: campo("editar-libro-titulo").value.trim(),
            category: campo("editar-libro-categoria").value,
            totalPages: Number(campo("editar-libro-paginas").value),
            originalPublicationDate: campo("editar-libro-fecha").value || null,
            coverUrl: campo("editar-libro-portada").value.trim() || null,
            synopsis: campo("editar-libro-sinopsis").value.trim()
        };
        try {
            const respuesta = await fetch(`/api/books/${encodeURIComponent(id)}`, {
                method: "PUT",
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify(libro)
            });
            if (!respuesta.ok) throw new Error(await errorDeRespuesta(respuesta));
            window.location.reload();
        } catch (error) {
            mostrarError(error.message);
            guardar.disabled = false;
        }
    });
});
