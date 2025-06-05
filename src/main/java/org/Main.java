package org;

import io.github.cdimascio.dotenv.Dotenv;
import services.ServiceAuth;
import services.ServiceCrypto;
import ui.ConsoleUI;

import java.util.Scanner;

/**
 * Classe principal que inicializa o aplicativo.
 * Carrega variáveis de ambiente, inicializa serviços e inicia a interface do usuário.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        // Carrega as variáveis de ambiente do arquivo .env
        Dotenv dotenv = Dotenv.load();

        // Obtém a senha mestre e a chave de criptografia das variáveis de ambiente
        String masterPassword = dotenv.get("MASTER_PASSWORD");
        String encryptionKey = dotenv.get("ENCRYPTION_KEY");

        // Scanner para entrada de dados via terminal
        Scanner scanner = new Scanner(System.in);

        // Inicializa o serviço de autenticação com a senha mestre e scanner
        ServiceAuth authService = new ServiceAuth(masterPassword, scanner);

        // Inicializa o serviço de criptografia com a chave de 16 caracteres
        ServiceCrypto cryptService = new ServiceCrypto(encryptionKey);

        // Inicializa a interface de linha de comando com os serviços e scanner
        ConsoleUI consoleUI = new ConsoleUI(authService, cryptService, scanner);

        // Inicia o fluxo da aplicação (autenticação + menu)
        consoleUI.start();

        // Fecha o scanner para liberar recursos
        scanner.close();
    }
}
