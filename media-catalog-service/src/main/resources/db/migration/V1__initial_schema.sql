CREATE TABLE media_item
(
    id                        BIGSERIAL PRIMARY KEY,
    resource_id               TEXT      NOT NULL,
    published_at              TIMESTAMP NOT NULL,
    uploader_channel_id       TEXT      NOT NULL,
    thumbnail                 TEXT,
    video_owner_channel_id    TEXT,
    video_owner_channel_title TEXT,
    platform_type             TEXT      NOT NULL,
    created_at                TIMESTAMP NOT NULL,
    updated_at                TIMESTAMP NULL,
    created_by                TEXT,
    updated_by                TEXT
);
COMMENT
ON COLUMN media_item.thumbnail IS 'URL of the thumbnail image';

CREATE TABLE media_playlist
(
    id            BIGSERIAL PRIMARY KEY,
    resource_id   TEXT      NOT NULL,
    thumbnail     TEXT,
    channel_id    TEXT      NOT NULL,
    channel_title TEXT      NOT NULL,
    published_at  TIMESTAMP NOT NULL,
    platform_type TEXT      NOT NULL,
    created_by                TEXT,
    updated_by                TEXT
);
COMMENT
ON COLUMN media_playlist.thumbnail IS 'URL of the thumbnail image';

CREATE TABLE playlist_item_mapping
(
    playlist_id   BIGSERIAL NOT NULL REFERENCES media_playlist (id),
    media_item_id BIGSERIAL NOT NULL REFERENCES media_item (id),
    position      INT       NOT NULL,
    PRIMARY KEY (playlist_id, media_item_id)
);

CREATE TABLE media_content_details
(
    media_item_id BIGSERIAL PRIMARY KEY REFERENCES media_item (id),
    duration      INT     NOT NULL,
    caption       BOOLEAN NOT NULL
);

CREATE TABLE media_item_localization
(
    media_item_id BIGSERIAL NOT NULL REFERENCES media_item (id),
    language_code CHAR(2)   NOT NULL,
    title         TEXT      NOT NULL,
    description   TEXT,
    PRIMARY KEY (media_item_id, language_code)
);

CREATE TABLE media_playlist_localization
(
    media_playlist_id BIGSERIAL NOT NULL REFERENCES media_playlist (id),
    language_code     CHAR(2)   NOT NULL,
    title             TEXT      NOT NULL,
    description       TEXT,
    PRIMARY KEY (media_playlist_id, language_code)
);

CREATE TABLE media_playlist_content_details
(
    media_playlist_id BIGSERIAL PRIMARY KEY REFERENCES media_playlist (id),
    item_count        INT NOT NULL
);