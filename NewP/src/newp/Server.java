import java.io.*;
import java.net.*;
import java.sql.*;

public class Server {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(1234)) {
            System.out.println("Server started on port 1234...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            System.out.println("Error starting server: " + e.getMessage());
        }
    }
}

class ClientHandler extends Thread {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        initializeDatabase(); // 👈 Added: Ensure DB & table exist before proceeding
    }

    public void run() {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String action = in.readLine(); // LOGIN or SIGNUP
            String username = in.readLine();
            String password = in.readLine();

            if ("LOGIN".equalsIgnoreCase(action)) {
                if (validateUser(username, password)) {
                    out.println("SUCCESS");
                } else {
                    out.println("FAILURE");
                }
            } else if ("SIGNUP".equalsIgnoreCase(action)) {
                if (createUser(username, password)) {
                    out.println("SUCCESS");
                } else {
                    out.println("FAILURE");
                }
            }

        } catch (IOException e) {
            System.out.println("Client error: " + e.getMessage());
        } finally {
            try { socket.close(); } catch (IOException e) { }
        }
    }

    private void initializeDatabase() {
        String dbName = "library";
        String rootUrl = "jdbc:mysql://localhost:3306/";
        String user = "root";
        String pass = "Crossworld@123";

        try (Connection conn = DriverManager.getConnection(rootUrl, user, pass);
             Statement stmt = conn.createStatement()) {

            // Create database if not exists
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + dbName);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (Connection conn = DriverManager.getConnection(rootUrl + dbName, user, pass);
             Statement stmt = conn.createStatement()) {

            // Create users table if not exists
            String createTable = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "username VARCHAR(50) NOT NULL UNIQUE, " +
                    "password VARCHAR(255) NOT NULL)";
            stmt.executeUpdate(createTable);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean validateUser(String username, String password) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "Crossworld@123")) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?");
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean createUser(String username, String password) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "Crossworld@123")) {
            PreparedStatement check = conn.prepareStatement("SELECT * FROM users WHERE username = ?");
            check.setString(1, username);
            ResultSet rs = check.executeQuery();
            if (rs.next()) return false;

            PreparedStatement ps = conn.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)");
            ps.setString(1, username);
            ps.setString(2, password);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
