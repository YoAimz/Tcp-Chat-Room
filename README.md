# Tcp-Chat-Room
Simple Server-Client Chat Room using TCP sockets is java based with GUI implemented for messaging and image sharing and presence notifications.

# Important Security Disclaimer
  WARNING: This application is not secure for real-world use or deployment on public networks. It is made for educational        purposes.

## Features

- Client-server architecture using Java socket programming
- Swing GUI for both server and client applications
- Real-time text messaging
- Image sharing capability
- User join/leave notifications
- Active member list
- Multi-threaded server for handling concurrent client connections

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 8 or higher
- Java IDE (e.g., IntelliJ IDEA, Eclipse) or command line interface

### Compilation

1. Clone the repository:
   ```
   git clone https://github.com/yourusername/ChattersApp.git
   ```
2. Navigate to the project directory:
   ```
   cd ChattersApp
   ```
3. Compile the server and client classes:
   ```
   javac Server/*.java
   javac Client/*.java
   ```

### Running the Application

1. Start the Server:
   - Run the `ServerGui` class:
     ```
     java Server.ServerGui
     ```
   - Enter a port number (1025-65535) in the GUI and click "Start Server"

2. Start the Client:
   - Run the `ChatClientLauncher` class:
     ```
     java Client.ChatClientLauncher
     ```
   - In the connection prompt, enter:
     - Your desired username
     - Server IP (use "localhost" if running on the same machine)
     - Port number (same as entered for the server)
   - Click "Connect" to join the chat room


## Project Structure

- `Server/`: Contains server-side code
  - `Server.java`: Main server logic
  - `ServerGui.java`: GUI for the server (First start this for the server and then run the chatClientLauncher)
- `Client/`: Contains client-side code
  - `ChatClientLauncher.java`: Entry point for the client application (Run the program from this file)
  - `Client.java`: Main client logic
  - `GuiChatt.java`: GUI for the client
  - `Prompt.java`: Initial connection prompt for the client
  
  (run first `serverGui.java` and then `chatClientLauncher.java`)
