CREATE TABLE match_events (
                              id BIGSERIAL PRIMARY KEY,

                              match_id BIGINT NOT NULL,

                              event_time_seconds INT,
                              event_time_min_sec VARCHAR(20),

                              event_type VARCHAR(100) NOT NULL,

                              team_side VARCHAR(20),
                              team_id BIGINT,

                              account_id BIGINT,
                              hero_id INT,
                              player_slot INT,

                              description TEXT,

                              raw_event JSONB,

                              created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                              CONSTRAINT fk_match_events_match
                                  FOREIGN KEY (match_id)
                                      REFERENCES matches (match_id)
                                      ON DELETE CASCADE,

                              CONSTRAINT fk_match_events_team
                                  FOREIGN KEY (team_id)
                                      REFERENCES teams (team_id),

                              CONSTRAINT fk_match_events_player
                                  FOREIGN KEY (account_id)
                                      REFERENCES players (account_id),

                              CONSTRAINT fk_match_events_hero
                                  FOREIGN KEY (hero_id)
                                      REFERENCES heroes (hero_id),

                              CONSTRAINT chk_match_events_team_side
                                  CHECK (
                                      team_side IS NULL
                                          OR team_side IN ('RADIANT', 'DIRE', 'UNKNOWN')
                                      )
);

CREATE INDEX idx_match_events_match_id
    ON match_events (match_id);

CREATE INDEX idx_match_events_event_type
    ON match_events (event_type);

CREATE INDEX idx_match_events_team_id
    ON match_events (team_id);

CREATE INDEX idx_match_events_account_id
    ON match_events (account_id);

CREATE INDEX idx_match_events_hero_id
    ON match_events (hero_id);