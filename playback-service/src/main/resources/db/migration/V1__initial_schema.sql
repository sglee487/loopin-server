CREATE TABLE play_session
(
    id                  BIGSERIAL PRIMARY KEY,

    user_id             VARCHAR(255) NOT NULL,
    media_playlist_id   VARCHAR(255) NOT NULL,
    now_playing_item_id VARCHAR(255) NOT NULL,

    start_seconds       BIGINT       NOT NULL DEFAULT 0,

    prev_items          TEXT[]       NOT NULL DEFAULT '{}',
    next_items          TEXT[]       NOT NULL DEFAULT '{}',

    created_at          TIMESTAMP,
    updated_at          TIMESTAMP,
    created_by          TEXT,
    updated_by          TEXT,

    UNIQUE (user_id, media_playlist_id)
);

-- ▶︎ 조회 성능용 인덱스 (필요에 따라 선택)
CREATE INDEX idx_play_session_user ON play_session (user_id);
CREATE INDEX idx_play_session_playlist ON play_session (media_playlist_id);
CREATE INDEX idx_play_session_updated_at ON play_session (updated_at DESC);
