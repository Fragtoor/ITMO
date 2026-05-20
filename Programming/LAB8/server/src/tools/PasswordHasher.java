package tools;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class PasswordHasher {
    public static String generateSalt() {
        String characterSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%^&*()-_=+~`'?><,.";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 7; i++) {
            int index = random.nextInt(characterSet.length());
            sb.append(characterSet.charAt(index));
        }
        return sb.toString();
    }
    public static String hashPassword(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            String pepper = "*63&^mVLC(#";
            byte[] hash = md.digest( (password + pepper + salt).getBytes(StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not found", e);
        }
    }

    public static boolean verifyPassword(String password, String salt, String hash) {
        String newHash = hashPassword(password, salt);
        return newHash.equals(hash);
    }
}
