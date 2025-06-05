package services;

import java.io.*;
import java.util.Scanner;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;

/**
 * Serviço de autenticação que gerencia:
 * - Senha mestre
 * - Autenticação em dois fatores (2FA) via TOTP com Google Authenticator
 */
public class ServiceAuth {
    private final String masterPassword;
    private final GoogleAuthenticator gAuth;
    private final Scanner scanner;
    private String totpSecret;
    private static final String SECRET_FILE = "totp.secret";

    /**
     * Construtor que inicializa o serviço, carregando ou criando o segredo TOTP.
     * 
     * @param masterPassword Senha mestre do usuário
     * @param scanner Scanner para entrada de dados via CLI
     * @throws IOException Caso ocorra erro na leitura/escrita do arquivo do segredo
     */
    public ServiceAuth(String masterPassword, Scanner scanner) throws IOException {
        this.masterPassword = masterPassword;
        this.scanner = scanner;
        this.gAuth = new GoogleAuthenticator();
        loadOrCreateTOTPSecret();
    }

    /**
     * Realiza a autenticação do usuário:
     * - Verifica se a senha mestre informada é correta
     * - Solicita e valida o código 2FA via Google Authenticator
     * 
     * @return true se autenticado com sucesso; false caso contrário
     */
    public boolean authenticate() {
        if (masterPassword == null || masterPassword.isEmpty()) {
            System.err.println("A variável de ambiente MASTER_PASSWORD não está definida.");
            return false;
        }

        System.out.print("Digite a senha mestre: ");
        String input = scanner.nextLine(); // Entrada da senha mestre

        if (!input.equals(masterPassword)) {
            return false; // Senha mestre incorreta
        }

        System.out.print("Digite o código 2FA do Google Authenticator: ");
        int code = Integer.parseInt(scanner.nextLine()); // Entrada do código 2FA

        boolean authorized = gAuth.authorize(totpSecret, code); // Verifica código 2FA

        if (!authorized) {
            System.out.println("Código 2FA incorreto.");
            return false; // Código 2FA inválido
        }

        return true; // Autenticado com sucesso
    }

    /**
     * Carrega o segredo TOTP salvo em arquivo ou cria um novo caso não exista.
     * Salva o segredo em arquivo para reutilização.
     * Imprime na tela a URL para geração do QR code no Google Authenticator.
     * 
     * @throws IOException Caso haja erro de I/O
     */
    private void loadOrCreateTOTPSecret() throws IOException {
        File file = new File(SECRET_FILE);
        if (file.exists()) {
            // Lê segredo salvo
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                this.totpSecret = reader.readLine();
            }
        } else {
            // Cria novo segredo e salva
            GoogleAuthenticatorKey key = gAuth.createCredentials();
            this.totpSecret = key.getKey();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(this.totpSecret);
            }

            // Gera URL para QR Code e exibe para o usuário configurar no app
            String qrUrl = GoogleAuthenticatorQRGenerator.getOtpAuthURL("SenhaSeguraApp", "usuario", key);
            System.out.println("\nEscaneie este QR Code no Google Authenticator:");
            System.out.println(qrUrl + "\n");
        }
    }
}
