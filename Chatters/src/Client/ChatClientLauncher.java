package Client;

import javax.swing.*;

public class ChatClientLauncher {

    // the program starts from prompt class ---> guichatt --> client
        public static void main(String[] args) {
            SwingUtilities.invokeLater(Prompt::new);
        }

}
