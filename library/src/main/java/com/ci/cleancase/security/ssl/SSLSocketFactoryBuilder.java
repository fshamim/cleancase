package com.ci.cleancase.security.ssl;

import android.content.Context;

import com.ci.cleanlog.L;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by fshamim on 29.09.14.
 */
public class SSLSocketFactoryBuilder {

    /**
     * restrict instantiation for this class
     */
    private SSLSocketFactoryBuilder(){

    }

    /**
     * Create SSLSocketFactory with the given parameters
     * @param context android context
     * @param keystoreResId key store resource id
     * @param keystorePass key store password
     * @return valid SSLSocketFactory instance for given parameters
     */
    public static SSLSocketFactory getSSLSocketFactory(Context context, int keystoreResId, String keystorePass) {
        try {
            // Get an instance of the Bouncy Castle KeyStore format
            KeyStore trusted = KeyStore.getInstance("BKS");
            // Get the raw resource, which contains the keystore with
            // your trusted certificates (root and any intermediate certs)
            InputStream in = context.getResources().openRawResource(keystoreResId);
            try {
                // Initialize the keystore with the provided trusted certificates
                // Also provide the password of the keystore
                trusted.load(in, keystorePass.toCharArray());
            } finally {
                in.close();
            }
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            tmf.init(trusted);
            SSLContext sslContext = getSslContext();

            sslContext.init(null, tmf.getTrustManagers(), new SecureRandom());

            return sslContext.getSocketFactory();
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    private static SSLContext getSslContext() throws NoSuchAlgorithmException {
        final String tlSv1 = "TLSv1";
        final String tlSv12 = "TLSv1.2";
        SSLContext sslContext;
        try {
            // Create a SSLContext with the certificate
            sslContext = SSLContext.getInstance(tlSv12);
        } catch (NoSuchAlgorithmException ex) {
            //fallback, try TLS 1.0, available in Gingerbread
            sslContext = SSLContext.getInstance(tlSv1);
            L.e("ssl", ex.getMessage(), ex);
        }
        return sslContext;
    }
}
