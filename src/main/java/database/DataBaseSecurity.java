package database;

import model.Credential;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataBaseSecurity {
    private static final String DB_URL = "jdbc:sqlite:database.db";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

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

    public static boolean credentialExists(String service, String username) {
        String sql = "SELECT 1 FROM credentials WHERE LOWER(service) = LOWER(?) AND LOWER(username) = LOWER(?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, service);
            pstmt.setString(2, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next(); // Existe uma credencial igual
            }
        } catch (SQLException e) {
            System.err.println("Erro ao verificar duplicidade: " + e.getMessage());
            return false;
        }
    }

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
