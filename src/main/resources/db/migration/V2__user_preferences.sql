CREATE TABLE user_preferences (
                                  user_id UUID PRIMARY KEY,
                                  travel_style VARCHAR(100),
                                  exploration_style VARCHAR(100),
                                  food_preference VARCHAR(100),
                                  travel_scope VARCHAR(100),
                                  updated_at TIMESTAMP,
                                  CONSTRAINT fk_user
                                      FOREIGN KEY(user_id)
                                          REFERENCES users(id)
                                          ON DELETE CASCADE
);
