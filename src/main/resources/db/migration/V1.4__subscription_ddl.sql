CREATE TABLE subscription
(
    id         UUID PRIMARY KEY NOT NULL DEFAULT public.uuid_generate_v4(),
    account_id UUID             NOT NULL,
    channel_id UUID             NOT NULL,
    removed    BOOLEAN          NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ      NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ      NOT NULL DEFAULT now(),
    CONSTRAINT UK_ACCOUNT_ID_CHANNEL_ID UNIQUE (account_id, channel_id),
    CONSTRAINT FK_ACCOUNT_ID FOREIGN KEY (account_id) REFERENCES account (id),
    CONSTRAINT FK_CHANNEL_ID FOREIGN KEY (channel_id) REFERENCES channel (id)
);