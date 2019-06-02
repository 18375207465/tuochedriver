package com.framework.app.component.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Base64 {
	private final static String DATA_BIN2ASCII = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

	public static String encodeBase64(byte[] binaryData) {
		if (binaryData == null)
			return null;

		// 每3个字节转换成4个字符
		int Length = ((binaryData.length + 2) / 3) << 2;
		StringBuilder sb = new StringBuilder(Length);

		int Index = 0;
		for (int i = binaryData.length; i > 0; i -= 3) {
			if (i >= 3) { // 将3字节数据转换成4个ASCII字符
				int b0 = binaryData[Index++] & 0xFF;
				int b1 = binaryData[Index++] & 0xFF;
				int b2 = binaryData[Index++] & 0xFF;

				sb.append(DATA_BIN2ASCII.charAt(b0 >> 2));
				sb.append(DATA_BIN2ASCII.charAt(((b0 << 4) | (b1 >> 4)) & 0x3F));
				sb.append(DATA_BIN2ASCII.charAt(((b1 << 2) | (b2 >> 6)) & 0x3F));
				sb.append(DATA_BIN2ASCII.charAt(b2 & 0x3F));
			} else {
				int b0 = binaryData[Index++] & 0xFF;
				int b1;
				if (i == 2)
					b1 = binaryData[Index++] & 0xFF;
				else
					b1 = 0;

				sb.append(DATA_BIN2ASCII.charAt(b0 >> 2));
				sb.append(DATA_BIN2ASCII.charAt(((b0 << 4) | (b1 >> 4)) & 0x3F));
				if (i == 1)
					sb.append('=');
				else
					sb.append(DATA_BIN2ASCII.charAt((b1 << 2) & 0x3F));
				sb.append('=');
			}
		}

		return sb.toString();
	}

	// private final static int B64_ERROR = -1; // 0xFF 错误字符
	// private final static int B64_EOLN = -16; // 0xF0 换行\n
	// private final static int B64_CR = -15; // 0xF1 回车\r
	// private final static int B64_EOF = -14; // 0xF2 连字符-
	private final static int B64_WS = -32; // 0xE0 跳格或者空格（\t、space）
	private final static int B64_NOT_BASE64 = -13; // 0xF3 空白字符、回车换行字符、连字符

	private final static byte[] DATA_ASCII2BIN = { -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -32, -16, -1, -1, -15, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -32, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, 0x3E, -1, -14, -1, 0x3F, 0x34, 0x35, 0x36, 0x37,
			0x38, 0x39, 0x3A, 0x3B, 0x3C, 0x3D, -1, -1, -1, 0x00, -1, -1, -1,
			0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A,
			0x0B, 0x0C, 0x0D, 0x0E, 0x0F, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15,
			0x16, 0x17, 0x18, 0x19, -1, -1, -1, -1, -1, -1, 0x1A, 0x1B, 0x1C,
			0x1D, 0x1E, 0x1F, 0x20, 0x21, 0x22, 0x23, 0x24, 0x25, 0x26, 0x27,
			0x28, 0x29, 0x2A, 0x2B, 0x2C, 0x2D, 0x2E, 0x2F, 0x30, 0x31, 0x32,
			0x33, -1, -1, -1, -1, -1 };

	public static byte[] decodeBase64(String base64String) throws IOException {
		if (base64String == null || base64String.isEmpty())
			return null;

		// 去除头部空白字符
		int upIndex = 0;
		int downIndex = base64String.length() - 1;
		while (upIndex <= downIndex) {
			int charValue = base64String.charAt(upIndex);
			if (charValue >= 0x80)
				throw new IllegalArgumentException(); // 不是ASCII字符

			if (DATA_ASCII2BIN[charValue] == B64_WS)
				upIndex++;
			else
				break;
		}

		// 去除尾部的空白字符、回车换行字符、连字符
		while (downIndex >= upIndex) {
			int charValue = base64String.charAt(downIndex);
			if (charValue >= 0x80)
				throw new IllegalArgumentException(); // 不是ASCII字符
			if ((DATA_ASCII2BIN[charValue] | 0x13) == B64_NOT_BASE64)
				downIndex--;
			else
				break;
		}

		// 有效长度必须为4的倍数
		int length = downIndex - upIndex + 1;
		if ((length < 4) || (length % 4 != 0))
			throw new IllegalArgumentException();

		// 转换字符串到字节数组
		ByteArrayOutputStream memoryStream = null;
		try {
			memoryStream = new ByteArrayOutputStream((length >> 2) * 3);
			int[] room = new int[4];
			while (upIndex <= downIndex) {
				int j = 0;
				for (; j < 4; j++) {
					int charValue = base64String.charAt(upIndex++);
					if (charValue >= 0x80)
						throw new IllegalArgumentException();

					if (charValue == '=')
						break; // 填充字符

					charValue = DATA_ASCII2BIN[charValue];
					if (charValue < 0)
						throw new IllegalArgumentException();
					room[j] = charValue;
				}

				if (j == 4) {
					memoryStream.write((room[0] << 2) | (room[1] >> 4));
					memoryStream.write((room[1] << 4) | (room[2] >> 2));
					memoryStream.write((room[2] << 6) | room[3]);
				} else if (j == 3) { // 有1个填充字节
					memoryStream.write((room[0] << 2) | (room[1] >> 4));
					memoryStream.write((room[1] << 4) | (room[2] >> 2));
					break;
				} else if (j == 2) { // 有2个填充字节
					memoryStream.write((room[0] << 2) | (room[1] >> 4));
					break;
				} else
					throw new IllegalArgumentException();
			}

			return memoryStream.toByteArray();
		} finally {
			if (memoryStream != null)
				memoryStream.close();
		}
	}
}
