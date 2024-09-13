package Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ServerGui {
    private JFrame frame;
    private JTextField portTextField;
    private JButton startButton;
    private JButton shutdownButton;
    private JLabel runningLabel;
    private JPanel panel;
    private JPanel buttonPanel;
    private ImageIcon frameIcon;
    private JPanel portPanel;
    private JLabel portLabel;

    public ServerGui() {
        frame = new JFrame("Server");
        frameIcon = new ImageIcon("..\\Chatters\\images\\newServerIcon.png");
        frame.setIconImage(frameIcon.getImage());
        panel = new JPanel(new BorderLayout());


        runningLabel = new JLabel("Server is running...");
        runningLabel.setVisible(false);

        buttonPanel = new JPanel(new GridLayout(1, 2));

        startButton = new JButton("Start Server");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String portStr = portTextField.getText();
                try {
                    int port = Integer.parseInt(portStr);
                    if (isValidPort(port)) {
                        Server server = new Server(port);
                        new Thread(server).start();
                        runningLabel.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(frame, "Please enter a valid port number (1024-65535).");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter a valid port number.");
                }
            }
        });
        buttonPanel.add(startButton);

        shutdownButton = new JButton("Shutdown Server");
        shutdownButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ImageIcon icon = new ImageIcon("..\\Chatters\\images\\quitPrompt.png");
                Object[] options = {"Yes", "No"};
                int choice = JOptionPane.showOptionDialog(frame,
                        "Are you sure you want to shutdown the server?",
                        "Shutdown Server", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, icon, options, options[0]);
                if (choice == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
        buttonPanel.add(shutdownButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        portTextField = new JTextField(10);
        portPanel = new JPanel(new FlowLayout());
        portLabel = new JLabel("Port:");

        portPanel.add(portTextField);
        portPanel.add(portLabel);


        panel.add(runningLabel, BorderLayout.CENTER);
        panel.add(portPanel, BorderLayout.NORTH);

        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }



    private boolean isValidPort(int port) {
        return port >= 1024 && port <= 65535;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ServerGui();
            }
        });
    }
}

