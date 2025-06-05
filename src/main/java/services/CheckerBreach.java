package services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;

/**
 * Classe responsável por verificar se uma senha foi comprometida
 * usando a API do serviço "Have I Been Pwned".
 * Utiliza o método k-Anonymity com SHA-1 para proteger a privacidade da senha.
 */
public class CheckerBreach {

    /**
     * Verifica se a senha fornecida foi exposta em algum vazamento conhecido.
     *
     * @param password Senha em texto plano a ser verificada
     * @return true se a senha estiver na base de dados de senhas vazadas; false caso contrário
     * @throws Exception Caso haja erro de conexão ou no processo de hashing
     */
    public static boolean isBreached(String password) throws Exception {
        // Gera o hash SHA-1 da senha
        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
        byte[] digest = sha1.digest(password.getBytes());

        // Converte o hash para uma string hexadecimal (em letras maiúsculas)
        StringBuilder hexString = new StringBuilder();
        for (byte b : digest) {
            hexString.append(String.format("%02X", b));
        }

        String hash = hexString.toString(); // Hash completo da senha
        String prefix = hash.substring(0, 5); // Os 5 primeiros caracteres
        String suffix = hash.substring(5);   // O restante do hash

        // Consulta a API usando o prefixo (k-Anonymity)
        URL url = new URL("https://api.pwnedpasswords.com/range/" + prefix);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        // Lê a resposta da API (lista de sufixos e contagens)
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;

        // Verifica se algum sufixo retornado bate com o da senha
        while ((inputLine = in.readLine()) != null) {
            if (inputLine.startsWith(suffix)) {
                in.close();
                return true; // Senha foi encontrada na lista de vazamentos
            }
        }

        in.close();
        return false; // Senha não foi encontrada
    }
}
