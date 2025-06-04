package ui;

import model.Credential;
import services.*;
import database.DatabaseService;

import java.util.List;
import java.util.Scanner;

public class ConsoleUI {
    private final AuthService authService;
    private final CryptoService cryptoService;
    private final Scanner scanner; // recebe do main

    public ConsoleUI(AuthService authService, CryptoService cryptoService, Scanner scanner) {
        this.authService = authService;
        this.cryptoService = cryptoService;
        this.scanner = scanner; // ← atribui 
    }

    public void start() throws Exception {
        if (!authService.authenticate()) {
            System.out.println("Autenticação falhou. Encerrando aplicação.");
            return;
        }

        int option = 0;
        do {
            System.out.println("\n1. Adicionar nova senha");
            System.out.println("2. Listar credenciais");
            System.out.println("3. Gerar senha segura");
            System.out.println("4. Sair");
            System.out.print("Escolha: ");
            String input = scanner.nextLine();
            if (input.isEmpty()) continue;
            option = Integer.parseInt(input);

            switch (option) {
                case 1 -> addCredential();
                case 2 -> listCredentials();
                case 3 -> System.out.println("Senha gerada: " + PasswordGenerator.generate(12));
            }
        } while (option != 4);
    }

    private void addCredential() throws Exception {
        System.out.print("Serviço: ");
        String service = scanner.nextLine();
        System.out.print("Usuário: ");
        String user = scanner.nextLine();
        System.out.print("Senha: ");
        String password = scanner.nextLine();

        if (BreachChecker.isBreached(password)) {
            System.out.println("⚠️  Essa senha já apareceu em vazamentos! Considere usar outra.");
        }

        String encrypted = cryptoService.encrypt(password);
        DatabaseService.save(new Credential(service, user, encrypted));
        System.out.println("Credencial salva com sucesso.");
    }

    private void listCredentials() throws Exception {
        List<Credential> creds = DatabaseService.getAll();
        for (Credential c : creds) {
            System.out.println(c + " | Senha (decifrada): " + cryptoService.decrypt(c.getEncryptedPassword()));
        }
    }
}
