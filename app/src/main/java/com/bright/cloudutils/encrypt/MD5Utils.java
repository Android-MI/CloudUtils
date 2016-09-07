package com.bright.cloudutils.encrypt;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {

	/**
	 * 下面的是采用MD5 32位加密.
	 */
	public static String getMd5_32(String num) {
		String str = num;
		byte[] digest = null;
		try {
			digest = MessageDigest.getInstance("MD5").digest(
					str.getBytes("UTF-8"));

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		StringBuilder md5 = new StringBuilder(digest.length * 2);
		for (byte b : digest) {
			if ((b & 0xFF) < 0x10)
				md5.append("0");
			md5.append(Integer.toHexString(b & 0xFF));
		}
		return md5.toString();
	}

	/*
	 * MD5加密 16位加密，从第9位到25位
	 */
	public static String getMd5_16(String str) {
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");

			messageDigest.reset();

			messageDigest.update(str.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			System.out.println("NoSuchAlgorithmException caught!");
			System.exit(-1);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		byte[] byteArray = messageDigest.digest();

		StringBuffer md5StrBuff = new StringBuffer();

		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
				md5StrBuff.append("0").append(
						Integer.toHexString(0xFF & byteArray[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
		}
		// 16位加密，从第9位到25位
		return md5StrBuff.substring(8, 24).toString().toUpperCase();
	}

	public static String getMd5(String string) {
		byte[] hash;
		try {
			hash = MessageDigest.getInstance("MD5").digest(
					string.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Huh, MD5 should be supported?", e);

		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Huh, UTF-8 should be supported?", e);
		}

		StringBuilder hex = new StringBuilder(hash.length * 2);
		for (byte b : hash) {

			if ((b & 0xFF) < 0x10)
				hex.append("0");
			hex.append(Integer.toHexString(b & 0xFF));
		}
		return hex.toString();

	}

	public static String MD5(String info) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(info.getBytes("UTF-8"));
			byte[] encryption = md5.digest();

			StringBuffer strBuf = new StringBuffer();
			for (int i = 0; i < encryption.length; i++) {
				if (Integer.toHexString(0xff & encryption[i]).length() == 1) {
					strBuf.append("0").append(
							Integer.toHexString(0xff & encryption[i]));
				} else {
					strBuf.append(Integer.toHexString(0xff & encryption[i]));
				}
			}

			return strBuf.toString();
		} catch (NoSuchAlgorithmException e) {
			return "";
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}
}
