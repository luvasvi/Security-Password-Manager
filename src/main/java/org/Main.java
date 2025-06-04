package org;
import io.github.cdimascio.dotenv.Dotenv;
import services.ServiceAuth;
import services.ServiceCrypto;
import ui.ConsoleUI;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Dotenv dotenv = Dotenv.load();

        String masterPassword = dotenv.get("MASTER_PASSWORD");
        String encryptionKey = dotenv.get("ENCRYPTION_KEY");

        Scanner scanner = new Scanner(System.in);

        
        ServiceAuth authService = new ServiceAuth(masterPassword, scanner);
        ServiceCrypto cryptService = new ServiceCrypto(encryptionKey);
        ConsoleUI consoleUI = new ConsoleUI(authService, cryptService, scanner);

        consoleUI.start();

        scanner.close();
    }
}
