-- Схема сохранений IN THE MYST (H2, версия 1).
-- Метаданные слота, персонажи и биомы хранятся в таблицах,
-- состояние мира (сетка высот и объекты) -- в версионированном BLOB (save_world).

CREATE TABLE save_slot (
    slot          INT PRIMARY KEY,
    updated_at    TIMESTAMP NOT NULL,
    game_day      INT NOT NULL,
    game_hour     INT NOT NULL,
    game_minute   INT NOT NULL,
    game_second   REAL NOT NULL,
    current_biom  REAL NOT NULL,
    current_km    INT NOT NULL,
    heightmap     BLOB
);

CREATE TABLE save_person (
    slot                 INT NOT NULL,
    ord                  INT NOT NULL,
    person_type          INT NOT NULL,
    experience           INT NOT NULL,
    exp_level            INT NOT NULL,
    total_health         INT NOT NULL,
    health               INT NOT NULL,
    health_charge        INT NOT NULL,
    total_manna          INT NOT NULL,
    manna                INT NOT NULL,
    manna_charge         INT NOT NULL,
    total_stamina        INT NOT NULL,
    stamina              INT NOT NULL,
    stamina_charge       INT NOT NULL,
    initiative           INT NOT NULL,
    initiative_charge    INT NOT NULL,
    total_action_point   INT NOT NULL,
    current_action_point INT NOT NULL,
    physical_power       INT NOT NULL,
    magic_power          INT NOT NULL,
    move_points          INT NOT NULL,
    jump_points          INT NOT NULL,
    speed                INT NOT NULL,
    final_vision         INT NOT NULL,
    vision               INT NOT NULL,
    temp_vision          REAL NOT NULL,
    PRIMARY KEY (slot, ord),
    CONSTRAINT fk_person_slot FOREIGN KEY (slot) REFERENCES save_slot(slot) ON DELETE CASCADE
);

CREATE TABLE save_biom (
    slot         INT NOT NULL,
    biom_index   INT NOT NULL,
    biom_value   REAL NOT NULL,
    PRIMARY KEY (slot, biom_index),
    CONSTRAINT fk_biom_slot FOREIGN KEY (slot) REFERENCES save_slot(slot) ON DELETE CASCADE
);

CREATE TABLE save_world (
    slot        INT PRIMARY KEY,
    payload     BLOB NOT NULL,
    CONSTRAINT fk_world_slot FOREIGN KEY (slot) REFERENCES save_slot(slot) ON DELETE CASCADE
);
