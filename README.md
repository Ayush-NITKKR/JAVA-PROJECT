# Library Management System

A modern library management application built using Java, **Swing, **JDBC, and **MySQL. This application allows librarians to manage book inventory, track books, and perform CRUD operations with an intuitive graphical interface.

## Team Members
- Ayush Tiwari
- Vishal Sahu
- Rahul Gupta
- Rahul Kumbhkar

## Features
- Book Management: Add, view, update, and delete books from the library inventory
- Search Functionality: Search books by title, author, publisher, or price range
- User Authentication: Secure login system with username/password validation
- Modern UI: Clean and intuitive graphical user interface with Swing
- Database Integration: All book data is stored in a MySQL database

## Technologies Used
- Java: Core programming language
- Swing: For graphical user interface
- JDBC: Database connectivity
- MySQL: Database management system
- Socket Programming: For client-server communication in authentication

## Prerequisites
1. Java Development Kit (JDBC): Version 8 or higher
2. MySQL Server: Installed and running
3. MySQL Connector/J: JDBC driver for MySQL

## Setup Instructions

### 1. Clone the Repository
bash
git clone https://github.com/your-username/LibraryManagementSystem.git
cd LibraryManagementSystem

### 2. Set Up the Database
CREATE DATABASE library;
CREATE TABLE users (
    username TEXT PRIMARY KEY,
    password TEXT NOT NULL
);

### 3.Configure the Database Connection
con = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "your_password");

###4. Configure the Database Connection
1.	Open BookFrame.java in your code editor
2.	Locate the database connection line:
con = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "your_password");
3.	Replace the following values with your MySQL credentials:
o	library with your database name (if different)
o	root with your MySQL username
o	your_password with your MySQL password
4.	For authentication setup, ensure SQLite database file users.db exists in your project directory

### 5. Compile and Run the Application
javac -cp lib/mysql-connector-j-8.0.23.jar brm/*.java
java -cp ".;lib/mysql-connector-j-8.0.23.jar" Server”
java -cp ".;lib/mysql-connector-j-8.0.23.jar" brm.LoginUI

### 6. Compile The Project
javac -cp ".;lib/mysql-connector-j-8.0.23.jar" brm/*.java

### 7. Running The Application
1.	Change the Directory
cd src/main/java
2.	Run the Server
java -cp ".;lib/mysql-connector-j-8.0.23.jar" Server
3.	Run the Client in another Terminal
java -cp ".;lib/mysql-connector-j-8.0.23.jar" brm.LoginUI
