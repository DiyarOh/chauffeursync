CREATE TABLE IF NOT EXISTS Role (
    id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    title VARCHAR(100) UNIQUE
    );

CREATE TABLE IF NOT EXISTS User (
    id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    name VARCHAR(100),
    email VARCHAR(255) UNIQUE,
    password VARCHAR(255),
    role_id CHAR(36),
    FOREIGN KEY (role_id) REFERENCES Role(id) ON DELETE SET NULL
    );

CREATE TABLE IF NOT EXISTS Vehicle (
    id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    license_plate VARCHAR(20) UNIQUE,
    type VARCHAR(100),
    status VARCHAR(50),
    current_kilometer INT
    );

CREATE TABLE IF NOT EXISTS Garage (
    id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    title VARCHAR(100),
    address VARCHAR(255)
    );

CREATE TABLE IF NOT EXISTS Shift (
    id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    user_id CHAR(36),
    vehicle_id CHAR(36),
    start_time DATETIME,
    end_time DATETIME,
    start_km INT,
    end_km INT,
    FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE SET NULL,
    FOREIGN KEY (vehicle_id) REFERENCES Vehicle(id) ON DELETE SET NULL
    );

CREATE TABLE IF NOT EXISTS KilometerEntry (
    id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    shift_id CHAR(36),
    vehicle_id CHAR(36),
    km INT,
    timestamp DATETIME,
    confirmed_by_driver BOOLEAN,
    confirmed_by_admin BOOLEAN,
    FOREIGN KEY (shift_id) REFERENCES Shift(id) ON DELETE SET NULL,
    FOREIGN KEY (vehicle_id) REFERENCES Vehicle(id) ON DELETE SET NULL
    );

CREATE TABLE IF NOT EXISTS DamageReport (
    id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    vehicle_id CHAR(36),
    user_id CHAR(36),
    description TEXT,
    photo_url VARCHAR(2083),
    status VARCHAR(50),
    priority VARCHAR(50),
    created_at DATETIME,
    FOREIGN KEY (vehicle_id) REFERENCES Vehicle(id) ON DELETE SET NULL,
    FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE SET NULL
    );

CREATE TABLE IF NOT EXISTS MaintenanceTask (
    id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    vehicle_id CHAR(36),
    garage_id CHAR(36),
    scheduled_date DATE,
    status VARCHAR(50),
    action_taken TEXT,
    updated_at DATETIME,
    FOREIGN KEY (vehicle_id) REFERENCES Vehicle(id) ON DELETE SET NULL,
    FOREIGN KEY (garage_id) REFERENCES Garage(id) ON DELETE SET NULL
    );

CREATE TABLE IF NOT EXISTS DriverReport (
    id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    user_id CHAR(36),
    period VARCHAR(30),
    total_hours FLOAT,
    generated_at DATETIME,
    FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE SET NULL
    );

CREATE TABLE IF NOT EXISTS VehicleReport (
    id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    vehicle_id CHAR(36),
    period VARCHAR(30),
    total_kilometers INT,
    generated_at DATETIME,
    FOREIGN KEY (vehicle_id) REFERENCES Vehicle(id) ON DELETE SET NULL
    );


INSERT INTO Role (id, title) VALUES
                                 ('cd524dea-fd97-4d70-ad41-1036cb8e1d52', 'Chauffeur'),
                                 ('1a4a9256-4c08-4cab-93d3-be95bdfecb20', 'Boekhouder'),
                                 ('ed8a9c6e-fbab-4419-aa37-27d5743b2aac', 'Administrator');
INSERT INTO Garage (id, title, address) VALUES
                                            ('dba7f639-b70f-4d6a-ba22-257667d209df', 'Garage Centrum', 'Straatweg 1, Den Haag'),
                                            ('f8a4eb2a-735c-4676-afd4-98685f682b60', 'Garage Zuid', 'Kade 22, Rotterdam');
