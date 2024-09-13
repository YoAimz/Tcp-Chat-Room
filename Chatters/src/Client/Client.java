package Client;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.Base64;

public class Client implements Runnable {
    private Socket clientSocket;
    private BufferedReader inputReader;
    private PrintWriter out;
    private boolean isRunning;
    private GuiChatt chatGui;

    private String ip;
    private int port;

    public Client(GuiChatt chatGui, String ip, int port) {
        this.chatGui = chatGui;
        this.ip = ip;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            clientSocket = new Socket(ip, port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            inputReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            isRunning = true;

            // Send the username to the server
            String userName = chatGui.getUserName();
            out.println(userName);

            Thread serverInputThread = new Thread(new ServerInputHandler());
            serverInputThread.start();

            String inMessage;
            while (isRunning && (inMessage = inputReader.readLine()) != null) {
                handleIncomingMessage(inMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
            shutdown();
        } finally {
            shutdown();
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public void handleIncomingMessage(String message) {
        try {
            if (message.startsWith("/image ")) {
                            if (message.length() > 7) {
                                String imageData = message.substring(7).trim();
                                if (!imageData.isEmpty()) {
                                    byte[] decodedImage = Base64.getDecoder().decode(imageData);
                        ImageIcon receivedImage = new ImageIcon(decodedImage);
                        chatGui.addImage(receivedImage);
                    }
                }
            } else if (message.startsWith("/members ")) {
                //Ensures the message has enough length to avoid StringIndexOutOfBoundsException
                if (message.length() > 9) {
                    String[] members = message.substring(9).split(",");
                    SwingUtilities.invokeLater(() -> {
                        chatGui.clearMembers();
                        for (String member : members) {
                            chatGui.addMember(member.trim());
                        }
                    });
                }
            } else {
                SwingUtilities.invokeLater(() -> chatGui.addMessage(message));
            }
        } catch (Exception e) {
            e.printStackTrace();
            SwingUtilities.invokeLater(() -> chatGui.addMessage("Error processing message: " + message));
        }
    }

    public void shutdown() {
        isRunning = false;
        try {
            if (clientSocket != null && !clientSocket.isClosed()) {
                String userName = chatGui.getUserName();
                //out.println(userName + " has left the chat.");

                clientSocket.close();
            }
            if (inputReader != null) inputReader.close();
            if (out != null) out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessageToServer(String message) {
        if (out != null) {
            out.println(message);
        }
    }

    class ServerInputHandler implements Runnable {
        @Override
        public void run() {
            try {
                String message;
                while (isRunning && (message = inputReader.readLine()) != null) {
                    handleIncomingMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
                shutdown();
            }
        }
    }
}
