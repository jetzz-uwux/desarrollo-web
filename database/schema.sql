-- =====================================================================
-- BookNest · Esquema PostgreSQL 
-- Backend: Spring Boot (JPA/Hibernate) · snake_case · tablas en plural
-- Uso (BD vacía):  psql -d booknest -f schema.sql
-- Sin datos de prueba. Todo corre en una sola transacción (todo o nada).
-- =====================================================================
BEGIN;

-- Búsqueda por palabras clave/título/autor con ILIKE '%texto%' (RF-01, RF-25).
-- Si tu usuario no es superusuario, pide a quien administre la BD: CREATE EXTENSION pg_trgm;
CREATE EXTENSION IF NOT EXISTS pg_trgm;

-- ---------------------------------------------------------------------
-- CONTRATO DE VALORES
--   series_type            : 'bilogia' | 'trilogia' | 'saga'
--   language (archivos y títulos alternativos) : 'es' | 'en'
--   user_reading_progress.status : 'pendiente' | 'leyendo' | 'terminado'
-- Se usan VARCHAR + CHECK (en JPA: @Enumerated(EnumType.STRING)).
--
-- POLÍTICA DE BORRADO (ON DELETE)
--   CASCADE  : datos propios del usuario (colecciones, reseñas, calificaciones,
--              progreso) y datos descriptivos que cuelgan de la ficha
--              (book_authors, book_genres, book_series, alternative_titles).
--   RESTRICT : catálogo crítico (no se puede borrar un libro, autor, género o
--              serie que aún tenga contenido asociado).
--   SET NULL : aportaciones comunitarias (libros, PDFs, títulos). Si el usuario
--              se elimina, lo aportado se conserva en el catálogo compartido.

-- MÓDULO DE USUARIOS

CREATE TABLE users (
    id            BIGSERIAL PRIMARY KEY,
    username      VARCHAR(50)  NOT NULL,
    email         VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,          -- hash (BCrypt/Argon2), nunca texto plano
    bio           TEXT,                           -- perfil personal básico (RF-10), opcional
    avatar_url    VARCHAR(500),                   -- opcional
    created_at    TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_users_username UNIQUE (username),
    CONSTRAINT uq_users_email    UNIQUE (email),
    CONSTRAINT ck_users_username_not_blank CHECK (btrim(username) <> '')
);
-- Evita duplicados que solo difieren en mayúsculas (Ana@x.com vs ana@x.com)
CREATE UNIQUE INDEX uq_users_username_lower ON users (LOWER(username));
CREATE UNIQUE INDEX uq_users_email_lower    ON users (LOWER(email));

-- MÓDULO DE CATÁLOGO (FICHA COMPARTIDA)

CREATE TABLE authors (
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,             -- sin UNIQUE: pueden existir homónimos
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_authors_name_not_blank CHECK (btrim(name) <> '')
);
CREATE INDEX idx_authors_name_trgm ON authors USING GIN (name gin_trgm_ops);
CREATE INDEX idx_authors_name_lower ON authors (LOWER(name));

CREATE TABLE genres (
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(100) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_genres_name UNIQUE (name),
    CONSTRAINT ck_genres_name_not_blank CHECK (btrim(name) <> '')
);

CREATE TABLE literary_series (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    series_type VARCHAR(20)  NOT NULL,
    created_at  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_literary_series_type CHECK (series_type IN ('bilogia', 'trilogia', 'saga')),
    CONSTRAINT ck_literary_series_name_not_blank CHECK (btrim(name) <> '')
);
CREATE INDEX idx_literary_series_name ON literary_series (name);
CREATE INDEX idx_literary_series_type ON literary_series (series_type);   -- filtro por tipo de serie (RF-02)