INSERT INTO Vehicle (id, license_plate, type, status, current_kilometer) VALUES
                                                                             ('f315761c-79be-4913-8c4f-37d7580aa869', 'XX-01-ZZ', 'Type 1', 'actief', 10500),
                                                                             ('7a4bf107-15ed-4d36-9800-cf2a115883ff', 'XX-02-ZZ', 'Type 2', 'actief', 11000),
                                                                             ('a0279f8f-0f53-485f-90d7-a41563733b7e', 'XX-03-ZZ', 'Type 3', 'actief', 11500),
                                                                             ('683d6356-cbdf-400a-ac36-b7660e481d45', 'XX-04-ZZ', 'Type 4', 'actief', 12000);
INSERT INTO User (id, name, email, password, role_id) VALUES
                                                          ('1cb9a6e7-e5ad-45df-9604-393b23535169', 'Gebruiker 1', 'user1@mail.com', 'hashed_pw_1', 'ed8a9c6e-fbab-4419-aa37-27d5743b2aac'),
                                                          ('fdce1c60-151d-4d18-968b-6095da597bb5', 'Gebruiker 2', 'user2@mail.com', 'hashed_pw_2', '1a4a9256-4c08-4cab-93d3-be95bdfecb20'),
                                                          ('de9bab39-a829-40fd-8d57-c8eef2e20b98', 'Gebruiker 3', 'user3@mail.com', 'hashed_pw_3', '1a4a9256-4c08-4cab-93d3-be95bdfecb20'),
                                                          ('58e202fb-e413-42a7-a270-c10bff5ca6d6', 'Gebruiker 4', 'user4@mail.com', 'hashed_pw_4', 'cd524dea-fd97-4d70-ad41-1036cb8e1d52'),
                                                          ('9d707958-d4ec-461b-8b6e-194b7f194999', 'Gebruiker 5', 'user5@mail.com', 'hashed_pw_5', 'cd524dea-fd97-4d70-ad41-1036cb8e1d52'),
                                                          ('73eafd00-37ed-4769-b035-aff0c5d7083b', 'Gebruiker 6', 'user6@mail.com', 'hashed_pw_6', '1a4a9256-4c08-4cab-93d3-be95bdfecb20'),
                                                          ('e08d5c25-005a-4cc5-af29-e03741b5a1f8', 'Gebruiker 7', 'user7@mail.com', 'hashed_pw_7', 'ed8a9c6e-fbab-4419-aa37-27d5743b2aac'),
                                                          ('ab7dc4b3-ac68-439e-a36c-2e6ce2369a2c', 'Gebruiker 8', 'user8@mail.com', 'hashed_pw_8', '1a4a9256-4c08-4cab-93d3-be95bdfecb20'),
                                                          ('d6c4551a-9abd-45bf-a4e9-0b7a536d1a41', 'Gebruiker 9', 'user9@mail.com', 'hashed_pw_9', '1a4a9256-4c08-4cab-93d3-be95bdfecb20'),
                                                          ('3b713c09-c643-4430-84bb-5b3055142e9d', 'Gebruiker 10', 'user10@mail.com', 'hashed_pw_10', '1a4a9256-4c08-4cab-93d3-be95bdfecb20');
INSERT INTO Shift (id, user_id, vehicle_id, start_time, end_time, start_km, end_km) VALUES
                                                                                        ('0989bd05-f9eb-4a68-8e62-49a73a33b298', 'ab7dc4b3-ac68-439e-a36c-2e6ce2369a2c', 'a0279f8f-0f53-485f-90d7-a41563733b7e', '2025-04-08 09:12:15', '2025-04-08 17:12:15', 10000, 10080),
                                                                                        ('572d01cb-a2e4-41c4-ad36-8bee27d6b4b3', 'ab7dc4b3-ac68-439e-a36c-2e6ce2369a2c', 'a0279f8f-0f53-485f-90d7-a41563733b7e', '2025-04-07 09:12:15', '2025-04-07 17:12:15', 10010, 10090),
                                                                                        ('01b8db0a-b551-4e0b-9741-681c580e5f6e', '1cb9a6e7-e5ad-45df-9604-393b23535169', 'a0279f8f-0f53-485f-90d7-a41563733b7e', '2025-04-06 09:12:15', '2025-04-06 17:12:15', 10020, 10100),
                                                                                        ('6097b535-fea7-4056-9a17-0bb6218fc5c5', 'fdce1c60-151d-4d18-968b-6095da597bb5', '683d6356-cbdf-400a-ac36-b7660e481d45', '2025-04-05 09:12:15', '2025-04-05 17:12:15', 10030, 10110),
                                                                                        ('b12fcd8a-17ef-4fd4-a735-1d8c8fd80315', 'ab7dc4b3-ac68-439e-a36c-2e6ce2369a2c', '683d6356-cbdf-400a-ac36-b7660e481d45', '2025-04-04 09:12:15', '2025-04-04 17:12:15', 10040, 10120),
                                                                                        ('c3970e53-3310-4766-991c-f64856d14105', 'de9bab39-a829-40fd-8d57-c8eef2e20b98', 'f315761c-79be-4913-8c4f-37d7580aa869', '2025-04-03 09:12:15', '2025-04-03 17:12:15', 10050, 10130);
