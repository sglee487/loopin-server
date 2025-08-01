ALTER TABLE playlist_item_mapping
    ADD COLUMN rank_key VARCHAR(64);

ALTER TABLE playlist_item_mapping
ALTER COLUMN position DROP NOT NULL;

-- -- ③ NOT NULL + 인덱스
-- ALTER TABLE playlist_item_mapping
--     ALTER COLUMN rank_key SET NOT NULL;
-- CREATE INDEX idx_pim_playlist_rank
--     ON playlist_item_mapping (playlist_id, rank_key);
--