CREATE TABLE video
(
    id          UUID PRIMARY KEY NOT NULL DEFAULT public.uuid_generate_v4(),
    title       VARCHAR(255)     NOT NULL,
    description VARCHAR(2000)    NOT NULL,
    file_path   VARCHAR(2000)    NOT NULL,
    channel_id  UUID             NOT NULL,
    removed     BOOLEAN          NOT NULL DEFAULT FALSE,
    created_at  TIMESTAMPTZ      NOT NULL DEFAULT now(),
    updated_at  TIMESTAMPTZ      NOT NULL DEFAULT now(),
    CONSTRAINT FK_CHANNEL_ID FOREIGN KEY (channel_id) REFERENCES channel (id)
);