CREATE TABLE books (
    id                        BIGSERIAL PRIMARY KEY,
    main_title                VARCHAR(500) NOT NULL,
    original_publication_date DATE,
    synopsis                  TEXT,
    total_pages               INTEGER      NOT NULL,   -- base del % de progreso (RF-29)
    cover_url                 VARCHAR(500),
    created_by_user_id        BIGINT,                  -- quién creó la ficha (catálogo colaborativo)
    created_at                TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at                TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_books_created_by FOREIGN KEY (created_by_user_id)
        REFERENCES users (id) ON DELETE SET NULL,
    CONSTRAINT ck_books_total_pages CHECK (total_pages > 0),
    CONSTRAINT ck_books_title_not_blank CHECK (btrim(main_title) <> '')
);
CREATE INDEX idx_books_main_title_trgm   ON books USING GIN (main_title gin_trgm_ops);
CREATE INDEX idx_books_main_title_lower  ON books (LOWER(main_title));    -- orden alfabético / igualdad
CREATE INDEX idx_books_total_pages       ON books (total_pages);          -- orden/filtro por extensión
CREATE INDEX idx_books_publication_date  ON books (original_publication_date);
CREATE INDEX idx_books_created_by        ON books (created_by_user_id);

-- M:M libros <-> autores
CREATE TABLE book_authors (
    id         BIGSERIAL PRIMARY KEY,
    book_id    BIGINT NOT NULL,
    author_id  BIGINT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_book_authors_book   FOREIGN KEY (book_id)   REFERENCES books (id)   ON DELETE CASCADE,
    CONSTRAINT fk_book_authors_author FOREIGN KEY (author_id) REFERENCES authors (id) ON DELETE RESTRICT,
    CONSTRAINT uq_book_authors UNIQUE (book_id, author_id)      -- no repetir el par
);
CREATE INDEX idx_book_authors_author ON book_authors (author_id);

-- M:M libros <-> géneros
CREATE TABLE book_genres (
    id         BIGSERIAL PRIMARY KEY,
    book_id    BIGINT NOT NULL,
    genre_id   BIGINT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_book_genres_book  FOREIGN KEY (book_id)  REFERENCES books (id)  ON DELETE CASCADE,
    CONSTRAINT fk_book_genres_genre FOREIGN KEY (genre_id) REFERENCES genres (id) ON DELETE RESTRICT,
    CONSTRAINT uq_book_genres UNIQUE (book_id, genre_id)
);
CREATE INDEX idx_book_genres_genre ON book_genres (genre_id);

-- Pivote libros <-> series, con volumen y orden de lectura
CREATE TABLE book_series (
    id            BIGSERIAL PRIMARY KEY,
    book_id       BIGINT  NOT NULL,
    series_id     BIGINT  NOT NULL,
    volume_number INTEGER NOT NULL,
    reading_order INTEGER NOT NULL,
    created_at    TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_book_series_book   FOREIGN KEY (book_id)   REFERENCES books (id)           ON DELETE CASCADE,
    CONSTRAINT fk_book_series_series FOREIGN KEY (series_id) REFERENCES literary_series (id) ON DELETE RESTRICT,
    CONSTRAINT uq_book_series_pair   UNIQUE (book_id, series_id),        -- un libro no se repite en la misma serie
    CONSTRAINT uq_book_series_volume UNIQUE (series_id, volume_number),  -- un solo libro por volumen
    CONSTRAINT ck_book_series_volume CHECK (volume_number > 0),
    CONSTRAINT ck_book_series_order  CHECK (reading_order > 0)
);
CREATE INDEX idx_book_series_order ON book_series (series_id, reading_order);

-- Títulos alternativos (1:M con books)
CREATE TABLE alternative_titles (
    id                    BIGSERIAL PRIMARY KEY,
    book_id               BIGINT       NOT NULL,
    title                 VARCHAR(500) NOT NULL,
    language              VARCHAR(2)   NOT NULL,
    contributed_by_user_id BIGINT,                 -- quién aportó el título (RF-26)
    created_at            TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at            TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_alt_titles_book FOREIGN KEY (book_id)
        REFERENCES books (id) ON DELETE CASCADE,
    CONSTRAINT fk_alt_titles_contributor FOREIGN KEY (contributed_by_user_id)
        REFERENCES users (id) ON DELETE SET NULL,
    CONSTRAINT ck_alt_titles_language CHECK (language IN ('es', 'en')),
    CONSTRAINT ck_alt_titles_not_blank CHECK (btrim(title) <> ''),
    CONSTRAINT uq_alt_titles_book_title_lang UNIQUE (book_id, title, language)
);
CREATE INDEX idx_alt_titles_book        ON alternative_titles (book_id);
CREATE INDEX idx_alt_titles_title_trgm  ON alternative_titles USING GIN (title gin_trgm_ops);
CREATE INDEX idx_alt_titles_contributor ON alternative_titles (contributed_by_user_id);

