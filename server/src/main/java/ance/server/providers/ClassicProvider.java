package ance.server.providers;

import ance.common.CryptoAlgorithm;
import ance.common.CryptoProvider;
import ance.common.SignaturePolicy;
import org.springframework.stereotype.Component;

import java.security.*;
import java.util.Base64;

/**
 * Provider 1 - Algorithmes Classiques (RSA, ECDSA)
 * Standards: FIPS 186-5 (ECDSA), PKCS#1 v2.2 (RSA)
 */
@Component
public class ClassicProvider implements CryptoProvider {

    @Override
    public byte[] sign(byte[] document, SignaturePolicy policy) throws Exception {
        CryptoAlgorithm algo = policy.getAlgorithm();
        
        KeyPairGenerator keyGen;
        String sigAlgorithm;
        
        switch (algo.getEncryption()) {
            case "RSA":
                keyGen = KeyPairGenerator.getInstance("RSA", "BC");
                keyGen.initialize(algo.getKeySize());
                sigAlgorithm = algo.getDigest().replace("-", "") + "withRSA";
                break;
            case "ECDSA":
                keyGen = KeyPairGenerator.getInstance("ECDSA", "BC");
                keyGen.initialize(algo.getKeySize());
                sigAlgorithm = algo.getDigest().replace("-", "") + "withECDSA";
                break;
            default:
                throw new IllegalArgumentException("Algorithme classique non supporte: " + algo);
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
        xades.append("  <SignatureType>Classique (FIPS 186-5 / PKCS#1)</SignatureType>\n");
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