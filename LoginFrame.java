import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginFrame extends JFrame {

    public LoginFrame() {

        // Frame settings
        setTitle("Employee Payroll - Login");
        setSize(420, 360);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(245, 245, 245));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // Heading
        JLabel title = new JLabel("EMPLOYEE PAYROLL");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setForeground(new Color(40, 40, 40));

        // Subtitle
        JLabel sub = new JLabel("Sign in to continue");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);
        sub.setForeground(new Color(100, 100, 100));

        // Username label & field
        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JTextField userField = new JTextField();
        userField.setPreferredSize(new Dimension(200, 38));
        userField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        userField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(190, 190, 190)),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)));

        // Password label & field
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JPasswordField passField = new JPasswordField();
        passField.setPreferredSize(new Dimension(200, 38));
        passField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        passField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(190, 190, 190)),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)));

        // Login button
        JButton loginBtn = new JButton("Login");
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setBackground(new Color(72, 133, 237));
        loginBtn.setFocusPainted(false);
        loginBtn.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Hover effect
        loginBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                loginBtn.setBackground(new Color(52, 113, 207));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                loginBtn.setBackground(new Color(72, 133, 237));
            }
        });

        // Action event
        loginBtn.addActionListener(e -> {
            String user = userField.getText();
            String pass = new String(passField.getPassword());

            if (user.equals("admin") && pass.equals("admin")) {
                new PayrollApp().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Username or Password!");
            }
        });

        // Small footer
        JLabel footer = new JLabel("Use admin / admin to login");
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footer.setForeground(new Color(120, 120, 120));
        footer.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Build layout
        mainPanel.add(title);
        mainPanel.add(Box.createVerticalStrut(6));
        mainPanel.add(sub);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(userLabel);
        mainPanel.add(userField);
        mainPanel.add(Box.createVerticalStrut(12));
        mainPanel.add(passLabel);
        mainPanel.add(passField);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(loginBtn);
        mainPanel.add(Box.createVerticalStrut(18));
        mainPanel.add(footer);

        add(mainPanel);
    }

    public static void main(String[] args) {
        // Allow quick testing
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
