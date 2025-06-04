package org.example;

import io.github.cdimascio.dotenv.Dotenv;
import services.AuthService;
import services.CryptoService;
import ui.ConsoleUI;

import java.util.Scanner; // ← importar Scanner

public class Main {
    public static void main(String[] args) throws Exception {
        Dotenv dotenv = Dotenv.load();

        String masterPassword = dotenv.get("MASTER_PASSWORD");
        String encryptionKey = dotenv.get("ENCRYPTION_KEY");

        // um unico scanner
        Scanner scanner = new Scanner(System.in);

        // passando no authservice e no console q usa tb scnner
        AuthService authService = new AuthService(masterPassword, scanner);
        CryptoService cryptService = new CryptoService(encryptionKey);
        ConsoleUI consoleUI = new ConsoleUI(authService, cryptService, scanner);

        consoleUI.start();

        //fechando
        scanner.close();
    }
}
