CREATE TABLE account
(
    id         UUID PRIMARY KEY NOT NULL DEFAULT public.uuid_generate_v4(),
    first_name VARCHAR(255)     NOT NULL,
    last_name  VARCHAR(255)     NOT NULL,
    email      VARCHAR(255)     NOT NULL UNIQUE,
    phone      VARCHAR(255)     NOT NULL UNIQUE,
    password   TEXT             NOT NULL,
    locale     VARCHAR(25)      NOT NULL DEFAULT 'pt-BR',
    timezone   VARCHAR(50)      NOT NULL DEFAULT 'America/Sao_Paulo',
    removed    BOOLEAN          NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ      NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ      NOT NULL DEFAULT now()
);