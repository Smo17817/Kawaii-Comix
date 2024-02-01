package utenteManagement;

import org.apache.commons.codec.digest.DigestUtils;

public class PasswordUtils {
	
	private PasswordUtils() {
		super();
	}

	public static String hashPassword(String password) {
        // Hash della password usando SHA-256
        return DigestUtils.sha256Hex(password);
    }

    public static boolean verifyPassword(String inputPassword, String hashedPassword) {
        // Hash della password immessa dall'utente e confronto con l'hash memorizzato
        String inputPasswordHash = hashPassword(inputPassword);

        return inputPasswordHash.equals(hashedPassword);
    }
}
