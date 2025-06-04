package services;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class ServiceCrypto {
    private static final String algorithm = "AES";
    private final SecretKeySpec secretKey;

    public ServiceCrypto(String encryptionKey) {
        if (encryptionKey == null || encryptionKey.length() != 16) {
            throw new IllegalArgumentException("A chave de criptografia conter 16 caracteres.");
        }

        secretKey = new SecretKeySpec(encryptionKey.getBytes(), algorithm);
    }

    public String encrypt(String data) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encrypted = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public String decrypt(String encryptedData) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(decrypted);
    }
}
