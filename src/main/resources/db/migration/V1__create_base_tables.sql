-- =========================================================
-- V1__create_base_tables.sql
-- Base schema for Dota 2 analytics project
-- PostgreSQL + Flyway
-- =========================================================


-- =========================================================
-- 1. Reference tables
-- =========================================================

CREATE TABLE tournaments (
                             league_id BIGINT PRIMARY KEY,
                             name VARCHAR(255) NOT NULL,
                             tier VARCHAR(50),

                             start_date TIMESTAMP,
                             end_date TIMESTAMP
);


CREATE TABLE patches (
                         patch_id INT PRIMARY KEY,
                         patch_name VARCHAR(50) NOT NULL,
                         release_date TIMESTAMP
);


CREATE TABLE teams (
                       team_id BIGINT PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       tag VARCHAR(50),

                       region VARCHAR(100),
                       logo_url TEXT
);


CREATE TABLE players (
                         account_id BIGINT PRIMARY KEY,
                         nickname VARCHAR(255),

                         country VARCHAR(100)
);


CREATE TABLE heroes (
                        hero_id INT PRIMARY KEY,

                        name VARCHAR(150),
                        localized_name VARCHAR(100) NOT NULL,

                        primary_attr VARCHAR(50),
                        attack_type VARCHAR(50)
);


-- =========================================================
-- 2. Main match table
-- =========================================================

CREATE TABLE matches (
                         match_id BIGINT PRIMARY KEY,

                         league_id BIGINT,
                         patch_id INT,

                         radiant_team_id BIGINT,
                         dire_team_id BIGINT,

                         radiant_win BOOLEAN NOT NULL,

                         duration INT NOT NULL,

                         start_time TIMESTAMP,

                         radiant_score INT,
                         dire_score INT,

                         first_blood_time INT,

                         cluster INT,
                         game_mode INT,
                         lobby_type INT,

                         match_seq_num BIGINT,

                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                         CONSTRAINT fk_matches_tournament
                             FOREIGN KEY (league_id)
                                 REFERENCES tournaments (league_id),

                         CONSTRAINT fk_matches_patch
                             FOREIGN KEY (patch_id)
                                 REFERENCES patches (patch_id),

                         CONSTRAINT fk_matches_radiant_team
                             FOREIGN KEY (radiant_team_id)
                                 REFERENCES teams (team_id),

                         CONSTRAINT fk_matches_dire_team
                             FOREIGN KEY (dire_team_id)
                                 REFERENCES teams (team_id),

                         CONSTRAINT chk_matches_duration_non_negative
                             CHECK (duration >= 0),

                         CONSTRAINT chk_matches_scores_non_negative
                             CHECK (
                                 (radiant_score IS NULL OR radiant_score >= 0)
                                     AND
                                 (dire_score IS NULL OR dire_score >= 0)
                                 )
);


-- =========================================================
-- 3. Raw OpenDota JSON archive
-- =========================================================

CREATE TABLE raw_matches (
                             match_id BIGINT PRIMARY KEY,

                             json_data JSONB NOT NULL,

                             downloaded_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             parsed_at TIMESTAMP,

                             parse_status VARCHAR(30),
                             parse_error TEXT,

                             CONSTRAINT chk_raw_matches_parse_status
                                 CHECK (
                                     parse_status IS NULL
                                         OR parse_status IN ('NEW', 'PARSED', 'ERROR')
                                     )
);


-- =========================================================
-- 4. Player statistics per match
-- =========================================================

CREATE TABLE player_match_stats (
                                    id BIGSERIAL PRIMARY KEY,

                                    match_id BIGINT NOT NULL,
                                    account_id BIGINT NOT NULL,
                                    hero_id INT NOT NULL,
                                    team_id BIGINT,

                                    player_slot INT,
                                    is_radiant BOOLEAN,

                                    kills INT,
                                    deaths INT,
                                    assists INT,

                                    gpm INT,
                                    xpm INT,

                                    net_worth INT,

                                    hero_damage INT,
                                    tower_damage INT,
                                    hero_healing INT,

                                    last_hits INT,
                                    denies INT,

                                    level INT,

                                    obs_placed INT,
                                    sen_placed INT,

                                    CONSTRAINT fk_pms_match
                                        FOREIGN KEY (match_id)
                                            REFERENCES matches (match_id)
                                            ON DELETE CASCADE,

                                    CONSTRAINT fk_pms_player
                                        FOREIGN KEY (account_id)
                                            REFERENCES players (account_id),

                                    CONSTRAINT fk_pms_hero
                                        FOREIGN KEY (hero_id)
                                            REFERENCES heroes (hero_id),

                                    CONSTRAINT fk_pms_team
                                        FOREIGN KEY (team_id)
                                            REFERENCES teams (team_id),

                                    CONSTRAINT uk_player_match_stats_match_account
                                        UNIQUE (match_id, account_id),

                                    CONSTRAINT chk_pms_kda_non_negative
                                        CHECK (
                                            (kills IS NULL OR kills >= 0)
                                                AND
                                            (deaths IS NULL OR deaths >= 0)
                                                AND
                                            (assists IS NULL OR assists >= 0)
                                            ),

                                    CONSTRAINT chk_pms_economy_non_negative
                                        CHECK (
                                            (gpm IS NULL OR gpm >= 0)
                                                AND
                                            (xpm IS NULL OR xpm >= 0)
                                                AND
                                            (net_worth IS NULL OR net_worth >= 0)
                                            )
);


