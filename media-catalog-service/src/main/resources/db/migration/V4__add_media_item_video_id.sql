ALTER TABLE media_item
    ADD COLUMN video_id VARCHAR(68);

CREATE INDEX idx_media_item_video_id ON media_item (video_id);