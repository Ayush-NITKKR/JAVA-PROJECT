package brm;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

class Book {
    private int bookId;
    private String title;
    private double price;
    private String author;
    private String publisher;

    public Book(int bookId, String title, double price, String author, String publisher) {
        this.bookId = bookId;
        this.title = title;
        this.price = price;
        this.author = author;
        this.publisher = publisher;
    }

    public int getBookId() { return bookId; }
    public String getTitle() { return title; }
    public double getPrice() { return price; }
    public String getAuthor() { return author; }
    public String getPublisher() { return publisher; }
}

public class BookFrame {
    private Connection con;
    private JTextField[] insertFields;
    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<String> searchTypeCombo;
    private JTextField searchField, minPriceField, maxPriceField;

    // UI Styles
    private final Color PRIMARY_COLOR = new Color(48, 63, 159);
    private final Color ACCENT_COLOR = new Color(255, 152, 0);
    private final Color DANGER_COLOR = new Color(244, 67, 54);
    private final Color SUCCESS_COLOR = new Color(76, 175, 80);
    private final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 18);
    private final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    public BookFrame() {
        initializeDatabase();
        createAndShowGUI();
    }

    private void initializeDatabase() {
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "Crossworld@123");
            createBooksTableIfNotExists();
        } catch (SQLException e) {
            showError("Database Error: " + e.getMessage());
        }
    }

    private void createBooksTableIfNotExists() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS books (" +
                "bookid INT PRIMARY KEY, " +
                "title VARCHAR(100), " +
                "price DECIMAL(10,2), " +
                "author VARCHAR(50), " +
                "publisher VARCHAR(50))";
        try (Statement stmt = con.createStatement()) {
            stmt.execute(sql);
        }
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Modern Book Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1100, 750);
        frame.setLocationRelativeTo(null);

        // Main panel with gradient background
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(240, 240, 240);
                Color color2 = new Color(220, 220, 220);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        // Header
        JLabel headerLabel = new JLabel("BOOK MANAGEMENT SYSTEM", SwingConstants.CENTER);
        headerLabel.setFont(HEADER_FONT);
        headerLabel.setForeground(PRIMARY_COLOR);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        // Tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(LABEL_FONT);

        // Add Book Tab
        tabbedPane.addTab("Add Book", createAddBookPanel());

        // View Books Tab
        tabbedPane.addTab("View Books", createViewBooksPanel());

        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private JPanel createAddBookPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        String[] labels = {"Book ID:", "Title:", "Price:", "Author:", "Publisher:"};
        insertFields = new JTextField[labels.length];

        // Form title
        JLabel formTitle = new JLabel("Add New Book");
        formTitle.setFont(HEADER_FONT);
        formTitle.setForeground(PRIMARY_COLOR);
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(formTitle, gbc);

        // Form fields
        for (int i = 0; i < labels.length; i++) {
            JLabel label = new JLabel(labels[i]);
            label.setFont(LABEL_FONT);
            gbc.gridwidth = 1;
            gbc.gridx = 0;
            gbc.gridy = i + 1;
            panel.add(label, gbc);

            insertFields[i] = createStyledTextField();
            gbc.gridx = 1;
            panel.add(insertFields[i], gbc);
        }

        // Save button
        JButton saveButton = createStyledButton("Save Book", ACCENT_COLOR);
        saveButton.addActionListener(e -> saveBook());
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = labels.length + 1;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(saveButton, gbc);

        return panel;
    }

    private JPanel createViewBooksPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Search panel
        JPanel searchPanel = new JPanel();
        searchPanel.setOpaque(false);
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.X_AXIS));

        searchTypeCombo = new JComboBox<>(new String[]{"Title", "Author", "Publisher", "Price Range"});
        searchTypeCombo.setFont(LABEL_FONT);
        searchTypeCombo.addActionListener(e -> togglePriceFields());

        searchField = createStyledTextField();
        searchField.setPreferredSize(new Dimension(200, 30));

        minPriceField = createStyledTextField();
        minPriceField.setPreferredSize(new Dimension(80, 30));
        minPriceField.setVisible(false);

        maxPriceField = createStyledTextField();
        maxPriceField.setPreferredSize(new Dimension(80, 30));
        maxPriceField.setVisible(false);

        JLabel toLabel = new JLabel("to");
        toLabel.setFont(LABEL_FONT);
        toLabel.setVisible(false);

        JButton searchButton = createStyledButton("Search", PRIMARY_COLOR);
        searchButton.addActionListener(e -> searchBooks());

        JButton refreshButton = createStyledButton("Refresh", SUCCESS_COLOR);
        refreshButton.addActionListener(e -> refreshBooks());

        searchPanel.add(new JLabel("Search by:"));
        searchPanel.add(Box.createHorizontalStrut(10));
        searchPanel.add(searchTypeCombo);
        searchPanel.add(Box.createHorizontalStrut(10));
        searchPanel.add(searchField);
        searchPanel.add(Box.createHorizontalStrut(5));
        searchPanel.add(minPriceField);
        searchPanel.add(Box.createHorizontalStrut(5));
        searchPanel.add(toLabel);
        searchPanel.add(Box.createHorizontalStrut(5));
        searchPanel.add(maxPriceField);
        searchPanel.add(Box.createHorizontalStrut(10));
        searchPanel.add(searchButton);
        searchPanel.add(Box.createHorizontalStrut(10));
        searchPanel.add(refreshButton);

        // Action buttons panel
        JPanel actionPanel = new JPanel();
        actionPanel.setOpaque(false);

        JButton updateButton = createStyledButton("Update Book", SUCCESS_COLOR);
        updateButton.addActionListener(e -> updateBook());

        JButton deleteButton = createStyledButton("Delete Book", DANGER_COLOR);
        deleteButton.addActionListener(e -> deleteBook());

        actionPanel.add(updateButton);
        actionPanel.add(Box.createHorizontalStrut(10));
        actionPanel.add(deleteButton);

        // Table setup
        String[] columns = {"Book ID", "Title", "Price", "Author", "Publisher"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };
        table = new JTable(tableModel);
        styleTable();

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(actionPanel, BorderLayout.SOUTH);

        refreshBooks(); // Load initial data

        return panel;
    }

    private void togglePriceFields() {
        boolean showPriceFields = searchTypeCombo.getSelectedItem().equals("Price Range");
        searchField.setVisible(!showPriceFields);
        minPriceField.setVisible(showPriceFields);
        maxPriceField.setVisible(showPriceFields);

        // Update the layout
        Container parent = searchField.getParent();
        parent.revalidate();
        parent.repaint();
    }

    private void styleTable() {
        table.setFont(LABEL_FONT);
        table.setRowHeight(30);
        table.setSelectionBackground(new Color(225, 245, 254));
        table.setSelectionForeground(Color.BLACK);
        table.setGridColor(new Color(240, 240, 240));

        JTableHeader header = table.getTableHeader();
        header.setFont(HEADER_FONT.deriveFont(14f));
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setFont(LABEL_FONT);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        field.setPreferredSize(new Dimension(250, 30));
        return field;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(LABEL_FONT);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor.darker()),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        return button;
    }

    private void saveBook() {
        try {
            int bookId = Integer.parseInt(insertFields[0].getText().trim());
            String title = insertFields[1].getText().trim();
            double price = Double.parseDouble(insertFields[2].getText().trim());
            String author = insertFields[3].getText().trim();
            String publisher = insertFields[4].getText().trim();

            if (title.isEmpty() || author.isEmpty() || publisher.isEmpty()) {
                showError("Please fill all fields");
                return;
            }

            String sql = "INSERT INTO books VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                pstmt.setInt(1, bookId);
                pstmt.setString(2, title);
                pstmt.setDouble(3, price);
                pstmt.setString(4, author);
                pstmt.setString(5, publisher);
                pstmt.executeUpdate();

                showMessage("Book added successfully!", "Success");
                clearInsertFields();
                refreshBooks();
            }
        } catch (NumberFormatException e) {
            showError("Please enter valid numbers for ID and Price");
        } catch (SQLException e) {
            showError("Database Error: " + e.getMessage());
        }
    }
    // Add to BookFrame.java
    private void connectToServer() {
        new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                try (Socket socket = new Socket("localhost", 8080);
                     PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                     BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                    out.println("SEARCH:Title:" + searchField.getText());
                    return in.readLine();
                }
            }
            @Override
            protected void done() {
                try {
                    String response = get();
                    // Process server response
                } catch (Exception e) {
                    showError("Server communication error");
                }
            }
        }.execute();
    }
    private void searchBooks() {
        String searchType = (String)searchTypeCombo.getSelectedItem();
        String searchText = searchField.getText().trim();

        try {
            tableModel.setRowCount(0); // Clear existing data

            if (searchType.equals("Price Range")) {
                double minPrice = minPriceField.getText().isEmpty() ? 0 : Double.parseDouble(minPriceField.getText());
                double maxPrice = maxPriceField.getText().isEmpty() ? Double.MAX_VALUE : Double.parseDouble(maxPriceField.getText());

                String sql = "SELECT * FROM books WHERE price BETWEEN ? AND ?";
                try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                    pstmt.setDouble(1, minPrice);
                    pstmt.setDouble(2, maxPrice);

                    ResultSet rs = pstmt.executeQuery();
                    while (rs.next()) {
                        addRowToTable(rs);
                    }
                }
            } else {
                String sql = "SELECT * FROM books WHERE " + getSearchColumn(searchType) + " LIKE ?";
                try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                    pstmt.setString(1, "%" + searchText + "%");

                    ResultSet rs = pstmt.executeQuery();
                    while (rs.next()) {
                        addRowToTable(rs);
                    }
                }
            }
        } catch (NumberFormatException e) {
            showError("Please enter valid price values");
        } catch (SQLException e) {
            showError("Database Error: " + e.getMessage());
        }
    }

    private String getSearchColumn(String searchType) {
        switch(searchType) {
            case "Title": return "title";
            case "Author": return "author";
            case "Publisher": return "publisher";
            default: return "title";
        }
    }

    private void refreshBooks() {
        try {
            tableModel.setRowCount(0);

            String sql = "SELECT * FROM books";
            try (Statement stmt = con.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {
                    addRowToTable(rs);
                }
            }
        } catch (SQLException e) {
            showError("Error loading books: " + e.getMessage());
        }
    }

    private void addRowToTable(ResultSet rs) throws SQLException {
        Object[] rowData = {
                rs.getInt("bookid"),
                rs.getString("title"),
                rs.getDouble("price"),
                rs.getString("author"),
                rs.getString("publisher")
        };
        tableModel.addRow(rowData);
    }

    private void updateBook() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            showError("Please select a book to update");
            return;
        }

        int bookId = (int)tableModel.getValueAt(selectedRow, 0);
        String currentTitle = (String)tableModel.getValueAt(selectedRow, 1);
        double currentPrice = (double)tableModel.getValueAt(selectedRow, 2);
        String currentAuthor = (String)tableModel.getValueAt(selectedRow, 3);
        String currentPublisher = (String)tableModel.getValueAt(selectedRow, 4);

        // Create edit dialog
        JPanel editPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        editPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField titleField = new JTextField(currentTitle);
        JTextField priceField = new JTextField(String.valueOf(currentPrice));
        JTextField authorField = new JTextField(currentAuthor);
        JTextField publisherField = new JTextField(currentPublisher);

        editPanel.add(new JLabel("Title:"));
        editPanel.add(titleField);
        editPanel.add(new JLabel("Price:"));
        editPanel.add(priceField);
        editPanel.add(new JLabel("Author:"));
        editPanel.add(authorField);
        editPanel.add(new JLabel("Publisher:"));
        editPanel.add(publisherField);

        int result = JOptionPane.showConfirmDialog(null, editPanel, "Edit Book",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String newTitle = titleField.getText().trim();
                double newPrice = Double.parseDouble(priceField.getText().trim());
                String newAuthor = authorField.getText().trim();
                String newPublisher = publisherField.getText().trim();

                String sql = "UPDATE books SET title=?, price=?, author=?, publisher=? WHERE bookid=?";
                try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                    pstmt.setString(1, newTitle);
                    pstmt.setDouble(2, newPrice);
                    pstmt.setString(3, newAuthor);
                    pstmt.setString(4, newPublisher);
                    pstmt.setInt(5, bookId);

                    int rowsAffected = pstmt.executeUpdate();
                    if (rowsAffected > 0) {
                        showMessage("Book updated successfully!", "Success");
                        refreshBooks();
                    } else {
                        showError("Update failed - book not found");
                    }
                }
            } catch (NumberFormatException e) {
                showError("Please enter valid price");
            } catch (SQLException e) {
                showError("Database Error: " + e.getMessage());
            }
        }
    }

    private void deleteBook() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            showError("Please select a book to delete");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(null,
                "Are you sure you want to delete this book?", "Confirm Delete",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            int bookId = (int)tableModel.getValueAt(selectedRow, 0);

            try {
                String sql = "DELETE FROM books WHERE bookid=?";
                try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                    pstmt.setInt(1, bookId);

                    int rowsAffected = pstmt.executeUpdate();
                    if (rowsAffected > 0) {
                        showMessage("Book deleted successfully!", "Success");
                        refreshBooks();
                    } else {
                        showError("Delete failed - book not found");
                    }
                }
            } catch (SQLException e) {
                showError("Database Error: " + e.getMessage());
            }
        }
    }

    private void clearInsertFields() {
        for (JTextField field : insertFields) {
            field.setText("");
        }
    }

    private void showMessage(String message, String title) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                new BookFrame();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}