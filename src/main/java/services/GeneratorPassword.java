package services;

import java.security.SecureRandom;

/**
 * Classe responsável por gerar senhas seguras e aleatórias.
 */
public class GeneratorPassword {

    // Conjunto de caracteres que podem ser usados na senha
    private static final String CHARACTERS = 
        "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +     // Letras maiúsculas
        "abcdefghijklmnopqrstuvwxyz" +     // Letras minúsculas
        "0123456789" +                     // Dígitos numéricos
        "!@#$%^&*()_+";                    // Símbolos especiais

    // Instância de gerador de números aleatórios criptograficamente seguro
    private static final SecureRandom random = new SecureRandom();

    /**
     * Gera uma senha aleatória com o comprimento especificado.
     *
     * @param length O comprimento desejado para a senha
     * @return Uma senha aleatória contendo letras, números e símbolos
     */
    public static String generate(int length) {
        StringBuilder sb = new StringBuilder(length);

        // Gera cada caractere da senha aleatoriamente
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHARACTERS.length()); // Índice aleatório
            sb.append(CHARACTERS.charAt(index));  // Adiciona o caractere correspondente
        }

        return sb.toString(); // Retorna a senha gerada
    }
}
