package Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {
    private final List<ConnectionHandler> clientHandlerList;
    private final Set<String> members;
    private ServerSocket serverSocket;
    private boolean isRunning;
    private ExecutorService threadPool;
    private final int port;

    public Server(int port) {
        this.port = port;
        clientHandlerList = new CopyOnWriteArrayList<>();
        members = Collections.synchronizedSet(new HashSet<>());
        isRunning = false;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            threadPool = Executors.newCachedThreadPool();
            isRunning = true;
            while (isRunning) {
                Socket client = serverSocket.accept();
                ConnectionHandler handler = new ConnectionHandler(client, this);
                threadPool.execute(handler);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            shutdown();
        }
    }

    public void broadcast(String message) {
        synchronized (clientHandlerList) {
            for (ConnectionHandler ch : clientHandlerList) {
                ch.sendMessage(message);
            }
        }
    }

    public void updateMembers() {
        StringBuilder memberList = new StringBuilder("/members ");
        synchronized (members) {
            for (String member : members) {
                memberList.append(member).append(",");
            }
        }
        if (memberList.length() > 9) {
            memberList.setLength(memberList.length() - 1); // Remove the trailing comma
        }
        broadcast(memberList.toString());
    }

    public synchronized void addMember(String member, ConnectionHandler handler) {
        members.add(member);
        clientHandlerList.add(handler);
        updateMembers();
        broadcast(member + " joined the chat.");
    }

    public synchronized void removeMember(String member, ConnectionHandler handler) {
        members.remove(member);
        clientHandlerList.remove(handler);
        updateMembers();
        broadcast(member + " left the chat.");
    }

    public void shutdown() {
        isRunning = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            synchronized (clientHandlerList) {
                for (ConnectionHandler ch : clientHandlerList) {
                    ch.shutdown();
                }
            }
            if (threadPool != null && !threadPool.isShutdown()) {
                threadPool.shutdown();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class ConnectionHandler implements Runnable {
        private final Socket client;
        private BufferedReader in;
        private PrintWriter out;
        private String userName;
        private final Server server;

        public ConnectionHandler(Socket client, Server server) {
            this.client = client;
            this.server = server;
        }

        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                out = new PrintWriter(client.getOutputStream(), true);

                userName = in.readLine();
                if (userName == null || userName.trim().isEmpty()) {
                    out.println("Invalid username.");
                    shutdown();
                    return;
                }

                server.addMember(userName, this);
                sendInitialMemberList();

                String message;
                while ((message = in.readLine()) != null) {
                    if (message.startsWith("/image ")) {
                        server.broadcast("/image " + message.substring(7).trim());
                    } else {
                        server.broadcast(userName + ": " + message);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                //server.removeMember(userName, this);
                shutdown();
            }
        }

        private void sendInitialMemberList() {
            StringBuilder memberList = new StringBuilder("/members ");
            synchronized (server.members) {
                for (String member : server.members) {
                    memberList.append(member).append(",");
                }
            }
            if (memberList.length() > 9) {
                memberList.setLength(memberList.length() - 1); // Remove the trailing comma
            }
            out.println(memberList.toString());
        }

        public void sendMessage(String message) {
            out.println(message);
        }

        public void shutdown() {
            try {
                if (!client.isClosed()) {
                    client.close();
                }
                server.removeMember(userName, this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}

