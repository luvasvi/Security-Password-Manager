package model;

public class Credential {
    private String service;
    private String username;
    private String encryptedPassword;

    public Credential(String service, String username, String encryptedPassword) {
        this.service = service;
        this.username = username;
        this.encryptedPassword = encryptedPassword;
    }

    public String getService() {
        return service;
    }

    public String getUsername() {
        return username;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    @Override
    public String toString() {
        return "Serviço: " + service + ", Usuário: " + username;
    }
}
