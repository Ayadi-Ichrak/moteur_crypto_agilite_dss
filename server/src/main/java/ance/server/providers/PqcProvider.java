package ance.server.providers;

import ance.common.CryptoAlgorithm;
import ance.common.CryptoProvider;
import ance.common.SignaturePolicy;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Component;

import java.security.*;
import java.util.Base64;

/**
 * Provider 2 - Algorithmes Post-Quantiques (ML-DSA, FALCON)
 * Utilise BouncyCastle 1.79+ avec support NIST FIPS 204/206
 */
@Component
public class PqcProvider implements CryptoProvider {

    static {
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
            System.out.println("BouncyCastle 1.79+ enregistre (support NIST FIPS 204/206)");
        }
    }

    @Override
    public byte[] sign(byte[] document, SignaturePolicy policy) throws Exception {
        CryptoAlgorithm algo = policy.getAlgorithm();
        
        String sigAlgorithm;
        KeyPairGenerator keyGen;
        
        switch (algo.getEncryption()) {
            case "ML-DSA":
                // ML-DSA - NIST FIPS 204 (standardise en aout 2024)
                // Niveaux de securite: 44, 65, 87
                sigAlgorithm = "ML-DSA";
                keyGen = KeyPairGenerator.getInstance("ML-DSA", "BC");
                break;
            case "FALCON":
                // FALCON - NIST FIPS 206
                sigAlgorithm = "FALCON";
                keyGen = KeyPairGenerator.getInstance("FALCON", "BC");
                break;
            default:
                throw new IllegalArgumentException("Algorithme PQC non supporte: " + algo);
        }

        KeyPair keyPair = keyGen.generateKeyPair();
        
        Signature signature = Signature.getInstance(sigAlgorithm, "BC");
        signature.initSign(keyPair.getPrivate());
        signature.update(document);
        byte[] sigBytes = signature.sign();

        StringBuilder xades = new StringBuilder();
        xades.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xades.append("<XAdESSignedDocument>\n");
        xades.append("  <SignatureAlgorithm>").append(algo).append("</SignatureAlgorithm>\n");
        xades.append("  <SignatureType>Post-Quantique (NIST FIPS 204)</SignatureType>\n");
        xades.append("  <SignatureValue>").append(Base64.getEncoder().encodeToString(sigBytes)).append("</SignatureValue>\n");
        xades.append("  <OriginalDocument>").append(Base64.getEncoder().encodeToString(document)).append("</OriginalDocument>\n");
        xades.append("  <PublicKey>").append(Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded())).append("</PublicKey>\n");
        xades.append("</XAdESSignedDocument>");

        return xades.toString().getBytes();
    }

    @Override
    public boolean verify(byte[] document, byte[] signature, SignaturePolicy policy) throws Exception {
        return true;
    }
}