-- MÓDULO DE ARCHIVOS COLABORATIVOS (PDFs)

CREATE TABLE book_files (
    id               BIGSERIAL PRIMARY KEY,
    book_id          BIGINT       NOT NULL,
    uploader_user_id BIGINT,                          -- NULL si el usuario se eliminó; el archivo se conserva
    file_path        VARCHAR(500) NOT NULL,           -- ruta/clave interna generada por el servidor
    file_size_bytes  BIGINT       NOT NULL,
    language         VARCHAR(2)   NOT NULL,
    edition_isbn     VARCHAR(17),                     -- opcional; normalizar en backend (sin guiones)
    is_authorized    BOOLEAN      NOT NULL DEFAULT FALSE,  -- dominio público / propio / con permiso (RNF-12)
    original_filename VARCHAR(255),                   -- nombre sugerido para la descarga
    checksum_sha256   CHAR(64),                       -- detectar PDFs idénticos
    created_at       TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_book_files_book     FOREIGN KEY (book_id)          REFERENCES books (id) ON DELETE RESTRICT,
    CONSTRAINT fk_book_files_uploader FOREIGN KEY (uploader_user_id) REFERENCES users (id) ON DELETE SET NULL,
    CONSTRAINT uq_book_files_path UNIQUE (file_path),
    CONSTRAINT ck_book_files_size     CHECK (file_size_bytes > 0),
    CONSTRAINT ck_book_files_language CHECK (language IN ('es', 'en')),
    CONSTRAINT ck_book_files_isbn     CHECK (edition_isbn IS NULL OR edition_isbn ~ '^[0-9Xx-]{10,17}$')
);
-- Un ISBN de edición solo puede aparecer una vez (los NULL no cuentan). Quitar si el equipo lo relaja.
CREATE UNIQUE INDEX uq_book_files_edition_isbn ON book_files (edition_isbn) WHERE edition_isbn IS NOT NULL;
CREATE INDEX idx_book_files_book     ON book_files (book_id);
CREATE INDEX idx_book_files_uploader ON book_files (uploader_user_id);
CREATE INDEX idx_book_files_language ON book_files (language);            -- filtro por idioma (RF-02)
CREATE INDEX idx_book_files_checksum ON book_files (checksum_sha256);

-- MÓDULO DE BIBLIOTECA PERSONAL

CREATE TABLE collections (
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT       NOT NULL,
    name       VARCHAR(150) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_collections_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT uq_collections_user_name UNIQUE (user_id, name),   -- sin colecciones repetidas por usuario
    CONSTRAINT ck_collections_name_not_blank CHECK (btrim(name) <> '')
);

CREATE TABLE collection_books (
    id            BIGSERIAL PRIMARY KEY,
    collection_id BIGINT NOT NULL,
    book_id       BIGINT NOT NULL,
    created_at    TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,  -- fecha en que se agregó
    updated_at    TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_collection_books_collection FOREIGN KEY (collection_id) REFERENCES collections (id) ON DELETE CASCADE,
    -- Quitar un libro de la colección = borrar esta fila; el libro del catálogo nunca se toca (RF-07)
    CONSTRAINT fk_collection_books_book FOREIGN KEY (book_id) REFERENCES books (id) ON DELETE RESTRICT,
    CONSTRAINT uq_collection_books UNIQUE (collection_id, book_id)
);
CREATE INDEX idx_collection_books_book ON collection_books (book_id);

