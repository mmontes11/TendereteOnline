package es.udc.pa.pa013.practicapa.model.userservice.util;

import java.util.Random;

public class PasswordEncrypter {
	/*
	 * "jcrypt" only considers the first 8 characters of the clear text, and
	 * generates an encrypted text that is always 13 characters in length. The
	 * first two characters of the encrypted text are always the first two
	 * characters of the salt (the first parameter of "jcrypt"). If the length
	 * of the salt is lower than 2, the salt is completed with a default
	 * character ('A'). The salt can not contain non-ASCII characters.
	 * Otherwise, an ArrayIndexOutOfBoundsException is thrown. This salt is used
	 * to perturb the algorithm in one of 4096 different ways. Check "man crypt"
	 * on a Unix machine and the implementation of "jcrypt" for further details.
	 *
	 * The implementation of "PasswordEncrypter" generates a random 2-character
	 * salt to encrypt a password. The first two characters of the encrypted
	 * password correspond to the salt. These two characters are randomly chosen
	 * among the 26 capital letters (from 'A' to 'Z') to guarantee that the
	 * characters returned in encrypted passwords are valid characters for a
	 * cookie (see javax.servlet.http.Cookie).
	 */

	private final static int A_ASCII_CODE = 65;
	private final static int Z_ASCII_CODE = 90;
	private final static int NUMBER_OF_LETTERS = Z_ASCII_CODE - A_ASCII_CODE
			+ 1;

	private PasswordEncrypter() {
	}

	private final static String generateRandomSalt() {

		Random randomGenerator = new Random();
		byte[] saltAsByteArray = new byte[2];
		String salt;

		saltAsByteArray[0] = (byte) (randomGenerator.nextInt(NUMBER_OF_LETTERS) + A_ASCII_CODE);
		saltAsByteArray[1] = (byte) (randomGenerator.nextInt(NUMBER_OF_LETTERS) + A_ASCII_CODE);
		salt = new String(saltAsByteArray);

		return salt;

	}

	public final static String crypt(String clearPassword) {

		String salt = generateRandomSalt();

		return jcrypt.crypt(salt, clearPassword);

	}

	public final static boolean isClearPasswordCorrect(String clearPassword,
			String encryptedPassword) {

		String salt = encryptedPassword.substring(0, 2);
		String encryptedClearPassword = jcrypt.crypt(salt, clearPassword);

		return encryptedClearPassword.equals(encryptedPassword);

	}

}
