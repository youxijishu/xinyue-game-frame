package com.xinyue.utils;

import java.security.MessageDigest;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AesUtil {	
	
	private static byte[] iv = new byte[] { 0x0, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9, 0xA, 0xB, 0xC, 0xD, 0xE, 0xF }; 
	private static IvParameterSpec ivSpec = new IvParameterSpec(iv);
	public static byte[] encryptBuffer(String encryptKey, byte[] buffer) throws Exception {
		if (buffer == null) {
			return null;
		}

		SecretKeySpec skeySpec = new SecretKeySpec(MessageDigest.getInstance("MD5").digest(encryptKey.getBytes("utf-8")), "AES");
		Cipher cipher = Cipher.getInstance("AES/CFB/NoPadding");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivSpec);
		
		return cipher.doFinal(buffer);
	}

	public static byte[] decryptBuffer(String encryptKey, byte[] buffer) throws Exception {
		if (buffer == null) {
			return null;
		}

		SecretKeySpec skeySpec = new SecretKeySpec(MessageDigest.getInstance("MD5").digest(encryptKey.getBytes("utf-8")), "AES");
		Cipher cipher = Cipher.getInstance("AES/CFB/NoPadding");
		cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivSpec);
		
		return cipher.doFinal(buffer);
	}
	
	
	public static void main(String[] args) {
		
		AtomicInteger i = new AtomicInteger(0);
		
		Runnable task = new Runnable() {			
			@Override
			public void run() {
				while (true) {			
					try {
						String ENCRYPT_KEY = ((Integer)new Random().nextInt()).toString();
						String str = ((Integer)new Random().nextInt()).toString();
						byte[] bytes = str.getBytes();
						byte[] en = encryptBuffer(ENCRYPT_KEY, bytes);
						byte[] de = decryptBuffer(ENCRYPT_KEY, en);
						String result = new String(de);
						if (!result.equals(str)) {
							System.out.println("Error");
						}
						
						i.incrementAndGet();
					} catch (Exception e) {
						e.printStackTrace();
					}			
				}
			}
		};

		new Thread(task).start();
		new Thread(task).start();
		new Thread(task).start();
		new Thread(task).start();
		
		long time = System.currentTimeMillis();
		while (true) {
			if (System.currentTimeMillis() - time >= 1000) {
				System.out.println(i.getAndSet(0) + "/s");
				time = System.currentTimeMillis();
			}
		}
		
		
	}
}
