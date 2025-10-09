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


-- Create indexes
CREATE INDEX idx_stream_session_stream_key ON stream_session(stream_key);
CREATE INDEX idx_stream_session_streamer_id ON stream_session(streamer_id);
CREATE INDEX idx_stream_session_status ON stream_session(status);

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