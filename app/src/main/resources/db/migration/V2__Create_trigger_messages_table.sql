CREATE OR REPLACE FUNCTION update_processed_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.processed_at = CURRENT_TIMESTAMP;
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_processed_at
    BEFORE UPDATE ON messages
    FOR EACH ROW
    EXECUTE FUNCTION update_processed_at_column();
