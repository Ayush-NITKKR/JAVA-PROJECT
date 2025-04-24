import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.Arrays;

public class SignupForm extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;

    public SignupForm() {
        setTitle("Signup Form");
        setSize(350, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(30, 30, 100, 25);
        add(userLabel);

        usernameField = new JTextField();
        usernameField.setBounds(140, 30, 150, 25);
        add(usernameField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(30, 70, 100, 25);
        add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(140, 70, 150, 25);
        add(passwordField);

        JLabel confirmPassLabel = new JLabel("Confirm Password:");
        confirmPassLabel.setBounds(30, 110, 120, 25);
        add(confirmPassLabel);

        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setBounds(140, 110, 150, 25);
        add(confirmPasswordField);

        JButton signupButton = new JButton("Sign Up");
        signupButton.setBounds(120, 160, 100, 30);
        add(signupButton);

        signupButton.addActionListener(e -> handleSignup());

        setVisible(true);
    }

    private void handleSignup() {
        String username = usernameField.getText().trim();
        char[] password = passwordField.getPassword();
        char[] confirmPassword = confirmPasswordField.getPassword();

        if (username.isEmpty() || password.length == 0 || confirmPassword.length == 0) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }

        if (!Arrays.equals(password, confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.");
            return;
        }

        try (Socket socket = new Socket("localhost", 1234);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println("SIGNUP");
            out.println(username);
            out.println(new String(password));

            String response = in.readLine();
            if ("SUCCESS".equals(response)) {
                JOptionPane.showMessageDialog(this, "Signup successful!");
                LoginUI loginUI = new LoginUI();
                loginUI.setLocationRelativeTo(null); // Center the window
                loginUI.setVisible(true);

    dispose(); // Close signup window
                
            } else {
                JOptionPane.showMessageDialog(this, "Signup failed! Username may already exist.");
            }

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Could not connect to server.");
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SignupForm::new);
    }
}
