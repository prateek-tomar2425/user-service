-- Add new columns to user_preferences table for budget and preferences
ALTER TABLE user_preferences
    ADD COLUMN IF NOT EXISTS budget VARCHAR(20);

ALTER TABLE user_preferences
    ADD COLUMN IF NOT EXISTS preferred_activities TEXT;

ALTER TABLE user_preferences
    ADD COLUMN IF NOT EXISTS preferred_destinations TEXT;

-- Create index for better query performance
CREATE INDEX IF NOT EXISTS idx_user_preferences_budget
    ON user_preferences(budget);

