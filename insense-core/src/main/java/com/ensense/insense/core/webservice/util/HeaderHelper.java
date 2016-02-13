package com.ensense.insense.core.webservice.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

import javax.mail.internet.MimeUtility;

public class HeaderHelper {

	
	public static String createGuid() {
		return new GuidUtility().toString();
	}
	
	public static String createTimestamp() {
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		f.setTimeZone( TimeZone.getTimeZone("UTC") );
		return f.format( new Date() );
	}
	
	public static String createDigest( String timestamp, String password ) {
		byte[] data = (timestamp + password).getBytes();
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Issue creating digest", e);
		}
		md.reset();
		md.update(data, 0, data.length);
		
		byte[] digest = md.digest();
		
		//now encode it...
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		OutputStream out = null;
		try {
			out = MimeUtility.encode(bout, "base64");
			out.write( digest );
			out.flush();
		} catch (Exception e) {
			throw new RuntimeException( "Error encoding", e );
		} finally {
			try {
				out.close();
			} catch (IOException e) {}
		}
		return bout.toString().trim();
	}
	
	
	static class GuidUtility {

		public String valueBeforeMD5 = "";

		public String valueAfterMD5 = "";

		private static Random myRand;

		private static SecureRandom mySecureRand;

		private static String s_id;

		/*
		 * Static block to take care of one time secureRandom seed. It takes a few
		 * seconds to initialize SecureRandom. You might want to consider removing
		 * this static block or replacing it with a "time since first loaded" seed
		 * to reduce this time. This block will run only once per JVM instance.
		 */

		static {
			mySecureRand = new SecureRandom();
			long secureInitializer = mySecureRand.nextLong();
			myRand = new Random(secureInitializer);
			try {
				s_id = InetAddress.getLocalHost().toString();
			} catch (UnknownHostException e) {
				throw new RuntimeException("Error getting localhost" , e);
			}

		}

		/*
		 * Default constructor. With no specification of security option, this
		 * constructor defaults to lower security, high performance.
		 */
		public GuidUtility() {
			getGuidUtility(false);
		}

		/*
		 * Constructor with security option. Setting secure true enables each random
		 * number generated to be cryptographically strong. Secure false defaults to
		 * the standard Random function seeded with a single cryptographically
		 * strong random number.
		 */
		public GuidUtility(boolean secure) {
			getGuidUtility(secure);
		}

		/*
		 * Method to generate the random GUID
		 */
		private void getGuidUtility(boolean secure) {
			MessageDigest md5 = null;
			StringBuffer sbValueBeforeMD5 = new StringBuffer();

			try {
				md5 = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e) {
				throw new RuntimeException("Error getting md5 message digest", e);
			}

			long time = System.currentTimeMillis();
			long rand = 0;

			if (secure) {
				rand = mySecureRand.nextLong();
			} else {
				rand = myRand.nextLong();
			}

			// This StringBuffer can be a long as you need; the MD5
			// hash will always return 128 bits. You can change
			// the seed to include anything you want here.
			// You could even stream a file through the MD5 making
			// the odds of guessing it at least as great as that
			// of guessing the contents of the file!
			sbValueBeforeMD5.append(s_id);
			sbValueBeforeMD5.append(":");
			sbValueBeforeMD5.append(Long.toString(time));
			sbValueBeforeMD5.append(":");
			sbValueBeforeMD5.append(Long.toString(rand));

			valueBeforeMD5 = sbValueBeforeMD5.toString();
			md5.update(valueBeforeMD5.getBytes());

			byte[] array = md5.digest();
			StringBuffer sb = new StringBuffer();
			for (int j = 0; j < array.length; ++j) {
				int b = array[j] & 0xFF;
				if (b < 0x10)
					sb.append('0');
				sb.append(Integer.toHexString(b));
			}

			valueAfterMD5 = sb.toString();

		}

		/*
		 * Convert to the standard format for GUID (Useful for SQL Server
		 * UniqueIdentifiers, etc.) Example: C2FEEEAC-CFCD-11D1-8B05-00600806D9B6
		 */
		public String toString() {
			String raw = valueAfterMD5.toUpperCase();
			StringBuffer sb = new StringBuffer();
			sb.append(raw.substring(0, 8));
			sb.append("-");
			sb.append(raw.substring(8, 12));
			sb.append("-");
			sb.append(raw.substring(12, 16));
			sb.append("-");
			sb.append(raw.substring(16, 20));
			sb.append("-");
			sb.append(raw.substring(20));

			return sb.toString();
		}
	}
}
