document.addEventListener("DOMContentLoaded", () => {
    const alertBox = document.getElementById("alert-box");

    function showNotification(message, type = "success") {
        alertBox.textContent = message;
        alertBox.className = `alert ${type}`;
        alertBox.classList.remove("hidden");

        setTimeout(() => {
            alertBox.classList.add("hidden");
        }, 5000);
    }

    // Elementos de Archivos PDF
    const fileForm = document.getElementById("file-form");
    const fileFormTitle = document.getElementById("file-form-title");
    const fileIdInput = document.getElementById("file-id");
    const fileBookSelect = document.getElementById("file-book-select");
    const fileUserInput = document.getElementById("file-user-id");
    const fileInputGroup = document.getElementById("file-input-group");
    const fileInput = document.getElementById("file-pdf");
    const fileFilenameGroup = document.getElementById("file-filename-group");
    const fileFilenameInput = document.getElementById("file-original-filename");
    const fileLanguageSelect = document.getElementById("file-language");
    const fileIsbnInput = document.getElementById("file-isbn");
    const fileAuthorizedCheck = document.getElementById("file-authorized");
    const btnFileSubmit = document.getElementById("btn-file-submit");
    const btnFileCancel = document.getElementById("btn-file-cancel");

    const filterFileBook = document.getElementById("filter-file-book");
    const filterFileLang = document.getElementById("filter-file-language");
    const filterFileQ = document.getElementById("filter-file-q");
    const filesTbody = document.getElementById("files-tbody");

    // Elementos de Títulos Alternativos
    const altForm = document.getElementById("alt-title-form");
    const altFormTitle = document.getElementById("alt-title-form-title");
    const altIdInput = document.getElementById("alt-title-id");
    const altBookSelect = document.getElementById("alt-title-book-select");
    const altUserInput = document.getElementById("alt-title-user-id");
    const altTextInput = document.getElementById("alt-title-text");
    const altLangSelect = document.getElementById("alt-title-language");
    const btnAltSubmit = document.getElementById("btn-alt-submit");
    const btnAltCancel = document.getElementById("btn-alt-cancel");

    const filterAltBook = document.getElementById("filter-alt-book");
    const filterAltLang = document.getElementById("filter-alt-language");
    const filterAltQ = document.getElementById("filter-alt-q");
    const altTbody = document.getElementById("alt-titles-tbody");

    // Prellenar ID de usuario si hay sesión en localStorage
    const savedUserId = localStorage.getItem("usuarioId");
    if (savedUserId) {
        fileUserInput.value = savedUserId;
        altUserInput.value = savedUserId;
    }

    // --- CARGAR LIBROS EN SELECTORES ---
    async function loadBooksForFiles() {
        try {
            const books = await BookNestAPI.getBooks();
            const defaultOpt = '<option value="">-- Selecciona un libro --</option>';
            const allOpt = '<option value="">Todos los libros</option>';

            fileBookSelect.innerHTML = defaultOpt;
            altBookSelect.innerHTML = defaultOpt;
            filterFileBook.innerHTML = allOpt;
            filterAltBook.innerHTML = allOpt;

            books.forEach(b => {
                const opt = `<option value="${b.id}">${b.id} - ${b.mainTitle}</option>`;
                fileBookSelect.innerHTML += opt;
                altBookSelect.innerHTML += opt;
                filterFileBook.innerHTML += opt;
                filterAltBook.innerHTML += opt;
            });
        } catch (error) {
            showNotification(`Error al cargar libros: ${error.message}`, "error");
        }
    }

    // ==================== ARCHIVOS PDF ====================
    async function loadFiles() {
        filesTbody.innerHTML = '<tr><td colspan="7" class="text-center">Cargando...</td></tr>';
        try {
            const files = await BookNestAPI.getFiles(
                filterFileBook.value,
                filterFileLang.value,
                filterFileQ.value.trim()
            );

            if (!files || files.length === 0) {
                filesTbody.innerHTML = '<tr><td colspan="7" class="text-center">No hay archivos registrados.</td></tr>';
                return;
            }

            filesTbody.innerHTML = files.map(f => {
                const bookLabel = f.book ? `${f.book.mainTitle} (#${f.book.id})` : "N/A";
                const sizeKb = f.fileSizeBytes ? (f.fileSizeBytes / 1024).toFixed(1) + " KB" : "0 KB";
                const downloadUrl = BookNestAPI.getDownloadUrl(f.id);

                return `
                    <tr>
                        <td>${f.id}</td>
                        <td>${bookLabel}</td>
                        <td>${f.originalFilename || "documento.pdf"}</td>
                        <td>${f.language ? f.language.toUpperCase() : "-"}</td>
                        <td>${f.editionIsbn || "Sin ISBN"}</td>
                        <td>${sizeKb}</td>
                        <td>
                            <a href="${downloadUrl}" class="btn btn-primary btn-sm" target="_blank">Descargar</a>
                            <button type="button" class="btn btn-outline btn-sm" onclick='prepareEditFile(${JSON.stringify(f)})'>Editar</button>
                            <button type="button" class="btn btn-danger btn-sm" onclick="deleteFileItem(${f.id})">Eliminar</button>
                        </td>
                    </tr>
                `;
            }).join("");
        } catch (error) {
            filesTbody.innerHTML = '<tr><td colspan="7" class="text-center">Error al cargar archivos.</td></tr>';
            showNotification(`Error al consultar archivos: ${error.message}`, "error");
        }
    }

    fileForm.addEventListener("submit", async (e) => {
        e.preventDefault();
        const fileId = fileIdInput.value;

        try {
            if (fileId) {
                // MODO EDICIÓN (PUT /api/files/{id})
                const payload = {
                    language: fileLanguageSelect.value,
                    editionIsbn: fileIsbnInput.value.trim(),
                    originalFilename: fileFilenameInput.value.trim(),
                    isAuthorized: fileAuthorizedCheck.checked
                };
                await BookNestAPI.updateFile(fileId, payload);
                showNotification(`Archivo #${fileId} actualizado correctamente.`, "success");
            } else {
                // MODO SUBIDA (POST /api/files/upload)
                const pdfFile = fileInput.files[0];
                if (!pdfFile) {
                    showNotification("Debes seleccionar un archivo PDF.", "error");
                    return;
                }

                const formData = new FormData();
                formData.append("file", pdfFile);
                formData.append("bookId", fileBookSelect.value);
                if (fileUserInput.value) {
                    formData.append("uploaderUserId", fileUserInput.value);
                }
                formData.append("language", fileLanguageSelect.value);
                if (fileIsbnInput.value.trim()) {
                    formData.append("editionIsbn", fileIsbnInput.value.trim());
                }
                formData.append("isAuthorized", fileAuthorizedCheck.checked);

                const created = await BookNestAPI.uploadFile(formData);
                showNotification(`Archivo "${created.originalFilename}" subido con éxito (ID #${created.id}).`, "success");
            }

            resetFileForm();
            loadFiles();
        } catch (error) {
            showNotification(`Error en archivo: ${error.message}`, "error");
        }
    });

    window.prepareEditFile = (fileObj) => {
        fileIdInput.value = fileObj.id;
        fileBookSelect.value = fileObj.book ? fileObj.book.id : "";
        fileBookSelect.disabled = true;
        fileUserInput.disabled = true;

        fileInputGroup.classList.add("hidden");
        fileInput.required = false;

        fileFilenameGroup.classList.remove("hidden");
        fileFilenameInput.value = fileObj.originalFilename || "";
        fileFilenameInput.required = true;

        fileLanguageSelect.value = fileObj.language || "es";
        fileIsbnInput.value = fileObj.editionIsbn || "";
        fileAuthorizedCheck.checked = fileObj.isAuthorized !== false;

        fileFormTitle.textContent = `Editando Archivo #${fileObj.id}`;
        btnFileSubmit.textContent = "Actualizar Metadatos";
        btnFileCancel.classList.remove("hidden");
    };

    window.deleteFileItem = async (id) => {
        if (!confirm(`¿Seguro que deseas eliminar el archivo PDF #${id}?`)) return;
        try {
            await BookNestAPI.deleteFile(id);
            showNotification(`Archivo #${id} eliminado correctamente.`, "success");
            loadFiles();
        } catch (error) {
            showNotification(`Error al eliminar archivo: ${error.message}`, "error");
        }
    };

    function resetFileForm() {
        fileForm.reset();
        fileIdInput.value = "";
        fileBookSelect.disabled = false;
        fileUserInput.disabled = false;
        if (savedUserId) fileUserInput.value = savedUserId;

        fileInputGroup.classList.remove("hidden");
        fileInput.required = true;

        fileFilenameGroup.classList.add("hidden");
        fileFilenameInput.required = false;

        fileAuthorizedCheck.checked = true;
        fileFormTitle.textContent = "Subir Archivo PDF";
        btnFileSubmit.textContent = "Subir PDF";
        btnFileCancel.classList.add("hidden");
    }

    btnFileCancel.addEventListener("click", resetFileForm);
    document.getElementById("btn-filter-files").addEventListener("click", loadFiles);

    // ==================== TÍTULOS ALTERNATIVOS ====================
    async function loadAltTitles() {
        altTbody.innerHTML = '<tr><td colspan="5" class="text-center">Cargando...</td></tr>';
        try {
            const list = await BookNestAPI.getAlternativeTitles(
                filterAltBook.value,
                filterAltLang.value,
                filterAltQ.value.trim()
            );

            if (!list || list.length === 0) {
                altTbody.innerHTML = '<tr><td colspan="5" class="text-center">No hay títulos alternativos registrados.</td></tr>';
                return;
            }

            altTbody.innerHTML = list.map(t => {
                const bookLabel = t.book ? `${t.book.mainTitle} (#${t.book.id})` : "N/A";
                return `
                    <tr>
                        <td>${t.id}</td>
                        <td>${bookLabel}</td>
                        <td>${t.title}</td>
                        <td>${t.language ? t.language.toUpperCase() : "-"}</td>
                        <td>
                            <button type="button" class="btn btn-outline btn-sm" onclick='prepareEditAltTitle(${JSON.stringify(t)})'>Editar</button>
                            <button type="button" class="btn btn-danger btn-sm" onclick="deleteAltTitleItem(${t.id})">Eliminar</button>
                        </td>
                    </tr>
                `;
            }).join("");
        } catch (error) {
            altTbody.innerHTML = '<tr><td colspan="5" class="text-center">Error al cargar títulos.</td></tr>';
            showNotification(`Error al consultar títulos alternativos: ${error.message}`, "error");
        }
    }

    altForm.addEventListener("submit", async (e) => {
        e.preventDefault();
        const altId = altIdInput.value;

        const payload = {
            book: { id: Number(altBookSelect.value) },
            title: altTextInput.value.trim(),
            language: altLangSelect.value
        };

        if (altUserInput.value) {
            payload.contributedByUser = { id: Number(altUserInput.value) };
        }

        try {
            if (altId) {
                await BookNestAPI.updateAlternativeTitle(altId, payload);
                showNotification(`Título alternativo #${altId} actualizado correctamente.`, "success");
            } else {
                await BookNestAPI.createAlternativeTitle(payload);
                showNotification("Título alternativo registrado correctamente.", "success");
            }
            resetAltForm();
            loadAltTitles();
        } catch (error) {
            showNotification(`Error al guardar título alternativo: ${error.message}`, "error");
        }
    });

    window.prepareEditAltTitle = (item) => {
        altIdInput.value = item.id;
        altBookSelect.value = item.book ? item.book.id : "";
        altBookSelect.disabled = true;
        altTextInput.value = item.title || "";
        altLangSelect.value = item.language || "es";

        altFormTitle.textContent = `Editando Título #${item.id}`;
        btnAltSubmit.textContent = "Actualizar Título";
        btnAltCancel.classList.remove("hidden");
    };

    window.deleteAltTitleItem = async (id) => {
        if (!confirm(`¿Eliminar el título alternativo #${id}?`)) return;
        try {
            await BookNestAPI.deleteAlternativeTitle(id);
            showNotification(`Título alternativo #${id} eliminado.`, "success");
            loadAltTitles();
        } catch (error) {
            showNotification(`Error al eliminar título alternativo: ${error.message}`, "error");
        }
    };

    function resetAltForm() {
        altForm.reset();
        altIdInput.value = "";
        altBookSelect.disabled = false;
        if (savedUserId) altUserInput.value = savedUserId;
        altFormTitle.textContent = "Aportar Título Alternativo";
        btnAltSubmit.textContent = "Guardar Título";
        btnAltCancel.classList.add("hidden");
    }

    btnAltCancel.addEventListener("click", resetAltForm);
    document.getElementById("btn-filter-alt").addEventListener("click", loadAltTitles);

    // Recargar datos al hacer clic en la pestaña "Archivos"
    const navBtnArchivos = document.querySelector('.nav-btn[data-target="seccion-archivos"]');
    if (navBtnArchivos) {
        navBtnArchivos.addEventListener("click", () => {
            loadBooksForFiles();
            loadFiles();
            loadAltTitles();
        });
    }
});