INSERT INTO DamageReport (id, vehicle_id, user_id, description, photo_url, status, priority, created_at) VALUES
                                                                                                             ('106854b4-808b-4f00-b949-12a3c88f5d76', '7a4bf107-15ed-4d36-9800-cf2a115883ff', '1cb9a6e7-e5ad-45df-9604-393b23535169', 'Schadebeschrijving 0', 'http://foto0.jpg', 'Open', 'Hoog', '2025-04-08 09:12:15'),
                                                                                                             ('45f1cc2c-aa6a-4ee8-8159-6d2d4b577acc', '683d6356-cbdf-400a-ac36-b7660e481d45', '3b713c09-c643-4430-84bb-5b3055142e9d', 'Schadebeschrijving 1', 'http://foto1.jpg', 'Open', 'Hoog', '2025-04-08 09:12:15'),
                                                                                                             ('971173e6-e519-4999-9d4e-c2971441bcb3', '683d6356-cbdf-400a-ac36-b7660e481d45', '1cb9a6e7-e5ad-45df-9604-393b23535169', 'Schadebeschrijving 2', 'http://foto2.jpg', 'Open', 'Hoog', '2025-04-08 09:12:15'),
                                                                                                             ('f72dd72c-6f77-40f2-bde6-c703de7af337', '7a4bf107-15ed-4d36-9800-cf2a115883ff', 'fdce1c60-151d-4d18-968b-6095da597bb5', 'Schadebeschrijving 3', 'http://foto3.jpg', 'Open', 'Hoog', '2025-04-08 09:12:15');
INSERT INTO MaintenanceTask (id, vehicle_id, garage_id, scheduled_date, status, action_taken, updated_at) VALUES
                                                                                                              ('d72a0658-a16b-4cdc-8cbc-d8c4c31cd764', 'a0279f8f-0f53-485f-90d7-a41563733b7e', 'dba7f639-b70f-4d6a-ba22-257667d209df', '2025-04-13', 'Gepland', 'Inspectie', '2025-04-08 09:12:15'),
                                                                                                              ('5842da38-ac57-4d2a-9f31-68d5553da41d', 'a0279f8f-0f53-485f-90d7-a41563733b7e', 'f8a4eb2a-735c-4676-afd4-98685f682b60', '2025-04-13', 'Gepland', 'Inspectie', '2025-04-08 09:12:15'),
                                                                                                              ('ad0e0dff-6774-44c9-b967-d726b1399cfc', '683d6356-cbdf-400a-ac36-b7660e481d45', 'dba7f639-b70f-4d6a-ba22-257667d209df', '2025-04-13', 'Gepland', 'Inspectie', '2025-04-08 09:12:15');
INSERT INTO KilometerEntry (id, shift_id, vehicle_id, km, timestamp, confirmed_by_driver, confirmed_by_admin) VALUES
    ('197a5b2c-2f58-4ea5-917a-4775b7e557d4', '0989bd05-f9eb-4a68-8e62-49a73a33b298', 'a0279f8f-0f53-485f-90d7-a41563733b7e', 10500, '2025-04-07 09:12:15', True, 'None');
