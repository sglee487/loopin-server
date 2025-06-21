-- 1. 컬럼 추가 (NULL 허용)
ALTER TABLE media_playlist
    ADD COLUMN item_count INT;

-- 2. 기존 데이터 이관
UPDATE media_playlist mp
SET    item_count = mpcd.item_count
FROM   media_playlist_content_details mpcd
WHERE  mp.id = mpcd.media_playlist_id;

-- 3. NOT NULL + 기본값 제약 (필요하면)
ALTER TABLE media_playlist
    ALTER COLUMN item_count SET NOT NULL;

-- 4. 이미 FK 타고 들어오는 곳이 없으므로
DROP TABLE media_playlist_content_details;
