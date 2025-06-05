package model;

/**
 * Classe que representa uma credencial armazenada pelo gerenciador de senhas.
 * Contém o nome do serviço, o nome do usuário e a senha criptografada.
 */
public class Credential {
    // Nome do serviço ao qual a credencial pertence (ex: Gmail, Facebook)
    private String service;

    // Nome de usuário associado ao serviço
    private String username;

    // Senha armazenada já criptografada (ex: com AES)
    private String encryptedPassword;

    /**
     * Construtor para criar uma credencial com os dados fornecidos.
     *
     * @param service Nome do serviço
     * @param username Nome do usuário
     * @param encryptedPassword Senha criptografada
     */
    public Credential(String service, String username, String encryptedPassword) {
        this.service = service;
        this.username = username;
        this.encryptedPassword = encryptedPassword;
    }

    /**
     * Obtém o nome do serviço.
     * @return nome do serviço
     */
    public String getService() {
        return service;
    }

    /**
     * Obtém o nome do usuário.
     * @return nome do usuário
     */
    public String getUsername() {
        return username;
    }

    /**
     * Obtém a senha criptografada.
     * @return senha criptografada
     */
    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    /**
     * Retorna uma representação textual da credencial, 
     * contendo serviço e usuário, sem expor a senha.
     *
     * @return String representando a credencial
     */
    @Override
    public String toString() {
        return "Serviço: " + service + ", Usuário: " + username;
    }
}
