document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("form-asociar-serie");
    if (!form) return;
    const mensaje = document.getElementById("serie-mensaje");
    document.querySelectorAll(".js-quitar-serie").forEach(boton => {
        boton.addEventListener("click", async () => {
            if (!window.confirm(`¿Quitar "${boton.dataset.bookTitle}" de esta serie? El libro no se borrará.`)) return;
            boton.disabled = true;
            try {
                const respuesta = await fetch(`/api/series/${form.dataset.seriesId}/books/${boton.dataset.bookId}`,
                    {method: "DELETE"});
                if (!respuesta.ok) throw new Error(`No se pudo quitar (HTTP ${respuesta.status})`);
                window.location.reload();
            } catch (error) { mensaje.textContent = error.message; boton.disabled = false; }
        });
    });
    form.addEventListener("submit", async (event) => {
        event.preventDefault();
        const bookId = Number(document.getElementById("serie-libro").value);
        const volumeNumber = Number(document.getElementById("serie-volumen").value);
        const readingOrder = Number(document.getElementById("serie-orden").value);
        if (!bookId || volumeNumber < 1 || readingOrder < 1) {
            mensaje.textContent = "Selecciona un libro y números positivos.";
            return;
        }
        const boton = form.querySelector("button[type=submit]");
        boton.disabled = true;
        try {
            const respuesta = await fetch(`/api/series/${form.dataset.seriesId}/books`, {
                method: "POST",
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify({bookId, volumeNumber, readingOrder})
            });
            if (!respuesta.ok) {
                throw new Error(respuesta.status === 409
                    ? "Este libro, volumen u orden ya está asignado a la serie."
                    : `No se pudo asociar el libro (HTTP ${respuesta.status}).`);
            }
            window.location.reload();
        } catch (error) {
            mensaje.textContent = error.message;
        } finally {
            boton.disabled = false;
        }
    });
});
