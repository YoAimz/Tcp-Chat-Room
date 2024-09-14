# Tcp-Chat-Room
Simple Server-Client Chat Room using TCP sockets is java based with GUI implemented for messaging and image sharing and presence notifications.

# Important Security Disclaimer
  WARNING: This application is not secure for real-world use or deployment on public networks. It is made for educational        purposes.

## Application Preview
### Server-GUI
![server-gui](https://github.com/user-attachments/assets/089582b9-59fc-43f4-8b1f-f76aced2410e)

Server interface for starting and managing the server
***
### Client-Connection-GUI
![client-connection-gui](https://github.com/user-attachments/assets/91e675d6-6871-4547-aac5-35d18777131e)

Client interface for connecting to the chat server
***
### Chat-Interface
![chat-interface](https://github.com/user-attachments/assets/7a5d2257-7455-4302-a525-fb303650b975)

Main interface for sending messages and viewing users in the chat room
***
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
