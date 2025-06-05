package database;

import model.Credential;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável pela conexão e operações no banco de dados SQLite
 * que armazena as credenciais do usuário.
 */
public class DataBaseSecurity {
    // URL do banco de dados SQLite local
    private static final String DB_URL = "jdbc:sqlite:database.db";

    /**
     * Método para criar e retornar uma conexão com o banco de dados SQLite.
     *
     * @return Conexão com o banco de dados.
     * @throws SQLException Caso ocorra erro ao conectar.
     */
    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    // Bloco estático executado quando a classe é carregada.
    // Cria a tabela 'credentials' se ela não existir.
    static {
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(
                     "CREATE TABLE IF NOT EXISTS credentials (service TEXT, username TEXT, password TEXT)"
             )) {
            pstmt.execute();
        } catch (SQLException e) {
            System.err.println("Erro ao criar a tabela: " + e.getMessage());
        }
    }

    /**
     * Salva uma nova credencial na tabela 'credentials'.
     *
     * @param cred Objeto Credential contendo serviço, usuário e senha criptografada.
     */
    public static void save(Credential cred) {
        String sql = "INSERT INTO credentials(service, username, password) VALUES (?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, cred.getService());
            pstmt.setString(2, cred.getUsername());
            pstmt.setString(3, cred.getEncryptedPassword());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao salvar credencial: " + e.getMessage());
        }
    }

    /**
     * Verifica se já existe uma credencial para um dado serviço e usuário.
     * A verificação é case-insensitive.
     *
     * @param service Nome do serviço.
     * @param username Nome do usuário.
     * @return true se a credencial existir, false caso contrário.
     */
    public static boolean credentialExists(String service, String username) {
        String sql = "SELECT 1 FROM credentials WHERE LOWER(service) = LOWER(?) AND LOWER(username) = LOWER(?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, service);
            pstmt.setString(2, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next(); // Retorna true se existir pelo menos um registro
            }
        } catch (SQLException e) {
            System.err.println("Erro ao verificar duplicidade: " + e.getMessage());
            return false;
        }
    }

    /**
     * Recupera todas as credenciais armazenadas no banco.
     *
     * @return Lista de objetos Credential contendo os dados armazenados.
     */
    public static List<Credential> getAll() {
        List<Credential> creds = new ArrayList<>();
        String sql = "SELECT service, username, password FROM credentials";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                creds.add(new Credential(
                    rs.getString("service"),
                    rs.getString("username"),
                    rs.getString("password")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar credenciais: " + e.getMessage());
        }
        return creds;
    }
}
