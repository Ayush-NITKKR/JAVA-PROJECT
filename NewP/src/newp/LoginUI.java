import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class LoginUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginUI() {
        setTitle("Login");
        setSize(300, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(30, 30, 80, 25);
        add(userLabel);

        usernameField = new JTextField();
        usernameField.setBounds(120, 30, 130, 25);
        add(usernameField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(30, 70, 80, 25);
        add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(120, 70, 130, 25);
        add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(100, 110, 80, 25);
        add(loginButton);
        
        JButton signupButton = new JButton("signup");
        signupButton.setBounds(180, 110, 80, 25);
        add(signupButton);

        loginButton.addActionListener(e -> login());
        signupButton.addActionListener(e-> signup());

        setVisible(true);
    }

    private void login() {
        try (Socket socket = new Socket("localhost", 1234)) {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out.println("LOGIN");
            out.println(usernameField.getText());
            out.println(new String(passwordField.getPassword()));

            String response = in.readLine();
            if ("SUCCESS".equals(response)) {
                JOptionPane.showMessageDialog(this, "Login successful!");
                BookFrame x=new BookFrame();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Login failed!");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void signup(){
        dispose();
        new SignupForm();
    }

    public static void main(String[] args) {
        new LoginUI();
    }
}


