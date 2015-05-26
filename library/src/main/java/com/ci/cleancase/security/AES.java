package com.ci.cleancase.security;

import com.ci.cleanlog.L;

import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.inject.Singleton;

/**
 * Created by jrodriguez on 08/09/14.
 */

/**
 * This class provides encryption and decryption of messages or information.
 * */

@Singleton
 public class AES {
    /**
     * Initialization vector has to be changed. Allow block cipher
     * */
    private String iv = "fedcba9876543210";
    private static final String TAG = AES.class.getSimpleName();
    private IvParameterSpec ivspec;
    private SecretKeySpec keyspec;
    private Cipher cipher;
    /*KEY has to be changed. */
    private String secretKey = "0123456789abcdef";
    /*Text Format*/
    private static String utf="UTF-8";
    /**
     * Constructor
     * */
    public AES(){

        ivspec = new IvParameterSpec(iv.getBytes(Charset.forName(utf)));

        keyspec = new SecretKeySpec(secretKey.getBytes(Charset.forName(utf)), "AES");

        try {
            cipher = Cipher.getInstance("AES/CBC/NoPadding");
        } catch (NoSuchAlgorithmException e) {
            L.e(TAG, "NoSuchAlgorithmException", e);
        } catch (NoSuchPaddingException e) {
            L.e(TAG, "NoSuchPaddingException", e);
        }
    }
    /**
     * Encryption Method
     * */
    public byte[] encrypt(String text) {
        byte[] encrypted = new byte[0];

        if(text == null || text.length() == 0) {
            return encrypted;
        }
        try {
            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);

            encrypted = cipher.doFinal(padString(text).getBytes(Charset.forName(utf)));
        } catch (InvalidAlgorithmParameterException e) {
            L.e(TAG, "InvalidAlgorithmParameterException", e);
        } catch (InvalidKeyException e) {
            L.e(TAG, "InvalidKeyException", e);
        } catch (BadPaddingException e) {
            L.e(TAG, "BadPaddingException", e);
        } catch (IllegalBlockSizeException e) {
            L.e(TAG, "IllegalBlockSizeException", e);
        }

        return encrypted;
    }
    /**
     * Decryption method.
     * */
    public byte[] decrypt(String code){
        byte[] decrypted = new byte[0];
        if(code == null || code.length() == 0) {
            return decrypted;
        }
        try {
            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

            decrypted = cipher.doFinal(hexToBytes(code));
        }  catch (InvalidKeyException e) {
            L.e(TAG, "InvalidKeyException", e);
        } catch (BadPaddingException e) {
            L.e(TAG, "BadPaddingException", e);
        } catch (InvalidAlgorithmParameterException e) {
            L.e(TAG, "InvalidAlgorithmParameterException", e);
        } catch (IllegalBlockSizeException e) {
            L.e(TAG, "IllegalBlockSizeException", e);
        }
        return decrypted;
    }
    /**
     * Byte to Hexadecimal
     * */
    public static String bytesToHex(byte[] data){
        if (data==null) {
            return "";
        }

        int len = data.length;
        StringBuilder buf = new StringBuilder();
        for (int i=0; i<len; i++) {
            if ((data[i]&0xFF)<16){
                buf.append("0");
            }
            buf.append(Integer.toHexString(data[i] & 0xFF));
        }
        return buf.toString();
    }
    /**
     * Hexadecimal to Byte
     * */
    public static byte[] hexToBytes(String str) {
        if (str==null) {
            return new byte[0];
        } else if (str.length() < 2) {
            return new byte[0];
        } else {
            int len = str.length() / 2;
            byte[] buffer = new byte[len];
            for (int i=0; i<len; i++) {
                buffer[i] = (byte) Integer.parseInt(str.substring(i*2,i*2+2),16);
            }
            return buffer;
        }
    }
    private static String padString(String source){
        char paddingChar = ' ';
        int size = 16;
        int x = source.length() % size;
        int padLength = size - x;
        StringBuilder buf = new StringBuilder();
        buf.append(source);
        for (int i = 0; i < padLength; i++){
            buf.append(paddingChar);
        }

        return buf.toString();
    }
}