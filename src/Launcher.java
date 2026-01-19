import javax.swing.JOptionPane;
import server.MainServer;
import client.MainClient;

public class Launcher {
    public static void main(String[] args) {
        String[] options = {"Start Server", "Start Client"};

        int choice = JOptionPane.showOptionDialog(
                null,
                "Choose what to start",
                "Chat Application",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == 0) {
            MainServer.main(null);
        } else if (choice == 1) {
            MainClient.main(null);
        }
    }
}
