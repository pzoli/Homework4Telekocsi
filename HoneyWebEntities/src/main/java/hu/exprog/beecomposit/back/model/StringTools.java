package hu.exprog.beecomposit.back.model;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Hex;

public class StringTools {

	public static Pattern i18pattern = Pattern.compile("#\\{'([-\\w]+)'\\}");

	public static String getMD5HashHex(String src) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		byte[] bytesOfMessage = src.getBytes("UTF-8");
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] digest = md.digest(bytesOfMessage);
		String result = new String(Hex.encodeHex(digest));
		return result;
	}
}
