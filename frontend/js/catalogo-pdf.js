document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("form-pdf-ficha");
    if (!form) return;
    const mensaje = document.getElementById("pdf-ficha-mensaje");
    form.addEventListener("submit", async event => {
        event.preventDefault();
        const file = document.getElementById("pdf-ficha-file").files[0];
        if (!file || file.size > 50 * 1024 * 1024) {
            mensaje.textContent = "Selecciona un PDF de hasta 50 MB."; return;
        }
        if (!document.getElementById("pdf-ficha-autorizado").checked) {
            mensaje.textContent = "Debes confirmar los derechos para compartir el archivo."; return;
        }
        const data = new FormData();
        data.append("file", file);
        data.append("bookId", form.dataset.bookId);
        data.append("language", document.getElementById("pdf-ficha-idioma").value);
        data.append("editionIsbn", document.getElementById("pdf-ficha-isbn").value.trim());
        data.append("isAuthorized", "true");
        const button = form.querySelector("button[type=submit]");
        button.disabled = true;
        mensaje.textContent = "Subiendo archivo...";
        try {
            const response = await fetch("/api/files/upload", {method: "POST", body: data});
            if (!response.ok) throw new Error(response.status === 401 ? "Inicia sesión para subir archivos." :
                response.status === 409 ? "El archivo o ISBN ya está registrado." :
                `No se pudo subir el PDF (HTTP ${response.status}).`);
            window.location.reload();
        } catch (err) { mensaje.textContent = err.message; }
        finally { button.disabled = false; }
    });
});
