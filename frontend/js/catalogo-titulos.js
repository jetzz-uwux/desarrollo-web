document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("form-titulo-alternativo");
    if (!form) return;
    const estado = document.getElementById("alt-mensaje");
    form.addEventListener("submit", async event => {
        event.preventDefault();
        const boton = form.querySelector("button[type=submit]");
        boton.disabled = true;
        try {
            const respuesta = await fetch("/api/alternative-titles", {
                method: "POST", headers: {"Content-Type": "application/json"},
                body: JSON.stringify({book: {id: Number(form.dataset.bookId)},
                    title: document.getElementById("alt-titulo").value.trim(),
                    language: document.getElementById("alt-idioma").value})
            });
            if (!respuesta.ok) throw new Error(respuesta.status === 409
                ? "Ese título ya está registrado para este libro e idioma."
                : `No se pudo guardar el título (HTTP ${respuesta.status}).`);
            window.location.reload();
        } catch (error) { estado.textContent = error.message; boton.disabled = false; }
    });
});