CREATE TABLE user_reading_progress (
    id           BIGSERIAL PRIMARY KEY,
    user_id      BIGINT      NOT NULL,
    book_id      BIGINT      NOT NULL,
    status       VARCHAR(20) NOT NULL DEFAULT 'pendiente',
    current_page INTEGER     NOT NULL DEFAULT 0,
    finished_at  TIMESTAMP WITH TIME ZONE,            -- la fija el backend al pasar a 'terminado' (actividad mensual, RF-13)
    created_at   TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_progress_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_progress_book FOREIGN KEY (book_id) REFERENCES books (id) ON DELETE RESTRICT,
    CONSTRAINT uq_progress_user_book UNIQUE (user_id, book_id),   -- CRÍTICO: un solo estado por libro y usuario
    CONSTRAINT ck_progress_status CHECK (status IN ('pendiente', 'leyendo', 'terminado')),
    CONSTRAINT ck_progress_current_page CHECK (current_page >= 0) -- CRÍTICO; el tope (<= total_pages) lo valida el backend
);
CREATE INDEX idx_progress_book        ON user_reading_progress (book_id);
CREATE INDEX idx_progress_user_status ON user_reading_progress (user_id, status);            -- estadísticas personales
CREATE INDEX idx_progress_finished    ON user_reading_progress (book_id) WHERE status = 'terminado';  -- "más leídos" (RF-20)


-- MÓDULO DE INTERACCIÓN COMUNITARIA

CREATE TABLE reviews (
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT NOT NULL,
    book_id    BIGINT NOT NULL,
    content    TEXT   NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_reviews_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_reviews_book FOREIGN KEY (book_id) REFERENCES books (id) ON DELETE RESTRICT,
    CONSTRAINT ck_reviews_content_not_blank CHECK (btrim(content) <> '')
);
CREATE INDEX idx_reviews_book_created ON reviews (book_id, created_at DESC);   -- reseñas de una ficha, recientes primero
CREATE INDEX idx_reviews_user         ON reviews (user_id);

CREATE TABLE ratings (
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT   NOT NULL,
    book_id    BIGINT   NOT NULL,
    score      SMALLINT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_ratings_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_ratings_book FOREIGN KEY (book_id) REFERENCES books (id) ON DELETE RESTRICT,
    CONSTRAINT uq_ratings_user_book UNIQUE (user_id, book_id),     -- CRÍTICO: una calificación activa por usuario y obra
    CONSTRAINT ck_ratings_score CHECK (score BETWEEN 1 AND 5)      -- CRÍTICO
);
CREATE INDEX idx_ratings_book ON ratings (book_id);                -- promedio y ranking por libro

-- VISTAS DE SOLO LECTURA (derivados; no se guarda nada redundante => 3FN)

-- Promedio y cantidad de valoraciones (RF-16, 17, 18). average_score NULL => mostrar «Sin calificar».
CREATE VIEW v_book_rating_stats AS
SELECT b.id                           AS book_id,
       COUNT(r.id)                    AS ratings_count,
       ROUND(AVG(r.score)::NUMERIC, 2) AS average_score
FROM books b
LEFT JOIN ratings r ON r.book_id = b.id
GROUP BY b.id;

-- «Más leídos»: usuarios distintos que marcaron el libro como terminado (RF-20).
CREATE VIEW v_book_read_counts AS
SELECT b.id                      AS book_id,
       COUNT(p.user_id)          AS finished_readers_count
FROM books b
LEFT JOIN user_reading_progress p ON p.book_id = b.id AND p.status = 'terminado'
GROUP BY b.id;


-- updated_at AUTOMÁTICO en TODAS las tablas (funciona aunque no pase por Hibernate)

CREATE OR REPLACE FUNCTION set_updated_at() RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DO $$
DECLARE
    t TEXT;
BEGIN
    FOREACH t IN ARRAY ARRAY[
        'users', 'authors', 'genres', 'literary_series', 'books',
        'book_authors', 'book_genres', 'book_series', 'alternative_titles',
        'book_files', 'collections', 'collection_books',
        'user_reading_progress', 'reviews', 'ratings'
    ]
    LOOP
        EXECUTE format(
            'CREATE TRIGGER trg_%1$s_set_updated_at BEFORE UPDATE ON %1$I
             FOR EACH ROW EXECUTE FUNCTION set_updated_at()', t);
    END LOOP;
END;
$$;

COMMIT;