package com.examlpe.zf_android.util;

import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

/***
 * 
 * @author Lijinpeng
 * 
 *         comdo
 */
public class StringUtil {

	public static String timeUtil(String time) {
		String t = time;
		System.out.println(time.length());
		if (time.length() > 16) {
			t = time.substring(0, 16);
		}
		// 2014-12-10
		t = t.split(" ")[0];
		t = t.substring(5);
		String T1 = t.split("-")[1];
		if (T1.equals("01")) {
			T1 = T1 + "st,";
		}else 
		if (T1.equals("02")) {
			T1 = T1 + "nd,";
		}
		else if (T1.equals("03")) {
			T1 = T1 + "rd,";
		} else {
			T1 = T1 + "th,";
		}
		String T2 = t.split("-")[0];
		if (T2.equals("01")) {
			T2 = "Jan";
		}
		if (T2.equals("04")) {
			T2 = "Apr";
		}
		if (T2.equals("02")) {
			T2 = "Feb";
		}
		if (T2.equals("03")) {
			T2 = "Mar";
		}
		if (T2.equals("05")) {
			T2 = "May";
		}
		if (T2.equals("06")) {
			T2 = "Jun";
		}
		if (T2.equals("07")) {
			T2 = "Jul";
		}
 
		if (T2.equals("08")) {
			T2 = "Aug";
		}
		if (T2.equals("09")) {
			T2 = "Sep";
		}
		if (T2.equals("10")) {
			T2 = "Oct";
		}
		if (T2.equals("11")) {
			T2 = "Nov";
		}
		if (T2.equals("12")) {
			T2 = "Dec";
		}
		t = T1 + T2;
		return t;
	}

	public static String mobileUtil(String time) {
		String t = time;
		System.out.println(time.length());
		if (time.length() == 11) {
			t = time.substring(0, 3) + "*****" + time.substring(7, 11);
		}

		return t;
	}

	/**
	 * MD5�����㷨
	 * 
	 * @param plainText
	 * @return
	 */
	public static String Md5(String plainText) {
		String md5Password = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();

			int i;

			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			md5Password = buf.toString();

			System.out.println("result: " + buf.toString());// 32λ�ļ���
			System.out.println("result: " + buf.toString().substring(8, 24));// 16λ�ļ���

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return md5Password.toUpperCase();
	}

	// ȥ�����пո�
	public static String replaceBlank(String str) {
		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}

	/**
	 * �������
	 * 
	 * @param src
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String encryptThreeDESECB(String src, String key)
			throws Exception {
		DESedeKeySpec dks = new DESedeKeySpec(key.getBytes("UTF-8"));
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
		SecretKey securekey = keyFactory.generateSecret(dks);

		Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, securekey);
		byte[] b = cipher.doFinal(src.getBytes());

		Log.e("code", android.util.Base64.encodeToString(b, Base64.DEFAULT)
				.replaceAll("\r", "").replaceAll("\n", ""));
		System.out.println("code```"
				+ android.util.Base64.encodeToString(b, Base64.DEFAULT)
						.replaceAll("\r", "").replaceAll("\n", ""));
		return android.util.Base64.encodeToString(b, Base64.DEFAULT)
				.replaceAll("\r", "").replaceAll("\n", "");
	}

	/***
	 * �������
	 * 
	 * @param paramValues
	 * @param secret
	 * @return ����sign
	 */
	public static String sign(Map<String, String> paramValues, String secret) {
		StringBuilder sign = new StringBuilder();
		try {
			byte[] sha1Digest = null;
			StringBuilder sb = new StringBuilder();
			List<String> paramNames = new ArrayList<String>(paramValues.size());
			paramNames.addAll(paramValues.keySet());
			Collections.sort(paramNames);
			sb.append(secret);
			for (String paramName : paramNames) {
				sb.append(paramName).append(paramValues.get(paramName));
			}
			sb.append(secret);
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			sha1Digest = md.digest(sb.toString().getBytes("UTF-8"));
			for (int i = 0; i < sha1Digest.length; i++) {
				String hex = Integer.toHexString(sha1Digest[i] & 0xFF);
				if (hex.length() == 1) {
					sign.append("0");
				}
				sign.append(hex.toUpperCase());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		Log.e("sign", sign.toString());
		return sign.toString();
	}

    public static String join(List<String> list, String separator) {
        StringBuilder sb = new StringBuilder();
        int len = list.size();
        int i = 0;
        for (String s : list)
        {
            sb.append(s);
            i++;
            if (i < len) {
                sb.append(separator);
            }
        }

        return sb.toString();
    }

    public static List<Integer> integerList(List<Integer> list) {
        List<Integer> newIds = new ArrayList<Integer>();
        for (Object id: list) {
            if (id instanceof Double) {
                newIds.add(((Double)id).intValue());
            } else if (id instanceof Integer) {
                newIds.add((Integer)id);
            }
        }

        return newIds;
    }

    public static String priceShow(int price) {
        double d = price / 100.0;
        String strPrice = String.format("%.2f", d);
        return strPrice;
    }

    public static int intSex(String sex) {
        return sex.equals("男") ? 1 : 2;
    }

    public static String strSex(int sex) {
        return sex == 1 ? "男" : "女";
    }

    public static String priceShow(String price) {
        return priceShow(Integer.parseInt(price));
    }
}
