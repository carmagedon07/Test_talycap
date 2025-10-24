-- 0. Crear esquema lógico para la app
CREATE SCHEMA IF NOT EXISTS taskflow;

--  usar este esquema por defecto en esta sesión
SET search_path TO taskflow;

-- 1. Tabla de usuarios
CREATE TABLE taskflow.users (
    id              SERIAL PRIMARY KEY,
    name            VARCHAR(100)  NOT NULL,
    email           VARCHAR(150)  NOT NULL UNIQUE,
    password_hash   VARCHAR(255)  NOT NULL,
    created_at      TIMESTAMP     NOT NULL DEFAULT NOW()
);

-- 2. Tabla de tareas
CREATE TABLE taskflow.tasks (
    id            SERIAL PRIMARY KEY,
    user_id       INTEGER      NOT NULL,
    title         VARCHAR(150) NOT NULL,
    description   TEXT,
    priority      VARCHAR(20)  NOT NULL DEFAULT 'media',         -- alta | media | baja
    status        VARCHAR(20)  NOT NULL DEFAULT 'POR_HACER',      -- POR_HACER | EN_PROGRESO | COMPLETADA
    created_at    TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at    TIMESTAMP    NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_tasks_user
        FOREIGN KEY (user_id)
        REFERENCES taskflow.users(id)
        ON DELETE CASCADE
        ON UPDATE NO ACTION
);