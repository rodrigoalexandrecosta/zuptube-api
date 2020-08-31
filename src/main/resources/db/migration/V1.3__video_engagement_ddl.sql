CREATE TABLE video_engagement
(
    id         UUID PRIMARY KEY NOT NULL DEFAULT public.uuid_generate_v4(),
    comment    VARCHAR(2000),
    liked      BOOLEAN                   DEFAULT FALSE,
    account_id UUID             NOT NULL,
    video_id   UUID             NOT NULL,
    removed    BOOLEAN          NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ      NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ      NOT NULL DEFAULT now(),
    CONSTRAINT UK_ACCOUNT_ID_VIDEO_ID UNIQUE (account_id, video_id),
    CONSTRAINT FK_ACCOUNT_ID FOREIGN KEY (account_id) REFERENCES account (id),
    CONSTRAINT FK_VIDEO_ID FOREIGN KEY (video_id) REFERENCES video (id)
);