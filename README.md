# 📚 Book Management System

A modern and visually appealing **Java Swing-based Book Management System** that integrates seamlessly with a MySQL database. This project supports all CRUD operations – Add, View, Search, Update, and Delete book records.

---

## Team Members
- Ayush Tiwari
- Vishal Sahu
- Rahul Gupta
- Rahul Kumbhkar
---

## 🗂️ Project Structure

```
brm/
├── Book2.java         # POJO representing a book
├── BookDao2.java      # Data Access Object for handling all DB operations
├── BookFrame.java     # Java Swing UI to display and manage books
└── Main.java          # Main class to launch the application
```

---

## 🔧 Features

- Add a new book with details like ID, Title, Price, Author, Publisher.
- View all books in a styled JTable.
- Search books by title, author, publisher, or even by price range.
- Update existing book details dynamically from the UI.
- Delete any book record with confirmation.
- Clean and intuitive UI using Java Swing components.
- MySQL integration for real-time database storage and retrieval.

---

## 🖥️ Technologies Used

- Java (JDK 8+)
- Java Swing (GUI)
- MySQL (as backend DB)
- JDBC (for database connection)
- MVC Architecture

---

## 🏗️ Setup Instructions

1. **Clone the Repository**
   ```bash
   git clone https://github.com/your-username/book-management-system.git
   ```

2. **Import into IDE**
   - Open the project in **IntelliJ IDEA** or **Eclipse**.

3. **Setup MySQL Database**
   - Create a database named: `bookdb`
   - Run the following table schema:
     ```sql
     CREATE TABLE books (
       bookId INT PRIMARY KEY,
       title VARCHAR(100),
       price DOUBLE,
       author VARCHAR(100),
       publisher VARCHAR(100)
     );
     ```

4. **Update DB Credentials**
   - Open `BookDao2.java` and set your:
     ```java
     String url = "jdbc:mysql://localhost:3306/bookdb";
     String username = "your_mysql_username";
     String password = "your_mysql_password";
     ```

5. **Run the App**
   - Execute `Main.java` and the UI should appear.

---

## 📸 Screenshots

> _(Add your screenshots here with image markdown)_  
> Example:  
> ![Book Management System UI](screenshots/main_ui.png)

---

## 📑 Class Descriptions

### ✅ `Book2.java`
Defines the book entity with attributes and getters/setters:
- `int bookId`
- `String title`
- `double price`
- `String author`
- `String publisher`

### ✅ `BookDao2.java`
Handles all database transactions:
- Insert, Update, Delete
- Search by title/author/publisher/price
- View all books

### ✅ `BookFrame.java`
Builds the GUI using Swing:
- Form panel to add/update book
- JTable to list all records
- Search bar for filtering
- Buttons for actions (Add, Update, Delete, Clear)

### ✅ `Main.java`
Launches the main UI window using:
```java
public class Main {
    public static void main(String[] args) {
        new BookFrame();
    }
}

## 📌 Future Improvements
- Add export to PDF/Excel
- Add login functionality
- Implement pagination for large datasets
- Use Prepared Statements to prevent SQL injection

---

## 🙌 Author

- **Rahul** – MCA Student  
  _GitHub: [your-username](https://github.com/your-username)_

---

## 📃 License

This project is open-source and free to use.

---
