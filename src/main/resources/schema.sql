-- Create Server table
CREATE TABLE IF NOT EXISTS Server (
    id INT PRIMARY KEY AUTO_INCREMENT UNIQUE,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
);

 -- Create Food table
 CREATE TABLE IF NOT EXISTS Food (
     id INT PRIMARY KEY AUTO_INCREMENT,
     name VARCHAR(255) NOT NULL,
     category VARCHAR(50),
     cost DOUBLE,
     inStock BOOLEAN,
     numOrders INT
 );
 
-- -- Create Order table
-- CREATE TABLE IF NOT EXISTS Orders (
--     id INT PRIMARY KEY AUTO_INCREMENT,
--     closed BOOLEAN,
--     tableNumber INT,
--     tip DOUBLE
-- );
-- 
-- -- Create Session table
 CREATE TABLE IF NOT EXISTS Session (
     id INT PRIMARY KEY AUTO_INCREMENT UNIQUE,
     date TIMESTAMP,
     serverId INT,
     totalTips DOUBLE,
     open BOOLEAN,
     FOREIGN KEY (serverId) REFERENCES Server(id)
 );
-- 
-- -- Create OrderFood table
-- CREATE TABLE IF NOT EXISTS OrderFood (
--     id INT PRIMARY KEY AUTO_INCREMENT,
--     seat INT,
--     quantity INT,
--     foodId INT,
--     orderId INT,
--     modifications VARCHAR(255),
--     FOREIGN KEY (foodId) REFERENCES Food(id),
--     FOREIGN KEY (orderId) REFERENCES Orders(id)
-- );