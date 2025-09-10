
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE IF NOT EXISTS faction (
                                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                       name TEXT UNIQUE NOT NULL,
                                       description TEXT
);

CREATE TABLE IF NOT EXISTS characters (
                                          id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                          name TEXT NOT NULL,
                                          alias TEXT,
                                          is_human BOOLEAN NOT NULL DEFAULT TRUE,
                                          faction_id UUID REFERENCES faction(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS ship (
                                    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                    name TEXT UNIQUE NOT NULL,
                                    faction_id UUID REFERENCES faction(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS crew_assignment (
                                               id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                               character_id UUID NOT NULL REFERENCES characters(id) ON DELETE CASCADE,
                                               ship_id UUID NOT NULL REFERENCES ship(id) ON DELETE CASCADE,
                                               role TEXT NOT NULL,
                                               CONSTRAINT uq_character_ship UNIQUE (character_id, ship_id)
);
