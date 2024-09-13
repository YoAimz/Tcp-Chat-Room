package Client;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Prompt {
    private JFrame frame;
    private JTextField nameTextField;
    private JTextField ipTextField;
    private JTextField portTextField;
    private JLabel nameLabel;
    private JLabel ipLabel;
    private JLabel portLabel;
    private JButton connectButton;
    private JButton exitButton;
    private ImageIcon frameIcon;

    private String promptName;
    private String promptIp;
    private int promptPort;

    public Prompt() {
        frame = new JFrame("Connect to server...");
        frameIcon = new ImageIcon("..\\Chatters\\images\\serverIcon.png");
        frame.setIconImage(frameIcon.getImage());
        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(63, 63, 63));

        nameLabel = new JLabel("Name: ");
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setBounds(85, 100, 75, 25);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 15));

        nameTextField = new JTextField(10);
        nameTextField.setForeground(Color.BLACK);
        nameTextField.setBounds(135, 100, 150, 25);
        nameTextField.setCaretColor(Color.BLACK);
        nameTextField.setBorder(null);

        ipLabel = new JLabel("IP: ");
        ipLabel.setForeground(Color.WHITE);
        ipLabel.setBounds(85, 150, 75, 25);
        ipLabel.setFont(new Font("Arial", Font.BOLD, 15));

        ipTextField = new JTextField(10);
        ipTextField.setForeground(Color.BLACK);
        ipTextField.setBounds(135, 150, 150, 25);
        ipTextField.setBorder(null);
        ipTextField.setCaretColor(Color.BLACK);

        portLabel = new JLabel("Port: ");
        portLabel.setForeground(Color.WHITE);
        portLabel.setBounds(85, 200, 75, 25);
        portLabel.setFont(new Font("Arial", Font.BOLD, 15));

        portTextField = new JTextField(10);
        portTextField.setForeground(Color.BLACK);
        portTextField.setBounds(135, 200, 150, 25);
        portTextField.setCaretColor(Color.BLACK);
        portTextField.setBorder(new LineBorder(new Color(64, 17, 127), 2, false));

        connectButton = new JButton("Connect");
        connectButton.setBackground(new Color(43, 55, 132));
        connectButton.setForeground(Color.WHITE);
        connectButton.setBorder(null);
        connectButton.setFocusPainted(false);
        connectButton.setFocusable(false);
        connectButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
        connectButton.setBounds(250, 250, 100, 32);
        connectButton.addActionListener(e -> handleConnectAction());
        connectButton.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleConnectAction();
                }
            }
        });
        nameTextField.addActionListener(e -> handleConnectAction());
        ipTextField.addActionListener(e -> handleConnectAction());
        portTextField.addActionListener(e -> handleConnectAction());

        exitButton = new JButton("EXIT");
        exitButton.setBackground(new Color(177, 19, 19));
        exitButton.setForeground(Color.WHITE);
        exitButton.setBorder(null);
        exitButton.setFocusPainted(false);
        exitButton.setFocusable(false);
        exitButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
        exitButton.setBounds(250, 290, 100, 28);
        exitButton.addActionListener(e -> System.exit(0));

        panel.add(nameLabel);
        panel.add(nameTextField);
        panel.add(ipLabel);
        panel.add(ipTextField);
        panel.add(portLabel);
        panel.add(portTextField);
        panel.add(connectButton);
        panel.add(exitButton);

        frame.getContentPane().add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void handleConnectAction() {
        String name = nameTextField.getText().trim();
        String ip = ipTextField.getText().trim();
        int port;

        try {
            port = Integer.parseInt(portTextField.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Port number must be an integer.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (name.isEmpty() || ip.isEmpty() || port <= 0 || port > 65535) {
            JOptionPane.showMessageDialog(frame, "Please enter valid connection details.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        this.promptName = name;
        this.promptIp = ip;
        this.promptPort = port;
        frame.dispose();

        new GuiChatt(promptName, promptIp, promptPort);
    }

    public String getPromptName() {
        return promptName;
    }

    public String getPromptIp() {
        return promptIp;
    }

    public int getPromptPort() {
        return promptPort;
    }
/*
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Prompt::new);
    }
    */

}
