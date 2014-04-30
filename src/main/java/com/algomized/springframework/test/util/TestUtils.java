package com.algomized.springframework.test.util;

import java.nio.charset.Charset;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.codec.Base64;

/**
 * Static utility methods for testing.
 * 
 * @author Poh Kah Kong
 */
public class TestUtils {	
	/**
	 * Media type for json utf8
	 */
	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(
			MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(),                       
			Charset.forName("utf8")                    
			);
	
	/**
	 * Creates a string with the given length
	 * 
	 * @param length the length of the string
	 * @return a string of the given length
	 */
	public static String createStringWithLength(int length) {
        StringBuilder builder = new StringBuilder();
        for (int index = 0; index < length; index++) {
            builder.append("a");
        }
        return builder.toString();
    }
	
	/**
	 * Encodes the username and password for Http Basic Authentication.
	 * 
	 * @param username the username
	 * @param password the password
	 * @return the encoded token
	 */
	public static String encodeForHttpBasicAuth(String username, String password) {
		String text = username + ":" + password;
		return new String(Base64.encode(text.getBytes()));
	}		
}
