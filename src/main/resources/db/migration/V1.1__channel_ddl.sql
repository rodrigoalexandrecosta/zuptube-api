CREATE TABLE channel
(
    id          UUID PRIMARY KEY NOT NULL DEFAULT public.uuid_generate_v4(),
    name        VARCHAR(255)     NOT NULL,
    description VARCHAR(1000)    NOT NULL,
    account_id  UUID             NOT NULL,
    removed     BOOLEAN          NOT NULL DEFAULT FALSE,
    created_at  TIMESTAMPTZ      NOT NULL DEFAULT now(),
    updated_at  TIMESTAMPTZ      NOT NULL DEFAULT now(),
    CONSTRAINT FK_ACCOUNT_ID FOREIGN KEY (account_id) REFERENCES account (id)
);