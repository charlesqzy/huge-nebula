package com.bizwell.passport.common;

import org.springframework.util.Assert;

/**
 * @author zhangjianjun
 * @date 2018年4月26日
 *
 */
// 十六进制工具类
public class HexHelper {
	
	private static final char UPPER_HEXS[] = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	private static final char LOWER_HEXS[] = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	private HexHelper() {
		
		throw new UnsupportedOperationException();
	}

	public static String getUpperHex(byte[] bytes) {
		
		int len = bytes.length;
		char str[] = new char[len * 2];
		for (int i = 0, k = 0; i < len; i++) {
			byte b = bytes[i];
			str[k++] = UPPER_HEXS[b >>> 4 & 0xf];
			str[k++] = UPPER_HEXS[b & 0xf];
		}
		return new String(str);
	}

	public static String getUpperHex(byte b) {
		
		char[] chars = new char[2];
		chars[0] = UPPER_HEXS[b >>> 4 & 0xf];
		chars[1] = UPPER_HEXS[b & 0xf];
		return new String(chars);
	}

	public static String getLowerHex(byte[] bytes) {
		
		int len = bytes.length;
		char str[] = new char[len * 2];
		for (int i = 0, k = 0; i < len; i++) {
			byte b = bytes[i];
			str[k++] = LOWER_HEXS[b >>> 4 & 0xf];
			str[k++] = LOWER_HEXS[b & 0xf];
		}
		return new String(str);
	}

	public static String getLowerHex(byte b) {
		
		char[] chars = new char[2];
		chars[0] = LOWER_HEXS[b >>> 4 & 0xf];
		chars[1] = LOWER_HEXS[b & 0xf];
		return new String(chars);
	}

	public static byte[] getBytes(String hex) {

		int length = hex.length();

		Assert.isTrue(length % 2 == 0, "the hex text length can't support.");

		char[] data = hex.toCharArray();

		byte[] out = new byte[length >> 1];
		for (int j = 0, i = 0; j < length; i++) {
			int high = Character.digit(data[(j++)], 16) << 4;
			high |= Character.digit(data[(j++)], 16);
			out[i] = (byte) (high & 0xFF);
		}

		return out;
	}

	/*public static void main(String[] args) {
		
		System.out.println(Integer.toHexString('0'));
		System.out.println(Integer.toHexString(307));
		System.out.println((char) Integer.parseInt("36", 16));
	}*/
}