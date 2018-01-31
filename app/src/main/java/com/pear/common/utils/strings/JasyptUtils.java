package com.pear.common.utils.strings;

import org.jasypt.util.text.BasicTextEncryptor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;



/**
 * 加密的工具包
 * */
public class JasyptUtils {
	
	private static final String PASSWORD = "xiaokedou";

	/**
	 * 加密
	 * @param content 待加密的数据
	 * @param time 当前时间
	 * @return 已加密的数据
	 * */
	public static String encode(String content,Long time) {
		try {
			BasicTextEncryptor encryptor = new BasicTextEncryptor();
			encryptor.setPassword(PASSWORD+time);
			return encryptor.encrypt(content);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 解密
	 * 
	 * @param codeStr 待解密的数据
	 * @param time 当前时间
	 * @return 已解密的数据
	 * */
	public static String decode(String codeStr,Long time) {
		try {
			BasicTextEncryptor encryptor = new BasicTextEncryptor();
			encryptor.setPassword(PASSWORD+time);
			return encryptor.decrypt(codeStr);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void main(String args[]) {
		/*
		String content = "123456san年号dfg";
		System.out.println(content);
		String codeStr = encode(content);
		System.out.println(codeStr);
		content = decode(codeStr);
		System.out.println(content);
		*/
	}

}