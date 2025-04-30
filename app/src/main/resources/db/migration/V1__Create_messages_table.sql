CREATE TABLE IF NOT EXISTS messages (
    id UUID PRIMARY KEY,
    content TEXT NOT NULL,
    status VARCHAR(20) NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    processedAt TIMESTAMP
    );

CREATE INDEX IF NOT EXISTS idx_messages_status ON messages(status);
