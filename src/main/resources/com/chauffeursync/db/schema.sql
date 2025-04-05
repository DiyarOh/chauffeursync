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

INSERT INTO Role (id, title)
SELECT UUID(), 'Chauffeur'
    WHERE NOT EXISTS (SELECT 1 FROM Role WHERE title = 'Chauffeur');

INSERT INTO Role (id, title)
SELECT UUID(), 'Boekhouder'
    WHERE NOT EXISTS (SELECT 1 FROM Role WHERE title = 'Boekhouder');

INSERT INTO Role (id, title)
SELECT UUID(), 'Administrator'
    WHERE NOT EXISTS (SELECT 1 FROM Role WHERE title = 'Administrator');