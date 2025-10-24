INSERT INTO users (name, email, password_hash)
VALUES (
    'Usuario Demo',
    'demo@taskflow.com',
    '$2a$10$CwTycUXWue0Thq9StjUM0uJ8G9Y.srXe3G.yUpiRaY1oCbcnZ6FZm'
);

INSERT INTO tasks (user_id, title, description, priority, status)
VALUES
(1, 'Configurar entorno de desarrollo', 'Instalar backend y frontend', 'alta', 'POR_HACER'),
(1, 'Diseñar modelo de datos', 'Tablas users y tasks', 'media', 'COMPLETADA'),
(1, 'Implementar login con JWT', 'POST /auth/login devuelve token JWT', 'alta', 'EN_PROGRESO'),
(1, 'Pantalla Kanban', 'Columnas Por hacer / En progreso / Completada', 'media', 'POR_HACER'),
(1, 'Agregar filtros', 'Filtro por estado de tarea', 'baja', 'POR_HACER');

commit;