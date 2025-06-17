CREATE TABLE media_item
(
    id                        BIGSERIAL PRIMARY KEY,
    resource_id               TEXT      NOT NULL,
    title                     TEXT      NOT NULL,
    description               TEXT,
    kind                      TEXT,
    published_at              TIMESTAMP NOT NULL,
    thumbnail                 TEXT,
    video_owner_channel_id    TEXT,
    video_owner_channel_title TEXT,
    platform_type             TEXT      NOT NULL,
    created_at                TIMESTAMP,
    updated_at                TIMESTAMP,
    created_by                TEXT,
    updated_by                TEXT
);
COMMENT
    ON COLUMN media_item.thumbnail IS 'URL of the thumbnail image';

CREATE TABLE media_playlist
(
    id            BIGSERIAL PRIMARY KEY,
    resource_id   TEXT      NOT NULL,
    title         TEXT      NOT NULL,
    description   TEXT,
    kind          TEXT,
    thumbnail     TEXT,
    channel_id    TEXT      NOT NULL,
    channel_title TEXT      NOT NULL,
    published_at  TIMESTAMP NOT NULL,
    platform_type TEXT      NOT NULL,
    created_at    TIMESTAMP,
    updated_at    TIMESTAMP,
    created_by    TEXT,
    updated_by    TEXT
);
COMMENT
    ON COLUMN media_playlist.thumbnail IS 'URL of the thumbnail image';

CREATE TABLE playlist_item_mapping
(
    id            BIGSERIAL PRIMARY KEY,
    playlist_id   BIGINT NOT NULL REFERENCES media_playlist (id) ON DELETE CASCADE,
    media_item_id BIGINT NOT NULL REFERENCES media_item (id) ON DELETE CASCADE,
    position      INT    NOT NULL,
    created_at    TIMESTAMP,
    updated_at    TIMESTAMP,
    UNIQUE (playlist_id, media_item_id)
);

CREATE TABLE media_playlist_content_details
(
    media_playlist_id BIGSERIAL PRIMARY KEY REFERENCES media_playlist (id),
    item_count        INT NOT NULL,
    created_at        TIMESTAMP,
    updated_at        TIMESTAMP
);
