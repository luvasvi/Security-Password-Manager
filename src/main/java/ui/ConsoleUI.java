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
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) continue;

            try {
                option = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Opção inválida. Digite um número entre 1 e 4.");
                continue;
            }

            switch (option) {
                case 1 -> addCredential();
                case 2 -> listCredentials();
                case 3 -> System.out.println("Senha gerada: " + GeneratorPassword.generate(12));
                case 4 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida. Tente novamente.");
            }
        } while (option != 4);
    }

    private void addCredential() throws Exception {
        System.out.print("Serviço: ");
        String service = scanner.nextLine().trim();

        System.out.print("Usuário: ");
        String user = scanner.nextLine().trim();

        if (DataBaseSecurity.credentialExists(service, user)) {
            System.out.println("Já existe uma credencial cadastrada para esse serviço e usuário.");
            return;
        }

        System.out.print("Senha: ");
        String password = scanner.nextLine();

        if (CheckerBreach.isBreached(password)) {
            System.out.println("⚠ Essa senha já apareceu em vazamentos!");
            System.out.println("1. Cancelar");
            System.out.println("2. Salvar mesmo assim (não recomendado)");
            System.out.print("Escolha: ");
            String escolha = scanner.nextLine();
            if (!escolha.equals("2")) {
                System.out.println("Operação cancelada.");
                return;
            }
        }

        String encrypted = cryptoService.encrypt(password);
        DataBaseSecurity.save(new Credential(service, user, encrypted));
        System.out.println("Credencial salva com êxito.");
    }

    private void listCredentials() throws Exception {
        List<Credential> creds = DataBaseSecurity.getAll();
        if (creds.isEmpty()) {
            System.out.println("Nenhuma credencial cadastrada.");
            return;
        }

        for (Credential c : creds) {
            String decrypted = cryptoService.decrypt(c.getEncryptedPassword());
            System.out.println("Serviço: " + c.getService() + ", Usuário: " + c.getUsername() + " | Senha (decifrada): " + decrypted);
        }
    }
}
