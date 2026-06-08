ALTER TABLE players
    ADD COLUMN pro_nickname VARCHAR(255),
    ADD COLUMN pro_team_id BIGINT,
    ADD COLUMN pro_team_name VARCHAR(255),
    ADD COLUMN pro_team_tag VARCHAR(100),
    ADD COLUMN is_pro BOOLEAN DEFAULT FALSE;