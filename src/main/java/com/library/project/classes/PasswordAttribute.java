package com.library.project.classes;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.library.project.contracts.Attribute;

public final class PasswordAttribute implements Attribute {

    /**
     * Valid email address
     */
    private String password;

    /**
     * @param String password
     */
    public PasswordAttribute(String password)
    {   
        this.password = password;
    }

    /**
     * @return String
     */
    public String getValue()
    {
        return this.password;
    }
    
    /**
     * @param password
     * @return String
     */
    public String toMd5() {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] messageDigestArray = messageDigest.digest(this.password.getBytes());
            return new BigInteger(1, messageDigestArray).toString(16);
        } catch (NoSuchAlgorithmException e) {
            return this.password;
        }
    }

    /**
     * @param password
     * @param hashedPassword
     * @return
     */
    public static boolean verify(PasswordAttribute password, String hashedPassword)
    {
        return password.toMd5().equals(hashedPassword);
    }
}
