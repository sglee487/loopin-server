-- Add public_id column to stream_session table
ALTER TABLE stream_session 
ADD COLUMN public_id VARCHAR(255) UNIQUE NOT NULL DEFAULT gen_random_uuid()::text;

-- Create index for public_id
CREATE INDEX idx_stream_session_public_id ON stream_session(public_id);

-- Update existing records with unique public_id values
UPDATE stream_session SET public_id = gen_random_uuid()::text WHERE public_id IS NULL;