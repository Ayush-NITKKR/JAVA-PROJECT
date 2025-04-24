# Book Management System

## Project Title
Book Management System

## Short Description
A Java-based application that allows users to manage a collection of books, including adding, viewing, updating, deleting, and searching books. It integrates with a MySQL database and provides an interactive graphical user interface (GUI) using Java Swing. The system is designed to help users maintain and manage book records efficiently.

## Team Members
- Ayush Tiwari
- Vishal Sahu
- Rahul Gupta
- Rahul Kumbhkar

## Key Features
- *Add Books*: Users can add new books with details such as book title, author, price, and publisher.
- *View Books*: View the list of all books in the system, including details like ID, title, price, author, and publisher.
- *Search Books*: Search for books based on title, author, and price range.
- *Update Books*: Update the information of existing books.
- *Delete Books*: Remove books from the system.
- *MySQL Integration*: All data is stored in a MySQL database.

## Technologies Used
- *Java*: The core programming language for the application.
- *Swing*: Used for creating the graphical user interface.
- *JDBC*: Used for database connectivity.
- *MySQL*: The database management system used for storing book information.

## Prerequisites
- *JDK*: Java Development Kit (JDK) 8 or later.
- *MySQL*: MySQL Database Server for managing book records.
- *MySQL JDBC Driver*: To connect the Java application with the MySQL database.

## Setup Instructions

1. *Clone the repository*:
   bash
   git clone <repository_url>
   cd <repository_folder>

### 2.Setup The Database
CREATE DATABASE book_management;
CREATE TABLE books (
    bookId INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    price DOUBLE NOT NULL,
    author VARCHAR(255) NOT NULL,
    publisher VARCHAR(255) NOT NULL
);

### 3. Configure the Database
  private static final String DB_URL = "jdbc:mysql://localhost:3306/book_management";
  private static final String DB_USERNAME = "root";
  private static final String DB_PASSWORD = "Crossworld@123";

### 4. Run The Application 
    javac BookFrame.java
    java BookFrame

### 5. Running the Application
  java brm.BookFrame
