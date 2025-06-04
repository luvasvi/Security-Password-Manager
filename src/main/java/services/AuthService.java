package services;

import java.io.*;
import java.util.Scanner;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;

public class AuthService {
    private final String masterPassword;
    private final GoogleAuthenticator gAuth;
    private final Scanner scanner; // puxei direto do main
    private String totpSecret;
    private static final String SECRET_FILE = "totp.secret";

    // atualizei o construtor
    public AuthService(String masterPassword, Scanner scanner) throws IOException {
        this.masterPassword = masterPassword;
        this.scanner = scanner; // guardando o scanner recebido
        this.gAuth = new GoogleAuthenticator();
        loadOrCreateTOTPSecret();
    }

    public boolean authenticate() {
        if (masterPassword == null || masterPassword.isEmpty()) {
            System.err.println("A variável de ambiente MASTER_PASSWORD não está definida.");
            return false;
        }

        System.out.print("Digite a senha mestre: ");
        String input = scanner.nextLine(); // so atualizando

        if (!input.equals(masterPassword)) {
            return false;
        }

        System.out.print("Digite o código 2FA do Google Authenticator: ");
        int code = Integer.parseInt(scanner.nextLine()); // ← idem

        boolean authorized = gAuth.authorize(totpSecret, code);

        if (!authorized) {
            System.out.println("Código 2FA incorreto.");
            return false;
        }

        return true;
    }

    private void loadOrCreateTOTPSecret() throws IOException {
        File file = new File(SECRET_FILE);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                this.totpSecret = reader.readLine();
            }
        } else {
            GoogleAuthenticatorKey key = gAuth.createCredentials();
            this.totpSecret = key.getKey();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(this.totpSecret);
            }

            String qrUrl = GoogleAuthenticatorQRGenerator.getOtpAuthURL("SenhaSeguraApp", "usuario", key);
            System.out.println("\n Escaneie este QR Code no Google Authenticator:");
            System.out.println(qrUrl + "\n");
        }
    }
}
