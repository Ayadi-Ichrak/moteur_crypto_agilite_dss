package ance.client;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Signature;
import java.util.Base64;

/**
 * Gestionnaire de keystore LOCAL
 * La cle privee ne quitte JAMAIS cette machine
 */
public class LocalKeyStoreManager {

    private final String keystorePath;
    private final String password;

    public LocalKeyStoreManager(String keystorePath, String password) {
        this.keystorePath = keystorePath;
        this.password = password;
    }

    /**
     * Signe localement les donnees avec la cle privee du client
     */
    public String signLocally(byte[] dataToSign, String algorithm) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        try (FileInputStream fis = new FileInputStream(keystorePath)) {
            keyStore.load(fis, password.toCharArray());
        }

        String alias = keyStore.aliases().nextElement();
        PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, password.toCharArray());

        String sigAlgorithm;
        if (algorithm.startsWith("RSA")) {
            sigAlgorithm = "SHA256withRSA";
        } else if (algorithm.startsWith("ECDSA")) {
            sigAlgorithm = "SHA256withECDSA";
        } else {
            sigAlgorithm = algorithm;
        }

        Signature signature = Signature.getInstance(sigAlgorithm);
        signature.initSign(privateKey);
        signature.update(dataToSign);
        byte[] rawSignature = signature.sign();

        return Base64.getEncoder().encodeToString(rawSignature);
    }

    /**
     * Affiche les informations du certificat
     */
    public void printCertificateInfo() throws Exception {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        try (FileInputStream fis = new FileInputStream(keystorePath)) {
            keyStore.load(fis, password.toCharArray());
        }

        String alias = keyStore.aliases().nextElement();
        java.security.cert.Certificate cert = keyStore.getCertificate(alias);
        System.out.println("📜 Certificat : " + cert.toString());
    }
}
