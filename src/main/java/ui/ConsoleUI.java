package ui;

import model.Credential;
import services.*;
import database.DataBaseSecurity;

import java.util.List;
import java.util.Scanner;

public class ConsoleUI {
    private final ServiceAuth authService;
    private final ServiceCrypto cryptoService;
    private final Scanner scanner; 

    public ConsoleUI(ServiceAuth authService, ServiceCrypto cryptoService, Scanner scanner) {
        this.authService = authService;
        this.cryptoService = cryptoService;
        this.scanner = scanner; 
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
                case 3 -> System.out.println("Senha gerada: " + GeneratorPassword.generate(12));
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

        if (CheckerBreach.isBreached(password)) {
            System.out.println("Essa senha já apareceu em vazamentos! Pense utilizar outra senha ou pegue uma das nossas geradas.");
        }

        String encrypted = cryptoService.encrypt(password);
        DataBaseSecurity.save(new Credential(service, user, encrypted));
        System.out.println("Credencial salva com êxito.");
    }

    private void listCredentials() throws Exception {
        List<Credential> creds = DataBaseSecurity.getAll();
        for (Credential c : creds) {
            System.out.println(c + " | Senha (decifrada): " + cryptoService.decrypt(c.getEncryptedPassword()));
        }
    }
}
