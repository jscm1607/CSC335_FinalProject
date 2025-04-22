-- Create Server table
CREATE TABLE IF NOT EXISTS Server (
    id INT PRIMARY KEY AUTO_INCREMENT UNIQUE,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
);

 -- Create Food table
 CREATE TABLE IF NOT EXISTS Food (
     id INT PRIMARY KEY AUTO_INCREMENT UNIQUE,
     name VARCHAR(255) NOT NULL,
     category VARCHAR(50),
     cost DOUBLE,
     inStock BOOLEAN
 );
 
-- Create Session table
CREATE TABLE IF NOT EXISTS Session (
    id INT PRIMARY KEY AUTO_INCREMENT UNIQUE,
    date TIMESTAMP,
    serverId INT,
    open BOOLEAN,
    FOREIGN KEY (serverId) REFERENCES Server(id)
);

-- Create Orders table
CREATE TABLE IF NOT EXISTS Orders (
    id INT PRIMARY KEY AUTO_INCREMENT UNIQUE,
    closed BOOLEAN,
    tableNumber INT,
    tip DOUBLE,
    sessionId INT,
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (sessionId) REFERENCES Session(id)
);

-- Create OrderFood table
CREATE TABLE IF NOT EXISTS OrderFood (
    id INT PRIMARY KEY AUTO_INCREMENT UNIQUE,
    seat INT,
    quantity INT,
    foodId INT,
    orderId INT,
    modifications VARCHAR(255),
    FOREIGN KEY (foodId) REFERENCES Food(id),
    FOREIGN KEY (orderId) REFERENCES Orders(id)
);