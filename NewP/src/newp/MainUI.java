package brm;
import javax.swing.*;

public class MainUI extends JFrame {
    public void Book() {
        setTitle("Book Management System");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        JLabel label = new JLabel("Welcome to Book Management System!");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        add(label);
    }
}
