package com.ensense.insense.core.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import com.sun.mail.util.BASE64DecoderStream;
import com.sun.mail.util.BASE64EncoderStream;

public class EncrypDecryptUtil {
	private static Logger logger = Logger.getLogger(EncrypDecryptUtil.class);
	public static String secretKey = "";

	private static Cipher ecipher;

	private static Cipher dcipher;

	public static String EncryptPassword = "";

	public static String DecryptPassword = "";

	public static String encryptPassword(String pass, String secKey) {
		logger.info("Entry : encryptPassword");
		try {
			byte[] keyBytes = secKey.getBytes();
			DESedeKeySpec keySpec = new DESedeKeySpec(keyBytes);
			SecretKeyFactory factory = SecretKeyFactory.getInstance("DESede");
			SecretKey key = factory.generateSecret(keySpec);

			ecipher = Cipher.getInstance("DESede");
			ecipher.init(Cipher.ENCRYPT_MODE, key); // initialize the ciphers
													// with the given key

			byte[] utf8 = pass.getBytes("UTF8");
			byte[] enc = ecipher.doFinal(utf8);
			enc = BASE64EncoderStream.encode(enc);
			
			logger.info("Exit : encryptPassword");
			return new String(enc);

			// return EncryptPassword;
		} catch (Exception e) {
			logger.error("Exception in encryptPassword, while encripting password.");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
			return e.getLocalizedMessage();
		}
	}

	public static String decryptPassword(String pass, String secKey) {
		if ( null == pass || pass.trim().length() < 1 ){
			return "";
		}
		try {
			byte[] keyBytes = secKey.getBytes();
			DESedeKeySpec keySpec = new DESedeKeySpec(keyBytes);
			SecretKeyFactory factory = SecretKeyFactory.getInstance("DESede");
			SecretKey key = factory.generateSecret(keySpec);

			dcipher = Cipher.getInstance("DESede");
			dcipher.init(Cipher.DECRYPT_MODE, key);

			byte[] dec = BASE64DecoderStream.decode(pass.getBytes());
			byte[] utf8 = dcipher.doFinal(dec);
			return new String(utf8, "UTF8");
		} catch (Exception e) {
			logger.error("Exception in encryptPassword, while encripting password.");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
			return e.getLocalizedMessage();
		}
	}
}