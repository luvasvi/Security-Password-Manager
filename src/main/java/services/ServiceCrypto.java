package services;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * Serviço de criptografia para:
 * - Criptografar e descriptografar dados usando AES (128 bits).
 * 
 * A chave de criptografia deve ter exatamente 16 caracteres (128 bits).
 */
public class ServiceCrypto {
    private static final String algorithm = "AES";
    private final SecretKeySpec secretKey;

    /**
     * Construtor que inicializa a chave de criptografia.
     * 
     * @param encryptionKey Chave de criptografia de 16 caracteres
     */
    public ServiceCrypto(String encryptionKey) {
        if (encryptionKey == null || encryptionKey.length() != 16) {
            throw new IllegalArgumentException("A chave de criptografia deve conter 16 caracteres.");
        }
        secretKey = new SecretKeySpec(encryptionKey.getBytes(), algorithm);
    }

    /**
     * Criptografa uma string de dados usando AES e codifica em Base64.
     * 
     * @param data Texto original a ser criptografado
     * @return Dados criptografados em Base64
     * @throws Exception Caso ocorra erro no processo de criptografia
     */
    public String encrypt(String data) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encrypted = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    /**
     * Descriptografa uma string criptografada em Base64 usando AES.
     * 
     * @param encryptedData Dados criptografados em Base64
     * @return Texto original descriptografado
     * @throws Exception Caso ocorra erro no processo de descriptografia
     */
    public String decrypt(String encryptedData) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(decrypted);
    }
}
