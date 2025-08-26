-- Create stream_session table
CREATE TABLE stream_session (
    id BIGSERIAL PRIMARY KEY,
    stream_key VARCHAR(255) UNIQUE NOT NULL,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'CREATED',
    streamer_id VARCHAR(255) NOT NULL,
    resolution VARCHAR(10) NOT NULL DEFAULT '720p',
    bitrate INTEGER NOT NULL DEFAULT 2500,
    viewers_count INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Create video_segment table
CREATE TABLE video_segment (
    id BIGSERIAL PRIMARY KEY,
    stream_session_id BIGINT NOT NULL REFERENCES stream_session(id) ON DELETE CASCADE,
    segment_number INTEGER NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    duration DECIMAL(5,2) NOT NULL,
    resolution VARCHAR(10) NOT NULL,
    file_size BIGINT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes
CREATE INDEX idx_stream_session_stream_key ON stream_session(stream_key);
CREATE INDEX idx_stream_session_streamer_id ON stream_session(streamer_id);
CREATE INDEX idx_stream_session_status ON stream_session(status);
CREATE INDEX idx_video_segment_stream_session ON video_segment(stream_session_id);
CREATE INDEX idx_video_segment_resolution ON video_segment(stream_session_id, resolution);

-- Create function to update updated_at timestamp
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Create trigger for stream_session table
CREATE TRIGGER update_stream_session_updated_at 
    BEFORE UPDATE ON stream_session 
    FOR EACH ROW 
    EXECUTE FUNCTION update_updated_at_column();