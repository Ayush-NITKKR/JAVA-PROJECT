# 📚 Book Management System

A modern and user-friendly **Java Swing-based application** for managing book records, including **Add**, **View**, **Search (including by price range)**, **Update**, and **Delete** functionalities, integrated with a **MySQL database**. This project is ideal for library inventory control, small bookstores, and academic demonstration purposes.

---

## 🚀 Features

- ✅ Add new book records with full details.
- 📄 View all books in a dynamic table.
- 🔍 Search books by:
  - Title
  - Author
  - Publisher
  - Price Range
- ✏️ Update existing book entries.
- ❌ Delete book records securely.
- 💾 MySQL integration for persistent storage.
- 🎨 Styled Java Swing GUI for better usability.

---

## 🛠️ Technologies Used

- **Java Swing** (GUI)
- **MySQL** (Backend Database)
- **JDBC** (Java Database Connectivity)
- **NetBeans / IntelliJ / Eclipse** (any Java IDE)
- **MVC Design Pattern** (loosely followed)

---

## 🗃️ Project Structure

```plaintext
brm/
├── Book2.java       # POJO class representing a Book entity
├── BookFrame.java   # GUI class containing all UI components and logic
└── Main.java        # Entry point of the application
```

---

## ⚙️ How to Run

1. **Clone the repository**:
   ```bash
   git clone https://github.com/your-username/book-management-system.git
   cd book-management-system
   ```

2. **Setup the MySQL Database**:

   ```sql
   CREATE DATABASE bookdb;

   USE bookdb;

   CREATE TABLE books (
       bookId INT PRIMARY KEY,
       title VARCHAR(255),
       price DOUBLE,
       author VARCHAR(255),
       publisher VARCHAR(255)
   );
   ```

3. **Configure the JDBC connection** inside `BookFrame.java`:
   ```java
   String url = "jdbc:mysql://localhost:3306/bookdb";
   String user = "root";
   String password = "yourpassword";
   ```

4. **Compile and Run**:
   - Use any Java IDE (like IntelliJ, NetBeans) to compile and run `Main.java`.
   - Or, use the terminal:
     ```bash
     javac brm/*.java
     java brm.Main
     ```

---

## 📸 Screenshots

> *(Add screenshots of your GUI application here once available — table view, add book form, etc.)*

---

## 📌 Future Enhancements

- 📦 Export book data to PDF/Excel.
- 🔐 Add user authentication.
- 🌐 Migrate to JavaFX or a web-based UI.
- 📱 Build an Android version using Java/Kotlin.

---

## 🙌 Author

**Rahul** – MCA Student and Java Developer  
🔗 [LinkedIn](#) • [GitHub](#)

---

## 📄 License

This project is open-source and available under the [MIT License](LICENSE).