-- =========================================================
-- 5. Picks and bans
-- =========================================================

CREATE TABLE picks_bans (
                            id BIGSERIAL PRIMARY KEY,

                            match_id BIGINT NOT NULL,

                            hero_id INT NOT NULL,

                            team_id BIGINT,
                            team_side VARCHAR(20),

                            is_pick BOOLEAN NOT NULL,

                            order_num INT NOT NULL,

                            CONSTRAINT fk_picks_bans_match
                                FOREIGN KEY (match_id)
                                    REFERENCES matches (match_id)
                                    ON DELETE CASCADE,

                            CONSTRAINT fk_picks_bans_hero
                                FOREIGN KEY (hero_id)
                                    REFERENCES heroes (hero_id),

                            CONSTRAINT fk_picks_bans_team
                                FOREIGN KEY (team_id)
                                    REFERENCES teams (team_id),

                            CONSTRAINT uk_picks_bans_match_order
                                UNIQUE (match_id, order_num),

                            CONSTRAINT chk_picks_bans_team_side
                                CHECK (
                                    team_side IS NULL
                                        OR team_side IN ('RADIANT', 'DIRE', 'UNKNOWN')
                                    ),

                            CONSTRAINT chk_picks_bans_order_non_negative
                                CHECK (order_num >= 0)
);


-- =========================================================
-- 6. Gold / XP advantage timeline
-- =========================================================

CREATE TABLE match_advantage_timeline (
                                          id BIGSERIAL PRIMARY KEY,

                                          match_id BIGINT NOT NULL,

                                          minute INT NOT NULL,

                                          radiant_gold_adv INT,
                                          radiant_xp_adv INT,

                                          CONSTRAINT fk_match_advantage_timeline_match
                                              FOREIGN KEY (match_id)
                                                  REFERENCES matches (match_id)
                                                  ON DELETE CASCADE,

                                          CONSTRAINT uk_match_advantage_timeline_match_minute
                                              UNIQUE (match_id, minute),

                                          CONSTRAINT chk_match_advantage_timeline_minute_non_negative
                                              CHECK (minute >= 0)
    );


-- =========================================================
-- 7. Team roster history
-- =========================================================

CREATE TABLE team_roster (
                             id BIGSERIAL PRIMARY KEY,

                             team_id BIGINT NOT NULL,
                             account_id BIGINT NOT NULL,

                             joined_at TIMESTAMP,
                             left_at TIMESTAMP,

                             CONSTRAINT fk_team_roster_team
                                 FOREIGN KEY (team_id)
                                     REFERENCES teams (team_id),

                             CONSTRAINT fk_team_roster_player
                                 FOREIGN KEY (account_id)
                                     REFERENCES players (account_id),

                             CONSTRAINT uk_team_roster_team_player_joined
                                 UNIQUE (team_id, account_id, joined_at),

                             CONSTRAINT chk_team_roster_dates
                                 CHECK (
                                     left_at IS NULL
                                         OR joined_at IS NULL
                                         OR left_at >= joined_at
                                     )
);


-- =========================================================
-- 8. Indexes for analytics / queries
-- =========================================================

-- matches
CREATE INDEX idx_matches_league_id
    ON matches (league_id);

CREATE INDEX idx_matches_patch_id
    ON matches (patch_id);

CREATE INDEX idx_matches_radiant_team_id
    ON matches (radiant_team_id);

CREATE INDEX idx_matches_dire_team_id
    ON matches (dire_team_id);

CREATE INDEX idx_matches_start_time
    ON matches (start_time);

CREATE INDEX idx_matches_match_seq_num
    ON matches (match_seq_num);


-- raw_matches
CREATE INDEX idx_raw_matches_parse_status
    ON raw_matches (parse_status);

CREATE INDEX idx_raw_matches_downloaded_at
    ON raw_matches (downloaded_at);


-- player_match_stats
CREATE INDEX idx_pms_match_id
    ON player_match_stats (match_id);

CREATE INDEX idx_pms_account_id
    ON player_match_stats (account_id);

CREATE INDEX idx_pms_hero_id
    ON player_match_stats (hero_id);

CREATE INDEX idx_pms_team_id
    ON player_match_stats (team_id);


-- picks_bans
CREATE INDEX idx_picks_bans_match_id
    ON picks_bans (match_id);

CREATE INDEX idx_picks_bans_hero_id
    ON picks_bans (hero_id);

CREATE INDEX idx_picks_bans_team_id
    ON picks_bans (team_id);

CREATE INDEX idx_picks_bans_is_pick
    ON picks_bans (is_pick);


-- match_advantage_timeline
CREATE INDEX idx_match_advantage_timeline_match_id
    ON match_advantage_timeline (match_id);


-- team_roster
CREATE INDEX idx_team_roster_team_id
    ON team_roster (team_id);

CREATE INDEX idx_team_roster_account_id
    ON team_roster (account_id);

CREATE INDEX idx_team_roster_joined_at
    ON team_roster (joined_at);