package Client;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

public class GuiChatt {
    private JFrame frame;
    private JTextPane messagesArea;
    private JList<String> membersList;
    private JTextArea inputField;
    private JButton sendButton;
    private JButton quitButton;
    private ImageIcon frameIcon;
    private DefaultListModel<String> membersListModel;
    private Client client;
    private String userName;
    private JButton imageButton;
    private static final String inputFieldUserAlert = "Type message here...";

    public GuiChatt(String userName, String ip, int port) {
        this.userName = userName;
        frame = new JFrame("Chatter " + userName);
        frameIcon = new ImageIcon("..\\Chatters\\images\\newFrameIcon.png");

        // Messages area setup
        messagesArea = new JTextPane();
        messagesArea.setBackground(new Color(46, 46, 46));
        messagesArea.setForeground(Color.WHITE);
        messagesArea.setEditable(false);
        messagesArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane messagesScrollPane = new JScrollPane(messagesArea);
        messagesScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        messagesScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        messagesScrollPane.setBorder(null);

        // Members list setup
        membersListModel = new DefaultListModel<>();
        membersList = new JList<>(membersListModel);
        membersList.setBackground(new Color(46, 46, 46));
        membersList.setForeground(Color.WHITE);
        membersList.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane membersScrollPane = new JScrollPane(membersList);
        membersScrollPane.setBorder(null);
        membersScrollPane.setPreferredSize(new Dimension(150, messagesScrollPane.getHeight()));

        // Input field setup
        inputField = new JTextArea(3, 50);
        inputField.setBackground(new Color(60, 60, 60));
        inputField.setForeground(Color.WHITE);
        inputField.setCaretColor(Color.WHITE);
        inputField.setFont(new Font("Arial", Font.PLAIN, 14));
        inputField.setLineWrap(true);
        inputField.setWrapStyleWord(true);
        inputField.setFocusTraversalKeysEnabled(false);
        inputField.setText(inputFieldUserAlert);
        inputField.setForeground(Color.GRAY);
        inputField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (inputField.getText().equals(inputFieldUserAlert)) {
                    inputField.setText("");
                    inputField.setForeground(Color.WHITE);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (inputField.getText().isEmpty()) {
                    inputField.setText(inputFieldUserAlert);
                    inputField.setForeground(Color.GRAY);
                }
            }
        });

        inputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    e.consume();
                    sendMessage();
                }
            }
        });

        JScrollPane inputFieldScrollPane = new JScrollPane(inputField);
        inputFieldScrollPane.setBorder(null);
        inputFieldScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        inputFieldScrollPane.setPreferredSize(new Dimension(530, inputField.getPreferredSize().height));

        // Buttons setup
        ImageIcon sendLogo = new ImageIcon("..\\Chatters\\images\\sendingLogo.png");
        sendButton = new JButton(sendLogo);
        sendButton.setPreferredSize(new Dimension(60, 40));
        sendButton.setFocusPainted(false);
        sendButton.setFocusable(false);
        sendButton.setBorder(null);
        sendButton.setOpaque(false);
        sendButton.setBackground(Color.WHITE);
        sendButton.setForeground(Color.WHITE);
        sendButton.setFont(new Font("Arial", Font.BOLD, 14));
        sendButton.addActionListener(e -> sendMessage());

        quitButton = new JButton("Quit");
        quitButton.setBackground(new Color(177, 19, 19));
        quitButton.setForeground(Color.WHITE);
        quitButton.setPreferredSize(new Dimension(60, 40));
        quitButton.setFocusPainted(false);
        quitButton.setBorder(null);
        quitButton.setFont(new Font("Arial", Font.BOLD, 12));
        ImageIcon joptionpaneQuiteImage = new ImageIcon("..\\Chatters\\images\\quitPrompt.png");
        quitButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(frame,
                    "Are you sure you want to quit the chat room?",
                    "Quit", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    joptionpaneQuiteImage);
            if (choice == JOptionPane.YES_OPTION) {
                client.shutdown();
                System.exit(0);
            }
        });

        imageButton = new JButton("Image");
        imageButton.setBackground(new Color(46, 46, 46));
        imageButton.setForeground(Color.WHITE);
        imageButton.setPreferredSize(new Dimension(60, 40));
        imageButton.setFocusPainted(false);
        imageButton.setBorder(null);
        imageButton.setFont(new Font("Arial", Font.BOLD, 12));
        imageButton.addActionListener(e -> chooseImageFile());

        // Bottom panel setup
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(46, 46, 46));

        bottomPanel.add(inputFieldScrollPane);
        bottomPanel.add(sendButton);
        bottomPanel.add(imageButton);
        bottomPanel.add(quitButton);

        // Main panel setup
        JPanel mainPanel = new JPanel(new BorderLayout(1, 1));
        mainPanel.setBackground(Color.BLACK);
        mainPanel.add(messagesScrollPane, BorderLayout.CENTER);
        mainPanel.add(membersScrollPane, BorderLayout.EAST);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        frame.setIconImage(frameIcon.getImage());
        frame.getContentPane().add(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(750, 400));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        //Start the client after initializing the GUI to avoid null pointer exceptions
        client = new Client(this, ip, port);
        new Thread(client).start();
    }

    public void chooseImageFile() {
        FileDialog dialog = new FileDialog(frame, "Select Image File");
        dialog.setMode(FileDialog.LOAD);
        dialog.setVisible(true);

        String directory = dialog.getDirectory();
        String file = dialog.getFile();

        if (directory != null && file != null) {
            String filePath = directory + file;
            try {
                File imageFile = new File(filePath);
                BufferedImage bufferedImage = ImageIO.read(imageFile);

                // Resize the image
                int maxWidth = 500;
                int maxHeight = 500;
                Image resizedImage = resizeImage(bufferedImage, maxWidth, maxHeight);

                BufferedImage resizedBufferedImage = new BufferedImage(resizedImage.getWidth(null), resizedImage.getHeight(null), BufferedImage.TYPE_INT_RGB);
                Graphics2D g2d = resizedBufferedImage.createGraphics();
                g2d.drawImage(resizedImage, 0, 0, null);
                g2d.dispose();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(resizedBufferedImage, "jpg", baos);
                byte[] imageData = baos.toByteArray();

                String encodedImage = Base64.getEncoder().encodeToString(imageData);

                client.sendMessageToServer("/image " + encodedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendMessage() {
        String message = inputField.getText().trim();
        if (!message.isEmpty()) {
            client.sendMessage(message);
            inputField.setText("");
            inputField.requestFocusInWindow();
        }
    }

    public void addMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            try {
                StyleContext context = new StyleContext();
                Style style = context.addStyle("MessageStyle", null);
                StyleConstants.setForeground(style, Color.WHITE);
                messagesArea.getStyledDocument().insertString(messagesArea.getStyledDocument().getLength(), message + "\n", style);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        });
    }

    public void addMember(String member) {
        SwingUtilities.invokeLater(() -> membersListModel.addElement(member));
    }

    public void removeMember(String member) {
        SwingUtilities.invokeLater(() -> membersListModel.removeElement(member));
    }

    public void clearMembers() {
        SwingUtilities.invokeLater(membersListModel::clear);
    }

    public String getUserName() {
        return userName;
    }

    public void addImage(ImageIcon imageIcon) {
        SwingUtilities.invokeLater(() -> {
            messagesArea.setCaretPosition(messagesArea.getDocument().getLength());
            JLabel imageLabel = new JLabel(imageIcon);
            imageLabel.setOpaque(false);
            messagesArea.insertComponent(imageLabel);

            try {
                messagesArea.getStyledDocument().insertString(messagesArea.getDocument().getLength(), "\n", null);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        });
    }

    private Image resizeImage(BufferedImage originalImage, int maxWidth, int maxHeight) {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        if (width > maxWidth) {
            height = (int) ((double) height * ((double) maxWidth / (double) width));
            width = maxWidth;
        }

        if (height > maxHeight) {
            width = (int) ((double) width * ((double) maxHeight / (double) height));
            height = maxHeight;
        }

        return originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }
}
