CREATE TABLE play_session
(
    id                  BIGSERIAL PRIMARY KEY,

    -- 식별자 ---------------------------------------
    user_id             VARCHAR(255) NOT NULL,
    media_playlist_id   BIGINT       NOT NULL,
    now_playing_item_id BIGINT       NOT NULL,

    -- 재생 위치 ------------------------------------
    start_seconds       INTEGER      NOT NULL DEFAULT 0,

    -- 큐(배열) -------------------------------------
    prev_items          BIGINT[]     NOT NULL DEFAULT '{}', -- 빈 bigint 배열
    next_items          BIGINT[]     NOT NULL DEFAULT '{}',

    -- 감사 정보 ------------------------------------
    created_at          TIMESTAMP,
    updated_at          TIMESTAMP,
    created_by          VARCHAR(255),
    updated_by          VARCHAR(255),

    -- 한 유저가 같은 플레이리스트에 대해 세션 하나만
    CONSTRAINT uq_play_session_user_playlist UNIQUE (user_id, media_playlist_id)
);

-- ▶︎ 조회 성능용 인덱스 (필요에 따라 선택)
CREATE INDEX idx_play_session_user ON play_session (user_id);
CREATE INDEX idx_play_session_playlist ON play_session (media_playlist_id);
CREATE INDEX idx_play_session_updated_at ON play_session (updated_at DESC);
