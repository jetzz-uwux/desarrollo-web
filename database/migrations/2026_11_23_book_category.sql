-- Ejecutar una sola vez sobre una BD existente (psql -d booknest -f ...).
-- Conserva todos los libros y sus relaciones.
BEGIN;
ALTER TABLE public.books ADD COLUMN IF NOT EXISTS category VARCHAR(80);
CREATE INDEX IF NOT EXISTS idx_books_category ON public.books (category);
COMMIT;
